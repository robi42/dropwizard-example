package com.example.helloworld.resources;

import com.codahale.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public interface ViewResource {

    @GET
    @Produces("text/html;charset=UTF-8")
    @Path("/utf8.ftl")
    View freemarkerUtf8();

    @GET
    @Produces("text/html;charset=ISO-8859-1")
    @Path("/iso88591.ftl")
    View freemarkerIso88591();

    @GET
    @Produces("text/html;charset=UTF-8")
    @Path("/utf8.mustache")
    View mustacheUtf8();

    @GET
    @Produces("text/html;charset=ISO-8859-1")
    @Path("/iso88591.mustache")
    View mustacheIso88591();
}
