package com.example.backendgroupgenerateur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backendgroupgenerateur.model.Object;

public interface ObjectRepository extends JpaRepository<Object, Long> {
    List<Object> findByUserId(Long userId);
}
