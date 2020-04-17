package com.esp30.smartMirror.data;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Data
@Entity
public class Emotion {
    private @Id @GeneratedValue Long id;
    private long timestamp;
    private @ManyToOne User user;
    private String description;

    public Emotion(){}

    public Emotion(User user, String descritpion)
    {
        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.user = user;
        this.description = descritpion;
    }
}
