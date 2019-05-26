package com.test.kmodzelewski.data;

import java.io.Serializable;

public abstract class EventData implements Serializable {

    private final String rawContent;

    public EventData(String rawContent) {
        this.rawContent = rawContent;
    }

    public abstract String getEventIdentifier();

    public String getRawContent() {
        return rawContent;
    }
}
