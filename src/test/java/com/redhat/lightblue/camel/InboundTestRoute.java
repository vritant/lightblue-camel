package com.redhat.lightblue.camel;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.lightblue.camel.model.User;
import com.redhat.lightblue.camel.utils.JacksonXmlDataFormat;
import com.redhat.lightblue.camel.utils.LightblueErrorVerifier;
import com.redhat.lightblue.camel.utils.LightblueInsertRequestTransformer;

public class InboundTestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
            .unmarshal(new JacksonXmlDataFormat(User[].class)).convertBodyTo(User[].class)
            .bean(new LightblueInsertRequestTransformer("user", "1.0.0"))
            .to("lightblue://inboundTest")
            .bean(new LightblueErrorVerifier())
            .to("mock:result");
    }

}
