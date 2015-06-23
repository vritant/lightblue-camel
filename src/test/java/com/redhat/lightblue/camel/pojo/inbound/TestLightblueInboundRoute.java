package com.redhat.lightblue.camel.pojo.inbound;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;
import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadResource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.camel.LightblueEndpoint;
import com.redhat.lightblue.camel.LightblueExternalResource;
import com.redhat.lightblue.camel.LightblueExternalResource.LightblueTestMethods;
import com.redhat.lightblue.camel.dataformat.JacksonXmlDataFormat;
import com.redhat.lightblue.camel.model.User;
import com.redhat.lightblue.camel.transformer.LightblueInsertRequestTransformer;
import com.redhat.lightblue.camel.verifier.LightblueErrorVerifier;
import com.redhat.lightblue.client.expression.query.ValueQuery;
import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.request.data.DataFindRequest;

public class TestLightblueInboundRoute extends CamelTestSupport {

    @ClassRule
    public static LightblueExternalResource lightblue = new LightblueExternalResource(new LightblueTestMethods() {

        @Override
        public JsonNode[] getMetadataJsonNodes() throws Exception {
            return new JsonNode[]{
                    loadJsonNode("./inbound/user.json")
            };
        }

    });

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @EndpointInject(uri = "lightblue://inboundTest")
    protected LightblueEndpoint lbEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {

            @Override
            public void configure() {
                lightblue.init();
                LightblueEndpoint.registerLightblueClient("inboundTest", lightblue.getLightblueClient());

                from("direct:start")
                        .unmarshal(new JacksonXmlDataFormat(User[].class)).convertBodyTo(User[].class)
                        .bean(new LightblueInsertRequestTransformer("user", "1.0.0"))
                        .to("lightblue://inboundTest")
                        .bean(new LightblueErrorVerifier())
                        .to("mock:result");
            }
        };
    }

    @Test
    public void testMessageToLightblue() throws Exception {
        String message = loadResource("./inbound/user-message.xml");
        template.sendBody(message);

        DataFindRequest findRequest = new DataFindRequest("user", null);
        findRequest.where(ValueQuery.withValue("objectType = user"));
        findRequest.select(FieldProjection.includeField("*"));
        User[] users = lightblue.getLightblueClient().data(findRequest, User[].class);

        assertNotNull(users);
        assertEquals(2, users.length);

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);

        assertMockEndpointsSatisfied();
    }

}
