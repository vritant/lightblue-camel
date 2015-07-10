package com.redhat.lightblue.camel;

import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

import com.redhat.lightblue.client.LightblueClient;

/**
 * Represents the component that manages {@link LightblueScheduledPollEndpoint}.
 */
public class LightblueComponent extends UriEndpointComponent {

    private LightblueClient lightblueClient;
    private LightblueRequestsHolder lightblueRequests;

    public LightblueComponent() {
        super(LightblueScheduledPollEndpoint.class);
    }

    public LightblueComponent(CamelContext context) {
        super(context, LightblueScheduledPollEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        LightblueScheduledPollEndpoint endpoint = new LightblueScheduledPollEndpoint(uri, this);

        // since there is only one consumer right now, its okay to use remaining
        // clause for identifying the request. if needed, we can use a uri param
        endpoint.setLightblueClient(lightblueClient);
        if (lightblueRequests != null) {
            endpoint.setLightbluePollingRequest(lightblueRequests.get(remaining));
        }
        setProperties(endpoint, parameters);
        return endpoint;
    }

    @Inject
    public void setLightblueClient(LightblueClient lightblueClient) {
        this.lightblueClient = lightblueClient;
    }

    @Inject
    public void setLightbluePollingRequests(@Nullable LightblueRequestsHolder lightblueRequests) {
        this.lightblueRequests = lightblueRequests;
    }

}
