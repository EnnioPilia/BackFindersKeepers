package com.example.backendgroupgenerateur.service;

import java.util.List;
import java.util.Optional;

import com.example.backendgroupgenerateur.model.UserObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.repository.UserObjectRepository;

@Service
public class UserObjectService {

    private final UserObjectRepository UserObjectRepository;

    @Autowired
    public UserObjectService(UserObjectRepository UserObjectRepository) {
        this.UserObjectRepository = UserObjectRepository;
    }

    public UserObject create(UserObject UserObject) {
        return UserObjectRepository.save(UserObject);
    }

    public Optional<UserObject> findById(Long id) {
        return UserObjectRepository.findById(id);
    }

    public List<UserObject> findByUserId(Long userId) {
        return UserObjectRepository.findByUserId(userId);
    }
}
