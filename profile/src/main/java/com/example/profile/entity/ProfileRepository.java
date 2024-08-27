package com.example.profile.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Optional<Profile> findByuserName(String userName);
    @Query("userName: ?0")
    @Update("{$set: {address: ?1, phoneNumber: ?2}}")
    void updateProfile(String userName, String address, String phoneNumber);;

    void deleteByUserName(String userName);

}