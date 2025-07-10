package com.example.backendgroupgenerateur.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.backendgroupgenerateur.model.PersonList;
import com.example.backendgroupgenerateur.repository.PersonListRepository;

@RestController
@RequestMapping("/personlists")
public class PersonListController {

    private final PersonListRepository personListRepository;

    public PersonListController(PersonListRepository personListRepository) {
        this.personListRepository = personListRepository;
    }

    @GetMapping
    public List<PersonList> getAllLists() {
        return personListRepository.findAll();
    }

    @GetMapping("/{id}")
    public PersonList getListById(@PathVariable Long id) {
        return personListRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Liste non trouvée avec id : " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonList createList(@RequestBody PersonList list) {
        return personListRepository.save(list);
    }

    @PutMapping("/{id}")
    public PersonList updateList(@PathVariable Long id, @RequestBody PersonList updatedList) {
        return personListRepository.findById(id).map(list -> {
            list.setName(updatedList.getName());
            // autres propriétés à mettre à jour si besoin
            return personListRepository.save(list);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Liste non trouvée avec id : " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteList(@PathVariable Long id) {
        if (!personListRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Liste non trouvée avec id : " + id);
        }
        personListRepository.deleteById(id);
    }
}
