package com.redhat.lightblue.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.request.data.DataSaveRequest;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;
import com.redhat.lightblue.client.request.data.LiteralDataRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

public class LightblueEntityConsumer extends DefaultConsumer implements Runnable, LightblueConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(LightblueEntityConsumer.class);

    private final LightblueEndpoint endpoint;
    private String entityName;
    private String entityVersion;
    private String operation;
    private String body;

    public LightblueEntityConsumer(LightblueEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    public void run() {
        Exchange exchange = endpoint.createExchange();
        exchange.getIn().setBody(callLightblue(endpoint.getLightblueClient(), getEntityName(), getEntityVersion(), getOperation(), getBody()));
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

    public static LightblueResponse callLightblue(LightblueClient client, String entityName, String entityVersion, String operation, String json) {
        AbstractLightblueDataRequest dataRequest;
        //TODO Not ideal, find better way.
        switch (operation) {
            case "insert":
                dataRequest = new DataInsertRequest();
                break;
            case "update":
                dataRequest = new DataUpdateRequest();
                break;
            case "save":
                dataRequest = new DataSaveRequest();
                break;
            case "find":
                dataRequest = new DataFindRequest();
                break;
            case "delete":
                dataRequest = new DataDeleteRequest();
                break;
            default:
                throw new RuntimeException("Unsupported operation: " + operation);
        }

        LiteralDataRequest request = new LiteralDataRequest(
                entityName,
                ((StringUtils.isEmpty(entityVersion)) ? null : entityVersion),
                json,
                dataRequest.getHttpMethod(),
                dataRequest.getOperationPathParam());

        return client.data(request);
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public String getEntityVersion() {
        return entityVersion;
    }

    @Override
    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    @Override
    public String getOperation() {
        return operation;
    }

    @Override
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

}
