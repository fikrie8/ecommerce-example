package com.mohdfikrie.securityexample.Service;

import com.mohdfikrie.securityexample.DataTransferObject.RequestRespond;
import com.mohdfikrie.securityexample.Model.UserPrincipal;
import com.mohdfikrie.securityexample.Model.Users;
import com.mohdfikrie.securityexample.Repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public Users register(Users users) {

        return userRepo.save(users);
    }

    public RequestRespond register(RequestRespond registrationRequest) {

        RequestRespond requestRespond = new RequestRespond();

        try{
            Users users = new Users();
            users.setUsername(registrationRequest.getUsername());
            users.setPassword(encoder.encode(registrationRequest.getPassword()));
            users.setName(registrationRequest.getName());
            users.setEmail(registrationRequest.getEmail());
            users.setAddress(registrationRequest.getAddress());
            users.setRole(registrationRequest.getRole());
            Users registeredUser = userRepo.save(users);

            if(registeredUser.getId() > 0) {
                requestRespond.setUsers(registeredUser);
                requestRespond.setMessage("User saved successfully");
                requestRespond.setStatusCode(200);
            }
        } catch (Exception e) {
            requestRespond.setStatusCode(500);
            requestRespond.setError(e.getMessage());
        }
        return requestRespond;
    }

    public String verify(Users users) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(users.getUsername());
        }
        return "Fail";
    }

    public RequestRespond login(RequestRespond loginRequest) {

        RequestRespond requestRespond = new RequestRespond();

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            var user = userRepo.findByUsername(loginRequest.getUsername());
            UserPrincipal userPrincipal = new UserPrincipal(user);
            var jwtToken = jwtService.generateToken(user.getUsername());
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), userPrincipal);
            requestRespond.setStatusCode(200);
            requestRespond.setToken(jwtToken);
            requestRespond.setRefreshToken(refreshToken);
            requestRespond.setExpirationTime("24Hrs");
            requestRespond.setMessage("Successfully Logged In");
        } catch (Exception e) {
            requestRespond.setStatusCode(500);
            requestRespond.setMessage(e.getMessage());
        }
        return requestRespond;
    }

    public List<Users> getListOfAllUsers() {

        List<Users> resultList = userRepo.findAll();
        return resultList;
    }

    public RequestRespond getAllUsers() {

        RequestRespond requestRespond = new RequestRespond();

        try {
            List<Users> resultList = userRepo.findAll();

            if(!resultList.isEmpty()) {
                requestRespond.setUsersList(resultList);
                requestRespond.setStatusCode(200);
                requestRespond.setMessage("Successfully get all user");
            } else {
                requestRespond.setStatusCode(404);
                requestRespond.setMessage("Users not found");
            }
        } catch (Exception e) {
            requestRespond.setStatusCode(500);
            requestRespond.setMessage("Error occurred when getting all users : " + e.getMessage());
        }
        return requestRespond;
    }

    public RequestRespond getUsersById(Integer requestUserId) {

        RequestRespond requestRespond = new RequestRespond();

        try {
            Users userById = userRepo.findById(requestUserId).orElseThrow(()-> new RuntimeException("User not found"));
            log.info("userById is : " + userById.getUsername() + "for id : " + requestUserId);
            requestRespond.setUsers(userById);
            requestRespond.setStatusCode(200);
            requestRespond.setMessage("User with id : " + requestUserId + " found successfully");
        } catch (Exception e) {
            requestRespond.setStatusCode(500);
            requestRespond.setMessage("Error occurred when getting user by id : " + e.getMessage());
        }
        return requestRespond;
    }

    public void adminAddUser(Users users) {

        List<Users> listOfRegisteredUser = getListOfAllUsers();

        if(!listOfRegisteredUser.contains(users)) {
            register(users);
            log.info("Success");
        } else {
            log.info("Failed. User already exist");
        }
    }

    public RequestRespond updateUser(Integer requestUserId, Users requestUpdateUser) {

        RequestRespond requestRespond = new RequestRespond();

        try {
            Optional<Users> usersOptional = userRepo.findById(requestUserId);

            if(usersOptional.isPresent()) {
                Users existingUser = usersOptional.get();

                if(requestUpdateUser.getUsername() != null && !requestUpdateUser.getUsername().isEmpty()) {
                    existingUser.setUsername(requestUpdateUser.getUsername());
                }

                if (requestUpdateUser.getName() != null && !requestUpdateUser.getName().isEmpty()) {
                    existingUser.setName(requestUpdateUser.getName());
                }

                if (requestUpdateUser.getEmail() != null && !requestUpdateUser.getEmail().isEmpty()) {
                    existingUser.setEmail(requestUpdateUser.getEmail());
                }

                if (requestUpdateUser.getAddress() != null && !requestUpdateUser.getAddress().isEmpty()) {
                    existingUser.setAddress(requestUpdateUser.getAddress());
                }

                if (requestUpdateUser.getRole() != null && !requestUpdateUser.getRole().isEmpty()) {
                    existingUser.setRole(requestUpdateUser.getRole());
                }

                if(requestUpdateUser.getPassword() != null && !requestUpdateUser.getPassword().isEmpty()) {
                    existingUser.setPassword(encoder.encode(requestUpdateUser.getPassword()));
                }
                Users savedUser = userRepo.save(existingUser);
                requestRespond.setUsers(savedUser);
                requestRespond.setStatusCode(200);
                requestRespond.setMessage("User updated successfully");
            } else {
                requestRespond.setStatusCode(404);
                requestRespond.setMessage("User not found for update");
            }
        } catch (Exception e) {
            requestRespond.setStatusCode(500);
            requestRespond.setMessage("Error occurred when updating user :" + e.getMessage());
        }
        return requestRespond;
    }

    public RequestRespond deleteUser(Integer requestUserId) {

        RequestRespond requestRespond = new RequestRespond();

        try {
            Optional<Users> usersOptional = userRepo.findById(requestUserId);

            if(usersOptional.isPresent()) {
                userRepo.deleteById(requestUserId);
                requestRespond.setStatusCode(200);
                requestRespond.setMessage("User successfully deleted");
            } else {
                requestRespond.setStatusCode(404);
                requestRespond.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            requestRespond.setStatusCode(500);
            requestRespond.setMessage("Error occurred while deleting user :" + e.getMessage());
        }
        return requestRespond;
    }
}
