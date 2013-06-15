package com.example.helloworld.resources;

import com.codahale.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/views")
public interface ViewResource {

    @GET
    @Path("/utf8.ftl")
    @Produces("text/html;charset=UTF-8")
    View freemarkerUtf8();

    @GET
    @Path("/iso88591.ftl")
    @Produces("text/html;charset=ISO-8859-1")
    View freemarkerIso88591();

    @GET
    @Path("/utf8.mustache")
    @Produces("text/html;charset=UTF-8")
    View mustacheUtf8();

    @GET
    @Path("/iso88591.mustache")
    @Produces("text/html;charset=ISO-8859-1")
    View mustacheIso88591();
}
