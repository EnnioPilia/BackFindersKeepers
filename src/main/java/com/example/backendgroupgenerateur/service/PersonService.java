package com.example.backendgroupgenerateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.model.Person;
import com.example.backendgroupgenerateur.repository.PersonRepository;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person create(Person person) {
        return personRepository.save(person);
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }

    public List<Person> findByPersonListId(Long personListId) {
        return personRepository.findByPersonListId(personListId);
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}
