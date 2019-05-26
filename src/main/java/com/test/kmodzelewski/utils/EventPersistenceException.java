package com.test.kmodzelewski.utils;

import java.util.List;

public class EventPersistenceException extends EventsException
{
    public EventPersistenceException(String message, List<String> rawEventData) {
        super(message, rawEventData);
    }

}
