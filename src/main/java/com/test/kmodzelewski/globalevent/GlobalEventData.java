package com.test.kmodzelewski.globalevent;

import java.io.Serializable;
import java.util.Objects;

public class GlobalEventData implements Serializable {
    public final String eventId;
    public final String type;
    public final String host;
    public final long duration;

    public GlobalEventData(String eventId, String type, String host, long duration) {
        this.eventId = eventId;
        this.type = type;
        this.host = host;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalEventData that = (GlobalEventData) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    public String getEventId() {
        return eventId;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public long getDuration() {
        return duration;
    }
}
