package com.redhat.lightblue.camel.outbound;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;

import org.apache.camel.Consume;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.lightblue.camel.LightblueEndpoint;
import com.redhat.lightblue.camel.LightblueEntityPollConsumer;
import com.redhat.lightblue.camel.LightblueExternalResource;
import com.redhat.lightblue.camel.LightblueExternalResource.LightblueTestMethods;
import com.redhat.lightblue.camel.model.Event;
import com.redhat.lightblue.camel.transformer.LightblueResponseTransformer;
import com.redhat.lightblue.camel.verifier.LightblueErrorVerifier;
import com.redhat.lightblue.client.expression.query.ValueQuery;

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
                    loadJsonNode("./outbound/event.json")
            };
        }

    });

    @Produce(uri = "direct:end")
    protected ProducerTemplate template;

    @Consume(uri = "lightblue://outboundTest")
    protected LightblueEntityPollConsumer consumer;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {

            @Override
            public void configure() {
                lightblue.init();

                from("lightblue://outboundTest")
                        .bean(new LightblueErrorVerifier())
                        .bean(new LightblueResponseTransformer<Event[]>(Event[].class))
                        .to("mock:result");

                ((LightblueEndpoint) getContext().getEndpoint("lightblue://outboundTest"))
                        .setLightblueClient(lightblue.getLightblueClient());
            }
        };
    }

    @Before
    public void before() throws Exception {
        lightblue.loadData("event", "1.0.0", "./outbound/data/insert/events.json");
        consumer.setQuery("event", "1.0.0", ValueQuery.withValue("processed = false"));
    }

    @Test
    public void testMessageFromLightblue() throws Exception {
        System.out.println("here");
    }

}
