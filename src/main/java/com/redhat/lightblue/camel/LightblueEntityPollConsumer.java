package com.redhat.lightblue.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.response.LightblueResponse;

/**
 * The Lightblue consumer.
 */
public class LightblueEntityPollConsumer extends ScheduledPollConsumer {
    private final LightblueEndpoint endpoint;
    private String entityName;
    private String entityVersion;
    private Query query;

    public void setQuery(String entityName, String entityVersion, Query query) {
        this.entityName = entityName;
        this.entityVersion = entityVersion;
        this.query = query;
    }

    public LightblueEntityPollConsumer(LightblueEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected int poll() throws Exception {
        Exchange exchange = endpoint.createExchange();

        // create a message body
        LightblueResponse response = LightblueConsumer.callLightblue(
                endpoint.getLightblueClient(), entityName, entityVersion, query, FieldProjection.includeFieldRecursively("*"));
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
