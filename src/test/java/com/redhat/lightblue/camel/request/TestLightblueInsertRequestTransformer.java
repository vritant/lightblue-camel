package com.redhat.lightblue.camel.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.request.data.DataInsertRequest;

public class TestLightblueInsertRequestTransformer {

    @Test
    public void test() {
        LightblueInsertRequestTransformer transformer = new LightblueInsertRequestTransformer("fake", "1.0.0");
        DataInsertRequest request = transformer.transform(new Object[]{"hi", "there"});

        assertNotNull(request);
        assertEquals("fake", request.getEntityName());
        assertEquals("1.0.0", request.getEntityVersion());
        assertEquals(1, transformer.getProjections().length);
        assertEquals(FieldProjection.includeFieldRecursively("*").toJson(), transformer.getProjections()[0].toJson());
    }

}
