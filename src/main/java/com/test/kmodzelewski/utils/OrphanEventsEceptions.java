package com.test.kmodzelewski.utils;

import java.util.List;

public class OrphanEventsEceptions extends EventsException {
    public OrphanEventsEceptions(String message, List<String> rawEventData) {
        super(message, rawEventData);
    }

    public OrphanEventsEceptions( List<String> rawEventData) {
        super("Orphan events", rawEventData);
    }
}
