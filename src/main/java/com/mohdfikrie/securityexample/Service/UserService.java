package com.mohdfikrie.securityexample.Service;

import com.mohdfikrie.securityexample.Model.Users;
import com.mohdfikrie.securityexample.Repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepo userRepo;

    @Autowired
    JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    public Users register(Users users) {
        return userRepo.save(users);
    }

    public String verify(Users users) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(users.getUsername());
        }
        return "Fail";
    }

    public List<Users> getAllUsers() {
        //RequestRespond requestRespond = new RequestRespond();

        List<Users> resultList = userRepo.findAll();
        return resultList;

        /*try {
            List<Users> resultList = userRepo.findAll();
            if(!resultList.isEmpty()) {
                return resultList;
                requestRespond.setOurUsersList(resultList);
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
        return requestRespond;*/
    }

    public void adminAddUser(Users users) {
        List<Users> listOfRegisteredUser = getAllUsers();
        if(!listOfRegisteredUser.contains(users)) {
            register(users);
            log.info("Success");
        } else {
            log.info("Failed. User already exist");
        }
    }
}
