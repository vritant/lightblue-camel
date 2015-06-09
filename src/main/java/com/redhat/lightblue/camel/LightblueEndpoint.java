package com.redhat.lightblue.camel;

import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.commons.lang.StringUtils;

import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.request.data.DataSaveRequest;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

/**
 * Represents a Lightblue endpoint.
 */
@UriEndpoint(scheme = "lightblue", title = "Lightblue", syntax = "lightblue:name", consumerClass = LightblueEntityPollingConsumer.class, label = "Lightblue")
public class LightblueEndpoint extends DefaultEndpoint {

    @UriParam
    @Metadata(required = "true")
    private String operation;
    @UriParam
    @Metadata(required = "true")
    private String entityName;
    @UriParam
    @Metadata(required = "true")
    private String entityVersion;
    @UriParam
    @Metadata(required = "true")
    private String request;

    @UriParam(defaultValue = "false")
    private boolean pollMode;

    private LightblueClient lightblueClient;

    public static String buildUriParameters(AbstractLightblueDataRequest request, boolean isPollModeEnabled) {
        StringBuffer builder = new StringBuffer("?");

        String operation;
        if (request instanceof DataInsertRequest) {
            operation = "insert";
        }
        else if (request instanceof DataFindRequest) {
            operation = "find";
        }
        else if (request instanceof DataUpdateRequest) {
            operation = "update";
        }
        else if (request instanceof DataSaveRequest) {
            operation = "save";
        }
        else if (request instanceof DataDeleteRequest) {
            operation = "delete";
        }
        else {
            throw new UnsupportedOperationException("Request type not supported: " + request.getClass());
        }

        builder.append("operation=").append(operation).append("&");
        builder.append("entityName=").append(request.getEntityName()).append("&");
        if (!StringUtils.isEmpty(request.getEntityVersion())) {
            builder.append("entityVersion=").append(request.getEntityVersion()).append("&");
        }
        if (isPollModeEnabled) {
            builder.append("pollMode=").append("true").append("&");
        }
        builder.append("request=").append(request.getBody());

        return builder.toString();
    }

    public LightblueEndpoint() {}

    public LightblueEndpoint(String uri, LightblueComponent component) {
        super(uri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new LightblueProducer(this);
    }

    @Override
    public LightblueConsumer createConsumer(Processor processor) throws Exception {
        LightblueConsumer consumer;
        if (isPollModeEnabled()) {
            consumer = new LightblueEntityPollingConsumer(this, processor);
        }
        else {
            consumer = new LightblueEntityConsumer(this, processor);
        }

        consumer.setEntityName(getEntityName());
        consumer.setEntityVersion(getEntityVersion());
        consumer.setOperation(getOperation());
        consumer.setBody(getRequest());

        return consumer;
    }

    @Override
    public boolean isSingleton() {
        return true;
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

    @ManagedAttribute(description = "Operation to be performed: [insert, update, save, find, delete]")
    public String getOperation() {
        return operation;
    }

    @ManagedAttribute(description = "Operation to be performed: [insert, update, save, find, delete]")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @ManagedAttribute(description = "Name of lightblue entity")
    public String getEntityName() {
        return entityName;
    }

    @ManagedAttribute(description = "Name of lightblue entity")
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @ManagedAttribute(description = "Version of lightblue entity")
    public String getEntityVersion() {
        return entityVersion;
    }

    @ManagedAttribute(description = "Version of lightblue entity")
    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    @ManagedAttribute(description = "Json body of request")
    public String getRequest() {
        return request;
    }

    @ManagedAttribute(description = "Json body of request")
    public void setRequest(String request) {
        this.request = request;
    }

    @ManagedAttribute(description = "Poll Mode: if enabled consumer will poll for data, otherwise single execution")
    public boolean isPollModeEnabled() {
        return pollMode;
    }

    @ManagedAttribute(description = "Poll Mode: if enabled consumer will poll for data, otherwise single execution")
    public void setPollMode(boolean pollMode) {
        this.pollMode = pollMode;
    }

}
