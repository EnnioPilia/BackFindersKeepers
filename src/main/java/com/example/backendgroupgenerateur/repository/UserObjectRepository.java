package com.example.backendgroupgenerateur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backendgroupgenerateur.model.UserObject;

public interface UserObjectRepository extends JpaRepository<UserObject, Long> {
    List<UserObject> findByUserId(Long userId);
}
