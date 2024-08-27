package com.example.auth.util;

public class AuthUserResponse {
	private String userName;
    private String jwtToken;

    public AuthUserResponse() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public AuthUserResponse(String userName, String jwtToken) {
        this.userName = userName;
        this.jwtToken = jwtToken;
    }
}
