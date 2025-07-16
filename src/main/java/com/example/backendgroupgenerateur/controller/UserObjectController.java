package com.example.backendgroupgenerateur.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.model.UserObject;
import com.example.backendgroupgenerateur.repository.UserObjectRepository;
import com.example.backendgroupgenerateur.service.ConversationAccessService;

@RestController
@RequestMapping("/objects")
public class UserObjectController {

    private final UserObjectRepository userObjectRepository;
    private final ConversationAccessService accessService;

    public UserObjectController(UserObjectRepository userObjectRepository, ConversationAccessService accessService) {
        this.userObjectRepository = userObjectRepository;
        this.accessService = accessService;
    }

    @GetMapping
    public List<UserObject> getAllObject() {
        return userObjectRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserObject createObject(@RequestBody UserObject userObject, Principal principal) {
        User currentUser = accessService.getCurrentUser(principal);
        userObject.setOwner(currentUser); // Définit l'utilisateur courant comme propriétaire
        return userObjectRepository.save(userObject);
    }

    @PutMapping("/{id}")
    public UserObject updateObject(@PathVariable Long id, @RequestBody UserObject updatedObject, Principal principal) {
        User currentUser = accessService.getCurrentUser(principal);

        return userObjectRepository.findById(id).map(object -> {
            if (!object.getOwner().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas autorisé à modifier cet objet.");
            }

            object.setName(updatedObject.getName());
            // autres propriétés à mettre à jour si besoin

            return userObjectRepository.save(object);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objet non trouvé avec id : " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteObject(@PathVariable Long id, Principal principal) {
        User currentUser = accessService.getCurrentUser(principal);

        UserObject object = userObjectRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objet non trouvé avec id : " + id));

        if (!object.getOwner().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas autorisé à supprimer cet objet.");
        }

        userObjectRepository.deleteById(id);
    }
}