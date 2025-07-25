package com.example.backendgroupgenerateur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendgroupgenerateur.dto.StatsDTO;
import com.example.backendgroupgenerateur.repository.UserObjectRepository;
import com.example.backendgroupgenerateur.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserObjectRepository userObjectRepository;

public StatsDTO getStats() {
    StatsDTO stats = new StatsDTO();
    stats.setUsersCount(userRepository.count());
    stats.setFoundObjectsCount(userObjectRepository.count());
    stats.setAdsCount(0);

    long activeUsersCount = userRepository.countActiveUsers();
    stats.setActiveUsersCount(activeUsersCount);

    return stats;
}

}
