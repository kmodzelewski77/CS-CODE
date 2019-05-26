package com.test.kmodzelewski.service;

import com.test.kmodzelewski.globalevent.JsonEventEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface EventCollector {
    Logger eventCollectorLogger = LoggerFactory.getLogger( EventCollector.class);
    void collectEventData(JsonEventEntry eventDataEntry);
    void cleanOrphanEvents();
}
