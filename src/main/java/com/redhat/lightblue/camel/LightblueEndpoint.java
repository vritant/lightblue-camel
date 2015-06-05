package com.redhat.lightblue.camel;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

import com.redhat.lightblue.client.LightblueClient;

/**
 * Represents a Lightblue endpoint.
 */
@UriEndpoint(scheme = "lightblue", title = "Lightblue", syntax = "lightblue:name", consumerClass = LightblueEntityPollConsumer.class, label = "Lightblue")
public class LightblueEndpoint extends DefaultEndpoint {
    @UriPath
    @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;

    private LightblueClient lightblueClient;

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
        return new LightblueEntityPollConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }

    public void setLightblueClient(LightblueClient lightblueClient) {
        this.lightblueClient = lightblueClient;
    }

    public LightblueClient getLightblueClient() {
        /*if (lightblueClient == null) {
            lightblueClient = new LightblueHttpClient();
        }*/
        return lightblueClient;
    }
}
