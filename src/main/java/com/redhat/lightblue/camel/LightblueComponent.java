package com.redhat.lightblue.camel;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

import com.google.inject.Inject;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.LightblueRequest;

/**
 * Represents the component that manages {@link LightblueEndpoint}.
 */
public class LightblueComponent extends UriEndpointComponent {

    @Inject
    private LightblueClient lightblueClient;
    @Inject(optional=true)
    private LightblueRequest lightbluePollingRequest;

    public LightblueComponent() {
        super(LightblueEndpoint.class);
    }

    public LightblueComponent(CamelContext context) {
        super(context, LightblueEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        LightblueEndpoint endpoint = new LightblueEndpoint(uri, this);

        // TODO: how to create the endpoint using guice?
        endpoint.setLightblueClient(lightblueClient);
        endpoint.setLightbluePollingRequest(lightbluePollingRequest);

        setProperties(endpoint, parameters);
        return endpoint;
    }

    public void setLightblueClient(LightblueClient lightblueClient) {
        this.lightblueClient = lightblueClient;
    }

    public void setLightbluePollingRequest(LightblueRequest lightbluePollingRequest) {
        this.lightbluePollingRequest = lightbluePollingRequest;
    }


}
