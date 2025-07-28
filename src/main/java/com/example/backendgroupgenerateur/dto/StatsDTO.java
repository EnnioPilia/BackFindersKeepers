package com.example.backendgroupgenerateur.dto;

public class StatsDTO {
    private long usersCount;
    private long adsCount;
    private long foundObjectsCount;

    // getters & setters
    public long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(long usersCount) {
        this.usersCount = usersCount;
    }

    public long getAdsCount() {
        return adsCount;
    }

    public void setAdsCount(long adsCount) {
        this.adsCount = adsCount;
    }

    public long getFoundObjectsCount() {
        return foundObjectsCount;
    }

    public void setFoundObjectsCount(long foundObjectsCount) {
        this.foundObjectsCount = foundObjectsCount;
    }
    private long activeUsersCount;

public long getActiveUsersCount() {
    return activeUsersCount;
}

public void setActiveUsersCount(long activeUsersCount) {
    this.activeUsersCount = activeUsersCount;
}

}
