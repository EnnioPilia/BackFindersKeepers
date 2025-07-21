package com.example.backendgroupgenerateur.controller;

import java.security.Principal;
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

    @GetMapping("/me")
    public List<UserObject> getMyObjects(Principal principal) {
        User currentUser = accessService.getCurrentUser(principal);
        return userObjectRepository.findByOwnerId(currentUser.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserObject createObject(@RequestBody UserObject userObject, Principal principal) {
        User currentUser = accessService.getCurrentUser(principal);
        userObject.setOwner(currentUser);  // <-- correction ici
        return userObjectRepository.save(userObject);
    }

    @PutMapping("/{id}")
    public UserObject updateObject(@PathVariable Long id, @RequestBody UserObject updatedObject, Principal principal) {
        User currentUser = accessService.getCurrentUser(principal);

        return userObjectRepository.findById(id).map(object -> {
            if (!object.getOwner().getId().equals(currentUser.getId())) {  // <-- correction ici
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Vous n'êtes pas autorisé à modifier cet objet.");
            }

            // Mise à jour des champs modifiables
            if (updatedObject.getName() != null) {
                object.setName(updatedObject.getName());
            }
            if (updatedObject.getDescription() != null) {
                object.setDescription(updatedObject.getDescription());
            }
            if (updatedObject.getType() != null) {
                object.setType(updatedObject.getType());
            }
            if (updatedObject.getDate() != null) {
                object.setDate(updatedObject.getDate());
            }
            if (updatedObject.getLocalisation() != null) {
                object.setLocalisation(updatedObject.getLocalisation());
            }
            if (updatedObject.getPhotoPath() != null) {
                object.setPhotoPath(updatedObject.getPhotoPath());
            }
            // Important : tu peux mettre à jour le champ reclame ici
            object.setReclame(updatedObject.isReclame());

            // NE PAS modifier object.setUser() ici !

            return userObjectRepository.save(object);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objet non trouvé avec id : " + id));
    }

    @GetMapping("/{id}")
    public UserObject getObjectById(@PathVariable Long id) {
        return userObjectRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objet non trouvé avec id : " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteObject(@PathVariable Long id, Principal principal) {
        User currentUser = accessService.getCurrentUser(principal);

        UserObject object = userObjectRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objet non trouvé avec id : " + id));

        if (!object.getOwner().getId().equals(currentUser.getId())) {  // <-- correction ici
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'êtes pas autorisé à supprimer cet objet.");
        }

        userObjectRepository.deleteById(id);
    }
}
