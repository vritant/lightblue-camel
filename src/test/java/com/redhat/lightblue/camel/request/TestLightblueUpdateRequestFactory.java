package com.redhat.lightblue.camel.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;

import com.redhat.lightblue.client.expression.query.ValueQuery;
import com.redhat.lightblue.client.expression.update.Update;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class TestLightblueUpdateRequestFactory {

    // @Test
    public void test() throws Exception {
        LightblueUpdateRequestFactory factory = new LightblueUpdateRequestFactory("entity", "1.2.3");

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setBody(new Update[] { new DummyUpdate("some update") });
        exchange.getIn().setHeader(LightblueRequestFactory.HEADER_QUERY, ValueQuery.withValue("field = value"));
        factory.process(exchange);

        DataUpdateRequest request = exchange.getIn().getBody(DataUpdateRequest.class);

        assertNotNull(request);
        assertEquals(
                "{\"query\":{\"field\":\"field\",\"op\":\"=\",\"rvalue\":\"value\"},\"update\":[some update],\"projection\":[{\"field\":\"*\",\"include\":true,\"recursive\":true}]}",
                request.getBody());
    }

    private static class DummyUpdate implements Update {

        private final String update;

        public DummyUpdate(String update) {
            this.update = update;
        }

        @Override
        public String toJson() {
            return update;
        }

    }

}
