package com.redhat.lightblue.camel;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.lightblue.camel.model.Event;
import com.redhat.lightblue.camel.utils.JacksonXmlDataFormat;
import com.redhat.lightblue.camel.utils.LightblueErrorVerifier;
import com.redhat.lightblue.camel.utils.LightblueResponseTransformer;

public class TestOutboundRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("lightblue://outboundPollingTest")
            .bean(new LightblueErrorVerifier())
            .bean(new LightblueResponseTransformer<Event[]>(Event[].class))
            .marshal(new JacksonXmlDataFormat())
            .to("mock:result");
    }
}
