package com.engage.codetest.security;

import io.dropwizard.auth.Authorizer;

public class UserAuthorizer implements Authorizer<BasicUser> {
    @Override
    public boolean authorize(BasicUser user, String role) {
        // return user.getUserName().equals("good-guy") && role.equals("ADMIN");
        return true;
    }
}
