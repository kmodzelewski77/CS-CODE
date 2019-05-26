package com.test.kmodzelewski.globalevent;


import java.io.Serializable;

public class JsonMicroEvent implements Serializable {

    private final String id;
    private final String type;
    private final String host;
    private final long timestamp;
    private final String state;

    public JsonMicroEvent(String id, String type, String host, long timestamp, String state) {
        this.id = id;
        this.type = type;
        this.host = host;
        this.timestamp = timestamp;
        this.state = state;
    }

    public String getState() {
        return state;
    }


    public String getId() {
        return id;
    }


    public String getType() {
        return type;
    }


    public String getHost() {
        return host;
    }


    public long getTimestamp() {
        return timestamp;
    }


}
