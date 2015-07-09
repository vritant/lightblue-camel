package com.redhat.lightblue.camel.request;

import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.expression.update.Update;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

/**
 * Converts {@link Update} instances into a lightblue update request.
 */
public class LightblueUpdateRequestTransformer extends AbstractLightblueUpsertRequestTransformer<Update[]> {

    private final Query query;

    public LightblueUpdateRequestTransformer(String entityName, Query query) {
        super(entityName);
        this.query = query;
    }

    public LightblueUpdateRequestTransformer(String entityName, String entityVersion, Query query) {
        super(entityName, entityVersion);
        this.query = query;
    }

    @Override
    public DataUpdateRequest transform(Update[] body) {
        DataUpdateRequest request = new DataUpdateRequest(getEntityName(), getEntityVersion());
        request.updates(body);
        request.where(query);
        request.returns(getProjections());

        return request;
    }

}
