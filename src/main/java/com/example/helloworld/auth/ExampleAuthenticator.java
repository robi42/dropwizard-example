package com.example.helloworld.auth;

import com.codahale.dropwizard.auth.AuthenticationException;
import com.codahale.dropwizard.auth.Authenticator;
import com.codahale.dropwizard.auth.basic.BasicCredentials;
import com.example.helloworld.domain.User;
import com.google.common.base.Optional;

public class ExampleAuthenticator implements Authenticator<BasicCredentials, User> {

    private final String basicAuthUsername;
    private final String basicAuthPassword;


    public ExampleAuthenticator(final String basicAuthUsername, final String basicAuthPassword) {
        this.basicAuthUsername = basicAuthUsername;
        this.basicAuthPassword = basicAuthPassword;
    }


    @Override
    public Optional<User> authenticate(final BasicCredentials credentials) throws AuthenticationException {
        final boolean authorized = basicAuthUsername.equals(credentials.getUsername())
                && basicAuthPassword.equals(credentials.getPassword());

        if (authorized) {
            return Optional.of(new User(credentials.getUsername()));
        }
        return Optional.absent();
    }
}
