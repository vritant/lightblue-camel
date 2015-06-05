package com.redhat.lightblue.camel;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;

/**
 * The Lightblue producer.
 */
public class LightblueProducer extends DefaultProducer {

    private final LightblueEndpoint endpoint;

    public LightblueProducer(LightblueEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        AbstractLightblueDataRequest request = exchange.getIn().getBody(AbstractLightblueDataRequest.class);
        if (request == null) {
            throw new Exception("request cannot be null");
        }
        exchange.getOut().setBody(endpoint.getLightblueClient().data(request));
    }

}
