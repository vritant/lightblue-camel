package com.redhat.lightblue.camel.request;

import com.redhat.lightblue.client.request.data.DataSaveRequest;

/**
 * Converts Objects into a lightblue save request.
 */
public class LightblueSaveRequestTransformer extends AbstractLightblueUpsertRequestTransformer<Object[]> {

    public LightblueSaveRequestTransformer(String entityName) {
        super(entityName);
    }

    public LightblueSaveRequestTransformer(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    public DataSaveRequest transform(Object[] body) {
        DataSaveRequest request = new DataSaveRequest(getEntityName(), getEntityVersion());
        request.create(body);
        request.returns(getProjections());

        return request;
    }

}
