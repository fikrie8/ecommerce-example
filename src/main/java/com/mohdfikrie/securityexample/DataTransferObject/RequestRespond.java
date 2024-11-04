package com.mohdfikrie.securityexample.DataTransferObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mohdfikrie.securityexample.Model.Users;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRespond {

    private int statusCode;
    private String username;
    private String password;
    private String name;
    private String email;
    private String address;
    private String role;
    private String refreshToken;
    private String ExpirationTime;
    private String token;
    private String message;
    private String error;
    private Users users;
    private List<Users> usersList;
}
