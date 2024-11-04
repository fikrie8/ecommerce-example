package com.mohdfikrie.securityexample.Repository;

import com.mohdfikrie.securityexample.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users,Integer> {

    Users findByUsername(String username);
    Optional<Users> findByEmail(String email);
}
