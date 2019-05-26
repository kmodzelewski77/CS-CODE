package com.test.kmodzelewski.utils;


import java.util.List;

public class EventsException extends RuntimeException {
    private final List<String> rawEventData ;

    public EventsException(String message, List<String> rawEventData) {
        super(message);
        this.rawEventData = rawEventData;

    }

    public List<String> getRawEventData() {
        return rawEventData;
    }
}
