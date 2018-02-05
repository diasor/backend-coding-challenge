package com.engage.codetest.security;

import java.security.Principal;

public class BasicUser implements Principal {
    private String userName;

    BasicUser(String userName) {
        this.userName = userName;
        System.out.println(this.userName);
    }

    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getName() {
        return this.userName;
    }
}
