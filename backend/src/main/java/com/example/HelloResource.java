package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {

    @Inject
    HelloService helloService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@QueryParam("name") String name) {
        return helloService.getGreeting(name);
    }
}
