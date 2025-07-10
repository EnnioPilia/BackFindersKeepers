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

import com.example.backendgroupgenerateur.model.Object;
import com.example.backendgroupgenerateur.repository.ObjectRepository;

@RestController
@RequestMapping("/objects")
public class ObjectController {

    private final ObjectRepository ObjectRepository;

    public ObjectController(ObjectRepository ObjectRepository) {
        this.ObjectRepository = ObjectRepository;
    }

    @GetMapping
    public List<Object> getAllObject() {
        return ObjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Object getObjectById(@PathVariable Long id) {
        return ObjectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Object non trouvée avec id : " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object createObject(@RequestBody Object Object) {
        return ObjectRepository.save(Object);
    }

    @PutMapping("/{id}")
    public Object updateObject(@PathVariable Long id, @RequestBody Object updatedList) {
        return ObjectRepository.findById(id).map(Object -> {
            Object.setName(updatedList.getName());
            // autres propriétés à mettre à jour si besoin
            return ObjectRepository.save(Object);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Object non trouvée avec id : " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteObject(@PathVariable Long id) {
        if (!ObjectRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object non trouvée avec id : " + id);
        }
        ObjectRepository.deleteById(id);
    }
}
