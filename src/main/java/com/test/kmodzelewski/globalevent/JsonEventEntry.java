package com.test.kmodzelewski.globalevent;

import com.test.kmodzelewski.data.EventData;

public class JsonEventEntry extends EventData {
    private final JsonMicroEvent jsonEvent;

    public JsonEventEntry(String rawContent, JsonMicroEvent jsonEvent) {
        super(rawContent);
        this.jsonEvent = jsonEvent;
    }

    public JsonMicroEvent getJsonEvent() {
        return jsonEvent;
    }

    @Override
    public String getEventIdentifier() {
        return jsonEvent.getId();
    }
}
