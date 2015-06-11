package com.redhat.lightblue.camel;

import org.apache.camel.Consumer;

public interface LightblueConsumer extends Consumer {

    String getEntityName();

    String getEntityVersion();

    String getOperation();

    String getBody();

}
