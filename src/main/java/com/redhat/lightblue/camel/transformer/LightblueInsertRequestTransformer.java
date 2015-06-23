package com.redhat.lightblue.camel.transformer;

import org.apache.camel.Handler;

import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.request.data.DataInsertRequest;

/**
 * Converts POJOs into a lightblue insert request which creates them.
 *
 */
public class LightblueInsertRequestTransformer {

    private final String entityName;
    private final String entityVersion;

    public LightblueInsertRequestTransformer(String entityName) {
        this(entityName, null);
    }

    public LightblueInsertRequestTransformer(String entityName, String entityVersion) {
        if (entityName == null) {
            throw new IllegalArgumentException("entityName cannot be null");
        }

        this.entityName = entityName;
        this.entityVersion = entityVersion;
    }

    @Handler
    public DataInsertRequest transform(Object[] body) {
        DataInsertRequest insertRequest = new DataInsertRequest(entityName, entityVersion);
        insertRequest.create(body);
        insertRequest.returns(FieldProjection.includeFieldRecursively("*")); //TODO doing now for simplicity, revisit.

        return insertRequest;
    }

}
