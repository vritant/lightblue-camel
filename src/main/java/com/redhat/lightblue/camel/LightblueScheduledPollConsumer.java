package com.redhat.lightblue.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

import com.redhat.lightblue.client.response.LightblueResponse;

/**
 * Lightblue polling consumer.
 */
public class LightblueScheduledPollConsumer extends ScheduledPollConsumer {

    private final LightblueEndpoint endpoint;

    public LightblueScheduledPollConsumer(LightblueEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected int poll() throws Exception {
        Exchange exchange = endpoint.createExchange();

        // create a message body
        LightblueResponse response = endpoint.getLightblueClient().data(endpoint.getLightbluePollingRequest());
        exchange.getIn().setBody(response);

        try {
            // send message to next processor in the route
            getProcessor().process(exchange);
            return response.parseMatchCount(); // number of messages polled
        } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }

}
