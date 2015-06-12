package com.redhat.lightblue.camel.pojo.outbound;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.camel.LightblueEndpoint;
import com.redhat.lightblue.camel.LightblueExternalResource;
import com.redhat.lightblue.camel.LightblueExternalResource.LightblueTestMethods;
import com.redhat.lightblue.camel.dataformat.JacksonXmlDataFormat;
import com.redhat.lightblue.camel.model.Company;
import com.redhat.lightblue.camel.model.Event;
import com.redhat.lightblue.camel.transformer.LightblueResponseTransformer;
import com.redhat.lightblue.camel.verifier.LightblueErrorVerifier;
import com.redhat.lightblue.client.expression.query.ValueQuery;
import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public class TestLightblueOutboundRoute extends CamelTestSupport {

    @BeforeClass
    public static void prepareMetadataDatasources() {
        System.setProperty("mongo.datasource", "mongodata");
    }

    @ClassRule
    public static LightblueExternalResource lightblue = new LightblueExternalResource(new LightblueTestMethods() {

        @Override
        public JsonNode[] getMetadataJsonNodes() throws Exception {
            return new JsonNode[]{
                    loadJsonNode("./outbound/company.json")
            };
        }

    });

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                lightblue.init();
                LightblueEndpoint.registerLightblueClient("outboundTest", lightblue.getLightblueClient());

                from("direct:start")
                        .process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {
                                Event event = exchange.getIn().getBody(Event.class);

                                DataFindRequest findRequest = new DataFindRequest("company", "1.0.0");
                                findRequest.where(ValueQuery.withValue("_id = " + event.getId()));
                                findRequest.select(FieldProjection.includeFieldRecursively("*"));

                                ConsumerTemplate consumer = context().createConsumerTemplate();
                                consumer.start();
                                LightblueResponse response = consumer.receiveBody(
                                        "lightblue://outboundTest" + LightblueEndpoint.buildUriParameters(findRequest, false),
                                        1000,
                                        LightblueResponse.class);
                                consumer.stop();

                                exchange.getIn().setBody(response, LightblueResponse.class);
                            }
                        })
                        .bean(new LightblueErrorVerifier())
                        .bean(new LightblueResponseTransformer<Company[]>(Company[].class))
                        .marshal(new JacksonXmlDataFormat())
                        .to("mock:result");
            };
        };
    }

    @Before
    public void before() throws Exception {
        lightblue.loadData("company", "1.0.0", "./outbound/data/insert/companies.json");
    }

    @Test
    public void testMessageFromLightblue() throws Exception {
        template.sendBody(new Event("2", "Something happened", false));

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }
}
