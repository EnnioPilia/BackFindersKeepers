package com.example.backendgroupgenerateur.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.GroupDraws;
import com.example.backendgroupgenerateur.repository.GroupDrawsRepository;

@Service
public class GroupDrawsService {

    private final GroupDrawsRepository groupDrawsRepository;

    @Autowired
    public GroupDrawsService(GroupDrawsRepository groupDrawsRepository) {
        this.groupDrawsRepository = groupDrawsRepository;
    }

    public GroupDraws create(GroupDraws groupDraws) {
        return groupDrawsRepository.save(groupDraws);
    }

    public Optional<GroupDraws> findById(Long id) {
        return groupDrawsRepository.findById(id);
    }
}
