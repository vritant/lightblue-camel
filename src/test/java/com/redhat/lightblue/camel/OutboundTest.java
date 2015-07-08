package com.redhat.lightblue.camel;

import static com.redhat.lightblue.util.test.AbstractJsonNodeTest.loadJsonNode;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.redhat.lightblue.client.expression.query.ValueQuery;
import com.redhat.lightblue.client.integration.test.AbstractLightblueClientCRUDController;
import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;

/**
 * Test for {@link TestOutboundRoute}.
 * 
 * @author mpatercz
 *
 */
public class OutboundTest extends AbstractLightblueClientCRUDController {

    public OutboundTest() throws Exception {
        super();
    }

    @Override
    public JsonNode[] getMetadataJsonNodes() throws Exception {
        return new JsonNode[]{loadJsonNode("./outbound/event.json"), loadJsonNode("./inbound/user.json")};
    }

    CamelContext context;
    MockEndpoint eventResultEndpoint;
    MockEndpoint userResultEndpoint;

    @Before
    public void setupCamel() throws Exception {
        // polling request
        Map<String, LightblueRequest> requestMap = new HashMap<>();
        DataFindRequest eventFindRequest = new DataFindRequest("event", "1.0.0");
        eventFindRequest.where(ValueQuery.withValue("processed = false"));
        eventFindRequest.select(FieldProjection.includeFieldRecursively("*"));
        requestMap.put("eventPoller", eventFindRequest);
        DataFindRequest userFindRequest = new DataFindRequest("user", "1.0.0");
        userFindRequest.where(ValueQuery.withValue("firstName = Taylor"));
        userFindRequest.select(FieldProjection.includeFieldRecursively("*"));
        requestMap.put("userPoller", userFindRequest);

        // init guice and register the client and polling request
        Injector injector = Guice.createInjector(new TestCamelModule(getLightblueClient(), requestMap));

        // init camel context
        context = injector.getInstance(CamelContext.class);

        userResultEndpoint = context.getEndpoint("mock:userResult", MockEndpoint.class);
        eventResultEndpoint = context.getEndpoint("mock:eventResult", MockEndpoint.class);

        // start camel
        context.start();
    }

    @Test
    public void testMessageFromLightblue() throws Exception {
        // load events
        loadData("event", "1.0.0", "./outbound/data/insert/events.json");
        loadData("user", "1.0.0", "./outbound/data/insert/users.json");

        userResultEndpoint.expectedBodiesReceived("<Users xmlns=\"\"><item><firstName>Taylor</firstName><lastName>Swift</lastName></item></Users>");
        eventResultEndpoint
                .expectedBodiesReceived("<Events xmlns=\"\"><item><name>Something happened</name><processed>false</processed><_id>2</_id></item><item><name>Something else happened</name><processed>false</processed><_id>3</_id></item></Events>");
        userResultEndpoint.assertIsSatisfied();
        eventResultEndpoint.assertIsSatisfied();

    }
    @After
    public void tearDownCamel() throws Exception {
        context.stop();
    }

}
