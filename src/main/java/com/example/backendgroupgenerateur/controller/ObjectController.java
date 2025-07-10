package com.example.backendgroupgenerateur.controller;

import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.backendgroupgenerateur.model.Object;
import com.example.backendgroupgenerateur.model.User;
import com.example.backendgroupgenerateur.repository.ObjectRepository;

@RestController
@RequestMapping("/objects")
public class ObjectController {

    private final ObjectRepository ObjectRepository;

    public ObjectController(ObjectRepository ObjectRepository) {
        this.ObjectRepository = ObjectRepository;
    }

    @Operation(summary = "Récupérer tous les objets", description = "Retourne une liste de tous les objets disponibles.")
    @GetMapping
    public List<Object> getAllObject() {
        return ObjectRepository.findAll();
    }

    @Operation(summary = "Récupérer un objet par ID", description = "Retourne un objet spécifique basé sur son ID.")
    @GetMapping("/{id}")
    public Object getObjectById(@PathVariable Long id) {
        return ObjectRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Object non trouvée avec id : " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object createObject(@RequestBody Object object, @AuthenticationPrincipal User user) {
        object.setUser(user);
        return ObjectRepository.save(object);
    }

    @Operation(summary = "Mettre à jour un objet existant", description = "Met à jour les informations d'un objet spécifié par son ID.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Représentation de l'objet à mettre à jour", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"name\": \"Nouveau nom de l'objet\"}"))))
    @PutMapping("/{id}")
    public Object updateObject(@PathVariable Long id, @RequestBody Object updatedList) {
        return ObjectRepository.findById(id).map(Object -> {
            Object.setName(updatedList.getName());
            // autres propriétés à mettre à jour si besoin
            return ObjectRepository.save(Object);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Object non trouvée avec id : " + id));
    }

    @Operation(summary = "Supprimer un objet par ID", description = "Supprime un objet spécifique basé sur son ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteObject(@PathVariable Long id) {
        if (!ObjectRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object non trouvée avec id : " + id);
        }
        ObjectRepository.deleteById(id);
    }
}
