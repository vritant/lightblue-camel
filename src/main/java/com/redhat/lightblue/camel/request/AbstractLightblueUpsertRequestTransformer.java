package com.redhat.lightblue.camel.request;

import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.projection.Projection;

public abstract class AbstractLightblueUpsertRequestTransformer<Body> extends AbstractLightblueRequestTransformer<Body> {

    private Projection[] projections;

    /**
     * Will return the set projection. If not set, then will return the default projection
     * that will return all fields recursively.
     * @return projections
     */
    public Projection[] getProjections() {
        if (projections == null) {
            return new Projection[]{FieldProjection.includeFieldRecursively("*")};
        }
        return projections;
    }

    public void setProjections(Projection... projections) {
        this.projections = projections;
    }

    public AbstractLightblueUpsertRequestTransformer(String entityName) {
        super(entityName);
    }

    public AbstractLightblueUpsertRequestTransformer(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

}
