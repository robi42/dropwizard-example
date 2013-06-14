package com.example.helloworld.resources;

import com.codahale.dropwizard.auth.Auth;
import com.example.helloworld.domain.User;
import com.google.inject.Inject;

import javax.inject.Singleton;
import javax.ws.rs.Path;

@Singleton
@Path("/api/v1")
public class V1ResourceContainer {

    private final HelloWorldResource helloWorldResource;
    private final PeopleResource peopleResource;
    private final PersonResource personResource;
    private final ProtectedResource protectedResource;
    private final ViewResource viewResource;


    @Inject
    public V1ResourceContainer(final HelloWorldResource helloWorldResource,
                               final PeopleResource peopleResource,
                               final PersonResource personResource,
                               final ProtectedResource protectedResource,
                               final ViewResource viewResource) {

        this.helloWorldResource = helloWorldResource;
        this.peopleResource = peopleResource;
        this.personResource = personResource;
        this.protectedResource = protectedResource;
        this.viewResource = viewResource;
    }


    @Path("/hello-world")
    public HelloWorldResource helloWorldResource() {
        return helloWorldResource;
    }

    @Path("/people")
    public PeopleResource peopleResource(final @Auth User user) {
        return peopleResource;
    }

    @Path("/people/{personId}")
    public PersonResource personResource() {
        return personResource;
    }

    @Path("/protected")
    public ProtectedResource protectedResource() {
        return protectedResource;
    }

    @Path("/views")
    public ViewResource viewResource() {
        return viewResource;
    }
}
