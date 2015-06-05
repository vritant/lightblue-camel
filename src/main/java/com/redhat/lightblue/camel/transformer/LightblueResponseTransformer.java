package com.redhat.lightblue.camel.transformer;

import org.apache.camel.Handler;

import com.redhat.lightblue.client.response.LightblueResponse;

public class LightblueResponseTransformer<T> {

    private final Class<T> type;

    public LightblueResponseTransformer(Class<T> type) {
        this.type = type;
    }

    @Handler
    public T transform(LightblueResponse body) throws Exception {
        return body.parseProcessed(type);
    }

}
