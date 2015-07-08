package com.redhat.lightblue.camel;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.lightblue.camel.model.Event;
import com.redhat.lightblue.camel.model.User;
import com.redhat.lightblue.camel.utils.JacksonXmlDataFormat;
import com.redhat.lightblue.camel.utils.LightblueErrorVerifier;
import com.redhat.lightblue.camel.utils.LightblueResponseTransformer;

public class TestOutboundRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("lightblue://eventPoller?delay=100000")
            .bean(new LightblueErrorVerifier())
            .bean(new LightblueResponseTransformer<Event[]>(Event[].class))
            .marshal(new JacksonXmlDataFormat())
            .to("mock:eventResult");

        from("lightblue://userPoller?initialDelay=5&delay=10&timeUnit=SECONDS")
            .bean(new LightblueResponseTransformer<User[]>(User[].class))
            .marshal(new JacksonXmlDataFormat())
            .to("mock:userResult");
    }
}
