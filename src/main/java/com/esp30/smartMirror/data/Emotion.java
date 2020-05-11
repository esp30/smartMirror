package com.esp30.smartMirror.data;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class Emotion {
    private @Id @GeneratedValue Long id;
    private long timestamp;
    private String value;
    private String user;

    public Emotion(){}

    public Emotion(String user, String value) {
        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.user = user;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format(
                "Emotion - id: %d | time: %d | value: '%s' | user: '%s'",
                id, timestamp, value, user);
    }

    public Long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getValue() {
        return value;
    }

    public String getUser() {
        return user;
    }
}
