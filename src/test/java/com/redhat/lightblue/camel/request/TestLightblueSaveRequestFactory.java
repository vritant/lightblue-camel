package com.redhat.lightblue.camel.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;

import com.redhat.lightblue.camel.model.Event;
import com.redhat.lightblue.client.request.data.DataSaveRequest;

public class TestLightblueSaveRequestFactory {

    // @Test
    public void test() throws Exception {
        LightblueSaveRequestFactory factory = new LightblueSaveRequestFactory("entity", "1.2.3");

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setBody(new Event[] { new Event("1", "someEvent", false) });
        factory.process(exchange);

        DataSaveRequest request = exchange.getIn().getBody(DataSaveRequest.class);

        assertNotNull(request);
        assertEquals(
                "{\"data\":[{\"name\":\"someEvent\",\"processed\":false,\"_id\":\"1\"}],\"projection\":[{\"field\":\"*\",\"include\":true,\"recursive\":true}]}",
                request.getBody());
    }

}
