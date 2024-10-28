package com.mohdfikrie.securityexample.Controller;

import com.mohdfikrie.securityexample.Model.Users;
import com.mohdfikrie.securityexample.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public Users register(@RequestBody Users users) {
        users.setPassword(encoder.encode(users.getPassword()));
        return userService.register(users);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users users)  {
        return userService.verify(users);
    }

    @GetMapping("/admin/get-all-users")
    public List<Users> getStudents() {
        return userService.getAllUsers();
    }

    //If I try Post with Postman without writing CSRF config. I will get error 401 unauthorized.
    // This is because, by default spring security will not allow Post,Put, Delete if there's no CSRF token
    @PostMapping("/admin/add-user")
    public Users addUsers(@RequestBody Users users) {
        users.setPassword(encoder.encode(users.getPassword()));
        userService.adminAddUser(users);
        return users;
    }

    @GetMapping("get-csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        //Since it's expecting a return of Csrf, so I need to cast it with type csrf first.
        return (CsrfToken) request.getAttribute("_csrf");
    }

}
