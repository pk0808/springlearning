package com.example.profile.controller;


import com.example.profile.entity.Profile;
import com.example.profile.service.ProfileService;
import com.example.profile.util.ProfileConstants;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/api/v1/profiles")
public class ProfileController{
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Profile getUserById(@RequestParam String userName)  {
        return profileService.getProfile(userName);
    }

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Profile createUserProfile(@RequestBody Profile profile) {
        if(profile.getUserName() == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ProfileConstants.user_name_required);
        return profileService.createUserProfile(profile);
    }

    @PutMapping("/profile/{userName}")
    @ResponseBody
    public Profile updateUserProfile(@PathVariable("userName") String userName, @RequestBody Profile profile) {
        if(profile.getUserName() == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ProfileConstants.user_name_required);
        return profileService.updateProfile(profile);
    }

    @DeleteMapping("/profile/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserProfile(@PathVariable("userName") String userName) {
        profileService.deleteProfile(userName);
    }
}