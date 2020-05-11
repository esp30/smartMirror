package com.esp30.smartMirror.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface EmotionRepository extends CrudRepository<Emotion, Long>
{
    List<Emotion> findByUser(String user);
    Emotion findById(long id);
}

