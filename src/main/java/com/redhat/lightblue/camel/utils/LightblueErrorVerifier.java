package com.redhat.lightblue.camel.utils;

import org.apache.camel.Handler;

import com.redhat.lightblue.client.response.LightblueErrorResponseException;
import com.redhat.lightblue.client.response.LightblueResponse;

/**
 * Verifies that a {@link LightblueResponse} does not contain any errors.
 *
 * @author dcrissman
 */
public class LightblueErrorVerifier {

    @Handler
    public void verify(final LightblueResponse body) {
        if (body.hasError()) {
            throw new LightblueErrorResponseException("Error returned in response: " + body.getText());
        }
    }

}
