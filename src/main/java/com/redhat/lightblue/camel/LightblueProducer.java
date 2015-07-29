package com.redhat.lightblue.camel;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import com.redhat.lightblue.client.request.AbstractLightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueErrorResponseException;
import com.redhat.lightblue.client.response.LightblueResponse;

/**
 * The Lightblue producer.
 */
public class LightblueProducer extends DefaultProducer {

    private final LightblueScheduledPollEndpoint endpoint;

    public LightblueProducer(LightblueScheduledPollEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("producerin  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + exchange.getIn());
        System.out.println("producerout  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + exchange.getOut());
        LightblueRequest req = (AbstractLightblueDataRequest) exchange.getIn().getBody();

        try {

            System.out.println("producer req>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + req.getBody());
            LightblueResponse response = endpoint.getLightblueClient().data(req);
            System.out.println("producer res>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response.getText());
            if (response.hasError()) {
                exchange.setException(new LightblueErrorResponseException("LightblueProducer:Error returned in response: "
                        + response.getText()));
                System.out.println("producer res has error >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response.getText());
            } else {
                System.out.println("producer res success >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response.getText());
                exchange.getOut().setBody(response);
            }
        } catch (Exception e) {
            /*
             * wrap all run time exceptions into LightblueErrorResponseException so routes are shielded from client errors and have to
             * handle only one type
             */
            exchange.setException(new LightblueErrorResponseException(e));
        }
    }

}
