package com.test.kmodzelewski.globalevent;

import com.test.kmodzelewski.data.EventData;
import com.test.kmodzelewski.data.EventDataCollection;
import com.test.kmodzelewski.service.EventCollector;
import com.test.kmodzelewski.utils.EventPersistenceException;
import com.test.kmodzelewski.utils.EventsProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CompletedEventCollector implements EventCollector {
    private final static String EVENT_LABEL_START_ENTRY="STARTED";
    private final static String EVENT_LABEL_END_ENTRY="FINISHED";

    private final CompletedEventPersistence completedEventPersistence;
    private long   cacheLifeLimitMiliseconds = 30000;

    private static final ConcurrentHashMap<String , EventDataCollection > eventDataStoreCache = new ConcurrentHashMap<>(10000);


    @Autowired
    public CompletedEventCollector(CompletedEventPersistence completedEventPersistence) {
        this.completedEventPersistence = completedEventPersistence;
    }

    @Async("collectTaskExecutor")
    public void collectEventData(JsonEventEntry eventDataEntry)
    {
        eventCollectorLogger.debug("Add new event data entry with id: {}", eventDataEntry.getEventIdentifier() );
        if ( ! eventDataStoreCache.containsKey(eventDataEntry.getEventIdentifier()) ) {
            eventDataStoreCache.put(eventDataEntry.getEventIdentifier(), new EventDataCollection(eventDataEntry));
            eventCollectorLogger.debug("Leave collection as it was first event occurrence");
            return;
        }
        eventCollectorLogger.debug("Collector cache contains event entry already, add another.");
        eventDataStoreCache.get(eventDataEntry.getEventIdentifier()).addEventDataEntry( eventDataEntry );
        EventDataCollection collectedData=eventDataStoreCache.get(eventDataEntry.getEventIdentifier());
        eventDataStoreCache.remove(eventDataEntry.getEventIdentifier());
        saveEventEntry( collectedData,eventDataEntry.getEventIdentifier() );
    }

    private void saveEventEntry(EventDataCollection collectedData, String idReferernece  )
    {
        eventCollectorLogger.trace("Prepare and save event: {}",idReferernece);
        if (collectedData.getMap().size() != 2 )
        {
            throw new EventsProcessingException("There should be only two event items", null );
        }
        JsonEventEntry startEntry ;
        JsonEventEntry endEntry;

        if (EVENT_LABEL_START_ENTRY.equals(collectedData.getMap().get(0).getJsonEvent().getState())) {
            startEntry = collectedData.getMap().get(0);
            endEntry = collectedData.getMap().get(1);
        } else
        {
            startEntry = collectedData.getMap().get(1);
            endEntry = collectedData.getMap().get(0);
        }

        if (  ! EVENT_LABEL_END_ENTRY.equals(endEntry.getJsonEvent().getState()) )
        {
            throw new EventsProcessingException("One Entry Should be: "+EVENT_LABEL_START_ENTRY+", Other: "+EVENT_LABEL_END_ENTRY ,
                    collectedData.getMap().stream().map( EventData::getRawContent ).collect(Collectors.toList()));
        }

        if ( endEntry.getJsonEvent().getTimestamp() - startEntry.getJsonEvent().getTimestamp() < 0 )
        {
            throw new EventsProcessingException("Calculated Event duration is less than 0" ,
                    collectedData.getMap().stream().map( EventData::getRawContent ).collect(Collectors.toList()));
        }

        if ( startEntry.getJsonEvent().getType() != null &&  ! startEntry.getJsonEvent().getType().equals( endEntry.getJsonEvent().getType()))
        {
            throw new EventsProcessingException("Different Types for the same event" ,
                    collectedData.getMap().stream().map( EventData::getRawContent ).collect(Collectors.toList()));
        }

        try {
            eventCollectorLogger.debug("Send event data for persistence");
            completedEventPersistence.saveEventData(new GlobalEventData(
                    startEntry.getJsonEvent().getId()
                    , startEntry.getJsonEvent().getType()
                    , startEntry.getJsonEvent().getHost()
                    , endEntry.getJsonEvent().getTimestamp() - startEntry.getJsonEvent().getTimestamp()
            ));
        } catch ( Exception e)
        {
            eventCollectorLogger.error("Cloud not save event data due to error: "+e.getMessage(), e);
            throw new EventPersistenceException("Unsucessful Event Save",
                    collectedData.getMap().stream().map( EventData::getRawContent ).collect(Collectors.toList())
            );
        }
    }

    public int getCacheLifeLimitSeconds() {
        return (int) (cacheLifeLimitMiliseconds/1000);
    }

    public CompletedEventCollector setCacheLifeLimitSeconds(int cacheLifeLimitSeconds) {
        this.cacheLifeLimitMiliseconds = 1000L* cacheLifeLimitSeconds;
        return this;
    }

    @Override
    @Scheduled(fixedRate = 10000)
    public void cleanOrphanEvents() {
        eventCollectorLogger.info("Clean Orphan events for {} ", getClass());
        synchronized ( eventDataStoreCache )
        {
            long currentTimeStamp = System.currentTimeMillis();
            long currentCacheSize = eventDataStoreCache.size();
            eventDataStoreCache.forEach( (key, entry)->{
                if ( currentTimeStamp- entry.getCreationTimeStamp() > cacheLifeLimitMiliseconds  )
                {
                    eventCollectorLogger.debug("Remove orphan entry: {}",key);
                    eventDataStoreCache.remove( key);
                }
            });
            eventCollectorLogger.info("Event cache reduced by {} entries",(eventDataStoreCache.size() - currentCacheSize) );
        }
        eventCollectorLogger.info("Clean Orphan events for {} completed", getClass());
    }
}
