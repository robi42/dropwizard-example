package com.example.helloworld.util;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;

public class SecurityUtilities {

    private static final String USER = "user";
    private static final String ADMIN = "admin";


    private SecurityUtilities() {
    }


    public static SecurityHandler createAdminBasicAuthHandler(final String username, final String password) {
        final HashLoginService loginService = new HashLoginService();
        loginService.putUser(username, Credential.getCredential(password), new String[]{USER});
        loginService.setName(ADMIN);

        final Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{USER});
        constraint.setAuthenticate(true);

        final ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.setAuthenticator(new BasicAuthenticator());
        securityHandler.setRealmName(ADMIN);
        securityHandler.addConstraintMapping(constraintMapping);
        securityHandler.setLoginService(loginService);

        return securityHandler;
    }
}
