package com.redhat.lightblue.camel;

public interface LightblueConsumerWithSetters extends LightblueConsumer {

    void setEntityName(String entityName);

    void setEntityVersion(String entityVersion);

    void setOperation(String operation);

    void setBody(String body);

}
