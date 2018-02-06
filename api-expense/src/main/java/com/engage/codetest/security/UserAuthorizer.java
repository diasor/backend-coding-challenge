package com.engage.codetest.security;

import io.dropwizard.auth.Authorizer;

/**
 * @author Diana Sormani
 * Created: February 01, 2018
 * Last Updated: February 05, 2018
 * Description: The UserAuthorizer class implements Authorizer in order to be set in the application
 *              environment as the application authorizer.
 *              It overrides the "authorize" method but since authorization is beyond the scope
 *              of this challenge, it always returns true.
 */
public class UserAuthorizer implements Authorizer<BasicUser> {
    @Override
    public boolean authorize(BasicUser user, String role) {
        return true;
    }
}
