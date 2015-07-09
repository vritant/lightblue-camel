package com.redhat.lightblue.camel.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.redhat.lightblue.client.projection.FieldProjection;
import com.redhat.lightblue.client.request.data.DataSaveRequest;

public class TestLightblueSaveRequestTransformer {

    @Test
    public void test() {
        LightblueSaveRequestTransformer transformer = new LightblueSaveRequestTransformer("fake", "1.0.0");
        DataSaveRequest request = transformer.transform(new Object[]{"hi", "there"});

        assertNotNull(request);
        assertEquals("fake", request.getEntityName());
        assertEquals("1.0.0", request.getEntityVersion());
        assertEquals(1, transformer.getProjections().length);
        assertEquals(FieldProjection.includeFieldRecursively("*").toJson(), transformer.getProjections()[0].toJson());
    }

}
