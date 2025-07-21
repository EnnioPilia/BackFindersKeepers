package com.example.backendgroupgenerateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.UserObject;
import com.example.backendgroupgenerateur.repository.UserObjectRepository;

@Service
public class UserObjectService {

    private final UserObjectRepository userObjectRepository;

    @Autowired
    public UserObjectService(UserObjectRepository userObjectRepository) {
        this.userObjectRepository = userObjectRepository;
    }

    public UserObject create(UserObject userObject) {
        return userObjectRepository.save(userObject);
    }

    public Optional<UserObject> findById(Long id) {
        return userObjectRepository.findById(id);
    }

    public List<UserObject> findByUserId(Long userId) {
        return userObjectRepository.findByOwnerId(userId);
    }
}

