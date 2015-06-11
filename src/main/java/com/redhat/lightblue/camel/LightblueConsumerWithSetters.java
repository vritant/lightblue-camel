package com.redhat.lightblue.camel;

/**
 * Allows the {@link LightblueEndpoint} to be able to set values on the {@link LightblueConsumer}
 * implementations, while hiding the setters to the outside world. <br/>
 * <br/>
 * <b>Note:</b> Not visible outside of package
 *
 * @author dcrissman
 */
interface LightblueConsumerWithSetters extends LightblueConsumer {

    void setEntityName(String entityName);

    void setEntityVersion(String entityVersion);

    void setOperation(String operation);

    void setBody(String body);

}
