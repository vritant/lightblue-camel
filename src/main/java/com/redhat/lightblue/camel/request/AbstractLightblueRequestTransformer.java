package com.redhat.lightblue.camel.request;

import org.apache.camel.Handler;

import com.redhat.lightblue.client.request.LightblueRequest;

/**
 * Transforms the body of a camel route to a {@link LightblueRequest}.
 *
 * @author dcrissman
 *
 * @param <Body> - the type of body this transformer supports.
 */
public abstract class AbstractLightblueRequestTransformer<Body> {

    private final String entityName;
    private final String entityVersion;

    public String getEntityName() {
        return entityName;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public AbstractLightblueRequestTransformer(String entityName) {
        this(entityName, null);
    }

    public AbstractLightblueRequestTransformer(String entityName, String entityVersion) {
        if (entityName == null) {
            throw new IllegalArgumentException("entityName cannot be null");
        }

        this.entityName = entityName;
        this.entityVersion = entityVersion;
    }

    /**
     * Transforms the body of the camel route to a {@link LightblueRequest}.
     * @param body - body to be converted
     * @return {@link LightblueRequest}
     */
    @Handler
    public abstract LightblueRequest transform(Body body);

}
