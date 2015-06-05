package com.redhat.lightblue.camel;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link LightblueEndpoint}.
 */
public class LightblueComponent extends UriEndpointComponent {

    public LightblueComponent() {
        super(LightblueEndpoint.class);
    }

    public LightblueComponent(CamelContext context) {
        super(context, LightblueEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new LightblueEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
