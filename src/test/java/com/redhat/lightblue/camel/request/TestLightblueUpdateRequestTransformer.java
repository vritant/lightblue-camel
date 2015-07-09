package com.redhat.lightblue.camel.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.redhat.lightblue.client.expression.query.Query;
import com.redhat.lightblue.client.expression.query.ValueQuery;
import com.redhat.lightblue.client.expression.update.Update;
import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class TestLightblueUpdateRequestTransformer {

    @Test
    public void test() {
        Query query = ValueQuery.withValue("_id = 1");
        LightblueUpdateRequestTransformer transformer = new LightblueUpdateRequestTransformer("fake", "1.0.0", query);
        Update[] updates = new Update[]{new Update() {

            @Override
            public String toJson() {
                return "some value";
            }

        }};
        DataUpdateRequest request = transformer.transform(updates);

        assertNotNull(request);
        assertEquals("fake", request.getEntityName());
        assertEquals("1.0.0", request.getEntityVersion());
        assertEquals(1, transformer.getProjections().length);
        assertEquals(FieldProjection.includeFieldRecursively("*").toJson(), transformer.getProjections()[0].toJson());
    }

}
