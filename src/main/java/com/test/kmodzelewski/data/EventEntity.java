package com.test.kmodzelewski.data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class EventEntity implements Serializable {
    @Id @GeneratedValue
    private long id;

    @Column( nullable =  false, unique = true)
    private String eventId;

    @Column (nullable = false)
    private long duration;

    @Column(nullable =  true)
    private String type ;

    @Column(nullable = true)
    private String host ;

    @Column
    private boolean alert = false;

    public long getId() {
        return id;
    }

    public EventEntity setId(long id) {
        this.id = id;
        return this;
    }

    public String getEventId() {
        return eventId;
    }

    public EventEntity setEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public EventEntity setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public String getType() {
        return type;
    }

    public EventEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getHost() {
        return host;
    }

    public EventEntity setHost(String host) {
        this.host = host;
        return this;
    }

    public boolean isAlert() {
        return alert;
    }

    public EventEntity setAlert(boolean alert) {
        this.alert = alert;
        return this;
    }
}
