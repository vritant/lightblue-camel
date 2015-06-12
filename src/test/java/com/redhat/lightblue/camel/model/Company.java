package com.redhat.lightblue.camel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Company {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
