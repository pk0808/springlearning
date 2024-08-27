package com.example.profile.service;

import com.example.profile.controller.ProfileController;
import com.example.profile.entity.Profile;
import com.example.profile.entity.ProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProfileConsumers {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final ProfileRepository profileRepository;

    public ProfileConsumers(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @KafkaListener(topics = "profile_update", groupId = "update")
    @Transactional
    public void readMsgForUpdateFromTopic(Profile message) {
        logger.info("Message read from topic : " + message);
        profileRepository.updateProfile(message.getUserName(), message.getAddress(), message.getPhoneNumber());
    }

    @KafkaListener(topics = "profile_delete", groupId = "delete")
    public void readMsgForDeleteFromTopic(Profile message) {
        logger.info("Message read from topic : " + message);
        profileRepository.deleteByUserName(message.getUserName());
    }
}
