package com.engage.codetest.security;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

/**
 * @author Diana Sormani
 * Created: February 01, 2018
 * Last Updated: February 05, 2018
 * Description: The UserAuthenticator class implements Authenticator in order to be set in the application
 *              environment as the application authenticator.
 *              It overrides the authenticate method in order to specified how the authentication is
 *              defined for this application.
 */
public class UserAuthenticator implements Authenticator<BasicCredentials, BasicUser> {
    /**
     * This methods indicates how the application will authenticate users for access.
     * Since the user login and control is out of scope for this challenge, the authenticator
     * has an over simplify criterion: it allows access to any user who enters a password "secret" .
     */
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
