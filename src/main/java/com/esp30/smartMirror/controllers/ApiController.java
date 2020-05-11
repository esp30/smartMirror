package com.esp30.smartMirror.controllers;

import com.esp30.smartMirror.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ApiController {

    @Autowired
    private EmotionRepository emotionRepository;

    @GetMapping("/users")
    public ArrayList<User> users() {
        // Fetch users from somewhere

        //Temporary
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Mariana", 42));
        users.add(new User("Tiago", 33));
        users.add(new User("Raquel", 40));
        return users;
    }

    @GetMapping("/user")
    public User user(@RequestParam(value = "id") int id) {
        // Fetch user by id from somewhere

        // Temporary
        User to_return = new User("Joao", 37);
        return to_return;
    }

    @GetMapping("/emotions")
    public ArrayList<Emotion> emotions() {
        ArrayList<Emotion> emotions = new ArrayList<>();
        emotionRepository.findAll().forEach(emotions::add);

        return emotions;
    }
}
