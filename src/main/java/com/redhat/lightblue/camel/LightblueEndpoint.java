package com.redhat.lightblue.camel;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.LightblueRequest;

/**
 * Lightblue endpoint.
 */
@UriEndpoint(scheme = "lightblue", title = "Lightblue", syntax = "lightblue:name", consumerClass = LightblueScheduledPollConsumer.class, label = "Lightblue")
public class LightblueEndpoint extends DefaultEndpoint {

    private LightblueClient lightblueClient;

    private LightblueRequest lightbluePollingRequest;

    public LightblueEndpoint() {}

    public LightblueEndpoint(String uri, LightblueComponent component) {
        super(uri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new LightblueProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new LightblueScheduledPollConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    //@Inject
    public void setLightblueClient(LightblueClient lightblueClient) {
        this.lightblueClient = lightblueClient;
    }

    //@Inject
    public void setLightbluePollingRequest(LightblueRequest lightblueRequest) {
        this.lightbluePollingRequest = lightblueRequest;
    }

    public LightblueRequest getLightbluePollingRequest() {
        return lightbluePollingRequest;
    }

    public LightblueClient getLightblueClient() {
        return lightblueClient;
    }

}
