package com.test.kmodzelewski.utils;

import java.util.List;

public class EventsProcessingException extends EventsException
{
    public EventsProcessingException(String message, List<String> rawEventData) {
        super(message, rawEventData);
    }

}
