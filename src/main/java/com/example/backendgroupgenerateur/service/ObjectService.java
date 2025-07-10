package com.example.backendgroupgenerateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.Object;
import com.example.backendgroupgenerateur.repository.ObjectRepository;

@Service
public class ObjectService {

    private final ObjectRepository ObjectRepository;

    @Autowired
    public ObjectService(ObjectRepository ObjectRepository) {
        this.ObjectRepository = ObjectRepository;
    }

    public Object create(Object Object) {
        return ObjectRepository.save(Object);
    }

    public Optional<Object> findById(Long id) {
        return ObjectRepository.findById(id);
    }

    public List<Object> findByUserId(Long userId) {
        return ObjectRepository.findByUserId(userId);
    }
}
