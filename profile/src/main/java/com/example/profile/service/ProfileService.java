package com.example.profile.service;

import com.example.profile.controller.ProfileController;
import com.example.profile.entity.Profile;
import com.example.profile.entity.ProfileRepository;
import com.example.profile.util.ProfileConstants;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final ProfileRepository profileRepository;

    @Autowired
    ProfileProducers profileProducers;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile getProfile(String userName) {
        Optional<Profile> profileExists = profileRepository.findByuserName(userName);
        if (profileExists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ProfileConstants.profileNotFoundErrMsg);
        }
        return profileExists.get();
    }

    @Transactional
    public Profile createUserProfile(Profile profile) {
        Optional<Profile> profileExists = profileRepository.findByuserName(profile.getUserName());
        if (profileExists.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ProfileConstants.profileAlreadyExistsErrMsg);
        }
        logger.info("Creating new profile for user : " + profile.getUserName());
        profileRepository.save(profile);
        return profile;
    }

    @Transactional
    public Profile updateProfile(Profile profile) {
        Profile profileExists = getProfile(profile.getUserName());
        logger.info("Updating profile for user : " + profile.getUserName());
        if (profile.getAddress() != null) {
            profileExists.setAddress(profile.getAddress());
        }
        if (profile.getPhoneNumber() != null) {
            profileExists.setPhoneNumber(profile.getPhoneNumber());
        }
        profileProducers.sendMessageToTopic("profile_update", profile);
        return profile;
    }

    @Transactional
    public void deleteProfile(String userName) {
        Profile profileExists = getProfile(userName);
        logger.info("Deleting profile for user : " + userName);
        profileProducers.sendMessageToTopic("profile_delete", profileExists);
    }


}
