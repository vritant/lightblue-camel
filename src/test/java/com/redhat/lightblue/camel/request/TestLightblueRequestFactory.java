package com.redhat.lightblue.camel.request;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;

import com.redhat.lightblue.camel.model.Event;
import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.expression.query.ValueQuery;
import com.redhat.lightblue.client.http.HttpMethod;
import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.AbstractLightblueRequest;
import com.redhat.lightblue.client.request.data.LiteralDataRequest;

public class TestLightblueRequestFactory {

    @Test
    public void testEntityNameAndVersion_FromConstructor() throws Exception {
        DummyLightblueRequestFactory factory = new DummyLightblueRequestFactory("myentity", "1.2.3");

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setBody(new Event[]{new Event("1", "someEvent", false)});

        factory.process(exchange);

        assertEquals("myentity", factory.getEntityName());
        assertEquals("1.2.3", factory.getEntityVersion());
    }

    @Test
    public void testEntityNameAndVersion_FromExchange() throws Exception {
        DummyLightblueRequestFactory factory = new DummyLightblueRequestFactory();

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setHeader(LightblueRequestFactory.HEADER_ENTITY_NAME, "myentity");
        exchange.getIn().setHeader(LightblueRequestFactory.HEADER_ENTITY_VERSION, "1.2.3");
        exchange.getIn().setBody(new Event[]{new Event("1", "someEvent", false)});

        factory.process(exchange);

        assertEquals("myentity", factory.getEntityName());
        assertEquals("1.2.3", factory.getEntityVersion());
    }

    @Test
    public void testProjections_Default() throws Exception {
        DummyLightblueRequestFactory factory = new DummyLightblueRequestFactory("myentity", "1.2.3");

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());

        exchange.getIn().setBody(new Event[]{new Event("1", "someEvent", false)});

        factory.process(exchange);

        assertArrayEquals(LightblueRequestFactory.DEFAULT_PROJECTIONS, factory.getProjections());
    }

    @Test
    public void testProjections_FromExchange() throws Exception {
        Projection[] projections = new Projection[]{};

        DummyLightblueRequestFactory factory = new DummyLightblueRequestFactory("myentity", "1.2.3");

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setHeader(LightblueRequestFactory.HEADER_PROJECTIONS, projections);

        exchange.getIn().setBody(new Event[]{new Event("1", "someEvent", false)});

        factory.process(exchange);

        assertArrayEquals(projections, factory.getProjections());
    }

    @Test
    public void testProjections_Body() throws Exception {
        DummyLightblueRequestFactory factory = new DummyLightblueRequestFactory("myentity", "1.2.3");

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());

        exchange.getIn().setBody(new Event[]{new Event("1", "someEvent", false)});

        factory.process(exchange);

        AbstractLightblueRequest request = factory.getBody(AbstractLightblueRequest.class);

        assertNotNull(request);
        assertEquals("myentity", request.getEntityName());
        assertEquals("1.2.3", request.getEntityVersion());
    }

    @Test
    public void testProjections_Query() throws Exception {
        Query query = ValueQuery.withValue("field = value");

        DummyLightblueRequestFactory factory = new DummyLightblueRequestFactory("myentity", "1.2.3");

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setHeader(LightblueRequestFactory.HEADER_QUERY, query);

        exchange.getIn().setBody(new Event[]{new Event("1", "someEvent", false)});

        factory.process(exchange);

        assertEquals(query, factory.getQuery());
    }

    private static class DummyLightblueRequestFactory extends LightblueRequestFactory<AbstractLightblueRequest> {

        public DummyLightblueRequestFactory() {
            super();
        }

        public DummyLightblueRequestFactory(String entityName, String entityVersion) {
            super(entityName, entityVersion);
        }

        @Override
        protected AbstractLightblueRequest createRequest(String entityName, String entityVersion) {
            return new LiteralDataRequest(entityName, entityVersion, "", HttpMethod.GET, "");
        }

    }

}
