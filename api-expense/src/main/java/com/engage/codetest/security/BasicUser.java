package com.engage.codetest.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class BasicUser implements Principal {
    private String userName;
    private List<String> userCurrRoles = new ArrayList<>();


    public BasicUser(String userName){

        this.userName = userName;
        System.out.println(this.userName);
    }

    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getName(){
        return this.userName;
    }

    public boolean userHasRole(String roleToCheck) {
        return this.userCurrRoles.contains(roleToCheck);
    }
}
