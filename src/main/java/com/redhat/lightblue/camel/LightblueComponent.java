package com.redhat.lightblue.camel;

import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.LightblueRequest;

/**
 * Represents the component that manages {@link LightblueEndpoint}.
 */
public class LightblueComponent extends UriEndpointComponent {

    private LightblueClient lightblueClient;
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

    @Inject
    public void setLightblueClient(LightblueClient lightblueClient) {
        this.lightblueClient = lightblueClient;
    }

    @Inject
    public void setLightbluePollingRequest(@Nullable LightblueRequest lightbluePollingRequest) {
        this.lightbluePollingRequest = lightbluePollingRequest;
    }

}
