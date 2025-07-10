package com.example.backendgroupgenerateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.PersonList;
import com.example.backendgroupgenerateur.repository.PersonListRepository;

@Service
public class PersonListService {

    private final PersonListRepository personListRepository;

    @Autowired
    public PersonListService(PersonListRepository personListRepository) {
        this.personListRepository = personListRepository;
    }

    public PersonList create(PersonList personList) {
        return personListRepository.save(personList);
    }

    public Optional<PersonList> findById(Long id) {
        return personListRepository.findById(id);
    }

    public List<PersonList> findByUserId(Long userId) {
        return personListRepository.findByUserId(userId);
    }
}
