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
 * Represents the component that manages {@link LightblueScheduledPollEndpoint}.
 */
public class LightblueComponent extends UriEndpointComponent {

    private LightblueClient lightblueClient;
    private Map<String, LightblueRequest> lightbluePollingRequests;

    public LightblueComponent() {
        super(LightblueScheduledPollEndpoint.class);
    }

    public LightblueComponent(CamelContext context) {
        super(context, LightblueScheduledPollEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        LightblueScheduledPollEndpoint endpoint = new LightblueScheduledPollEndpoint(uri, this);

        // TODO: how to create the endpoint using guice?
        endpoint.setLightblueClient(lightblueClient);
        if (lightbluePollingRequests != null) {
            endpoint.setLightbluePollingRequest(lightbluePollingRequests.get(remaining));
        }
        setProperties(endpoint, parameters);
        return endpoint;
    }

    @Inject
    public void setLightblueClient(LightblueClient lightblueClient) {
        this.lightblueClient = lightblueClient;
    }

    @Inject
    public void setLightbluePollingRequests(@Nullable Map<String, LightblueRequest> lightbluePollingRequests) {
        this.lightbluePollingRequests = lightbluePollingRequests;
    }

}
