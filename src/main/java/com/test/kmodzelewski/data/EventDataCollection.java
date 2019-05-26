package com.test.kmodzelewski.data;

import com.test.kmodzelewski.globalevent.JsonEventEntry;

import java.util.ArrayList;
import java.util.List;

public class EventDataCollection  {
    private final List<JsonEventEntry> map = new ArrayList<>();
    private final long creationTimeStamp = System.currentTimeMillis();

    public EventDataCollection(JsonEventEntry firstEvent)  {
        map.add( firstEvent );
    }

    public List<JsonEventEntry> addEventDataEntry (JsonEventEntry nextEvent)
    {
        map.add( nextEvent );
        return map;
    }

    public List<JsonEventEntry> getMap() {
        return map;
    }

    public long getCreationTimeStamp() {
        return creationTimeStamp;
    }
}
