package com.example.backendgroupgenerateur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backendgroupgenerateur.model.PersonList;

public interface PersonListRepository extends JpaRepository<PersonList, Long> {
    List<PersonList> findByUserId(Long userId);
}
