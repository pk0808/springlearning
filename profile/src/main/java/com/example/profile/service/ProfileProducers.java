package com.example.profile.service;

import com.example.profile.entity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileProducers {
    @Autowired
    KafkaTemplate<String, Profile> kafkaTemplate;

    public void sendMessageToTopic(String topicName, Profile profile) {
        kafkaTemplate.send(topicName, profile);
    }
}
