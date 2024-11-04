package com.mohdfikrie.securityexample.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class Users{

    @Id
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String address;
    private String role;

    @Override
    public String toString() {

        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
