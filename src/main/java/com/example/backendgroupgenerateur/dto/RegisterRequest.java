package com.example.backendgroupgenerateur.dto;

public class RegisterRequest {
    private String name;      // pour nom complet, ou nom + prénom en 1 champ
    private String email;
    private String passWord;  // correspond à passWord côté Angular (sensible à la casse)
    private Integer age;
    private String role;

    // getters/setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassWord() { return passWord; }
    public void setPassWord(String passWord) { this.passWord = passWord; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
