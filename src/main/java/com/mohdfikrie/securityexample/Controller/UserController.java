package com.mohdfikrie.securityexample.Controller;

import com.mohdfikrie.securityexample.DataTransferObject.RequestRespond;
import com.mohdfikrie.securityexample.Model.Users;
import com.mohdfikrie.securityexample.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public ResponseEntity<RequestRespond> register(@RequestBody RequestRespond requestRespondRegister) {

        return ResponseEntity.ok(userService.register(requestRespondRegister));
    }

    @PostMapping("/login")
    public ResponseEntity<RequestRespond> login(@RequestBody RequestRespond requestRespondLogin) {

        return ResponseEntity.ok(userService.login(requestRespondLogin));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<RequestRespond> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    //If I try Post with Postman without writing CSRF config. I will get error 401 unauthorized.
    // This is because, by default spring security will not allow Post,Put, Delete if there's no CSRF token
    @PostMapping("/admin/add-user")
    public Users addUsers(@RequestBody Users users) {

        users.setPassword(encoder.encode(users.getPassword()));
        userService.adminAddUser(users);
        return users;
    }

    @GetMapping("/admin/get-user/{requestUserId}")
    public ResponseEntity<RequestRespond> getUserById(@PathVariable(required = true) Integer requestUserId) {

        return ResponseEntity.ok(userService.getUsersById(requestUserId));
    }

    @PutMapping("/admin/update/{requestUserId}")
    public ResponseEntity<RequestRespond> updateUser(@PathVariable(required = true) Integer requestUserId, @RequestBody Users requestUpdateUser) {

        return ResponseEntity.ok(userService.updateUser(requestUserId, requestUpdateUser));
    }

    @DeleteMapping("/admin/delete/{requestUserId}")
    public ResponseEntity<RequestRespond> deleteUser(@PathVariable(required = true) Integer requestUserId) {

        return ResponseEntity.ok(userService.deleteUser(requestUserId));
    }

    @GetMapping("get-csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {

        //Since it's expecting a return of Csrf, so I need to cast it with type csrf first.
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
