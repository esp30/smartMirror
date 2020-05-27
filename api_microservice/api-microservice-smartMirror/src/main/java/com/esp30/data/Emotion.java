package com.esp30.data;

public class Emotion {
    private long id;
    private long timestamp;
    private String value;
    private String user;

    public Emotion(){}

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
