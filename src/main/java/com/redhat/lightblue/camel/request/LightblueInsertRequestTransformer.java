package com.redhat.lightblue.camel.request;

import com.redhat.lightblue.client.request.data.DataInsertRequest;

/**
 * Converts Objects into a lightblue insert request.
 */
public class LightblueInsertRequestTransformer extends AbstractLightblueUpsertRequestTransformer<Object[]> {

    public LightblueInsertRequestTransformer(String entityName) {
        super(entityName);
    }

    public LightblueInsertRequestTransformer(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public DataInsertRequest transform(Object[] body) {
        DataInsertRequest request = new DataInsertRequest(getEntityName(), getEntityVersion());
        request.create(body);
        request.returns(getProjections());

        return request;
    }

}
