package com.engage.codetest.security;

import java.security.Principal;
/**
 * @author Diana Sormani
 * Created: February 01, 2018
 * Last Updated: February 05, 2018
 * Description: The BasicUser class implements Principal in order to be used in the UserAuthenticator class.
 *              It contains the userName property.
 */
public class BasicUser implements Principal {
    private String userName;

    BasicUser(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getName() {
        return this.userName;
    }
}
