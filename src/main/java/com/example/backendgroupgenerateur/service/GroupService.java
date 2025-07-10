package com.example.backendgroupgenerateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.Group;
import com.example.backendgroupgenerateur.repository.GroupRepository;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group create(Group group) {
        return groupRepository.save(group);
    }

    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

public List<Group> findByDrawsId(Long drawsId) {
    return groupRepository.findByGroupDrawsId(drawsId);
}

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }

public List<Group> findAll() {
    return groupRepository.findAll();
}

public boolean deleteById(Long id) {
    if (groupRepository.existsById(id)) {
        groupRepository.deleteById(id);
        return true;
    }
    return false;
}

}
