package com.redhat.lightblue.camel;

import org.apache.camel.Consumer;

public interface LightblueConsumer extends Consumer, Runnable {

    String getEntityName();

    void setEntityName(String entityName);

    String getEntityVersion();

    void setEntityVersion(String entityVersion);

    String getOperation();

    void setOperation(String operation);

    String getBody();

    void setBody(String body);

}
