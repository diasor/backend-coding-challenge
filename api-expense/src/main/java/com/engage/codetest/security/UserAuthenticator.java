package com.engage.codetest.security;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class UserAuthenticator implements Authenticator<BasicCredentials, BasicUser> {
    @Override
    public Optional<BasicUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if ("secret".equals(credentials.getPassword())) {
            BasicUser buser = new BasicUser(credentials.getUsername());
            return Optional.of(buser);
        }
        // todo log failed auth attempt
        return Optional.empty();
    }

}
