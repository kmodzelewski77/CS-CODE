package com.test.kmodzelewski.globalevent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface CompletedEventPersistence {
    Logger completedEventPersistanceLogger = LoggerFactory.getLogger(CompletedEventPersistence.class);
    void saveEventData( GlobalEventData globalEventData );
}
