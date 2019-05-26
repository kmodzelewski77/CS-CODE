package com.test.kmodzelewski.globalevent;


import com.test.kmodzelewski.data.EventEntity;
import com.test.kmodzelewski.data.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventPersistenceService implements CompletedEventPersistence {

    private final EventRepository eventRepository;
    private int durationTimeAlert = 4;
    @Autowired
    public EventPersistenceService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void saveEventData( GlobalEventData globalEventData )
    {
        completedEventPersistanceLogger.debug("Save new global event Data ");
        eventRepository.save(
           new EventEntity()
                   .setDuration(globalEventData.duration)
                .setEventId(globalEventData.eventId)
                .setHost(globalEventData.host)
                .setType(globalEventData.type)
                .setAlert( globalEventData.duration > getDurationTimeAlert()  )
        );
    }

    int getDurationTimeAlert() {
        return durationTimeAlert;
    }

    public EventPersistenceService setDurationTimeAlert(int durationTimeAlert) {
        this.durationTimeAlert = durationTimeAlert;
        return this;
    }
}
