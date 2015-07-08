package com.redhat.lightblue.camel;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.guice.CamelModule;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;
import com.redhat.lightblue.client.LightblueClient;
import com.redhat.lightblue.client.request.LightblueRequest;

public class TestCamelModule extends CamelModule {

    private final LightblueClient client;
    private final Map<String, LightblueRequest> requests;

    public TestCamelModule(LightblueClient client) {
        this.client = client;
        this.requests = null;
    }

    public TestCamelModule(LightblueClient client, Map<String, LightblueRequest> requests) {
        super();
        this.client = client;
        this.requests = requests;
    }

    @Override
    protected void configure() {
        super.configure();

        bind(LightblueClient.class).toInstance(client);
        if (requests != null) {
            MapBinder<String, LightblueRequest> mapbinder = MapBinder.newMapBinder(binder(), String.class, LightblueRequest.class);
            for (Map.Entry<String, LightblueRequest> request : requests.entrySet()) {
                mapbinder.addBinding(request.getKey()).toInstance(request.getValue());
            }
        }
    }

    @Provides
    Set<RoutesBuilder> routes(Injector injector) {
        Set<RoutesBuilder> set = new HashSet<RoutesBuilder>();
        set.add(new InboundTestRoute());
        set.add(new OutboundTestRoute());
        return set;
    }

}
