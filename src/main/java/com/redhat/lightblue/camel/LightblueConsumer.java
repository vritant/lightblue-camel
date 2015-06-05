package com.redhat.lightblue.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.projection.Projection;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public class LightblueConsumer extends DefaultConsumer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(LightblueConsumer.class);

    private final LightblueEndpoint endpoint;
    private String entityName;
    private String entityVersion;
    private Query query;
    private Projection projection;

    public LightblueConsumer(LightblueEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    public void run() {
        Exchange exchange = endpoint.createExchange();
        exchange.getIn().setBody(callLightblue(endpoint.getLightblueClient(), entityName, entityVersion, query, projection));
        try {
            // send message to next processor in the route
            getProcessor().process(exchange);
        } catch (Exception e) {
            //TODO rethrow?
            LOG.error("Unable to process exchange", e);
        } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }

    public static LightblueResponse callLightblue(LightblueClient client, String entityName, String entityVersion, Query query, Projection projection) {
        DataFindRequest findRequest = new DataFindRequest(entityName, entityVersion);
        findRequest.where(query);
        findRequest.select(projection);

        return client.data(findRequest);
    }

}
