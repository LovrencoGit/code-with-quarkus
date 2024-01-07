package org.acme.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.service.GreetingService;

@Path("/greeting")
public class GreetingResource {

    //@Inject
    final GreetingService greetingService;

    public GreetingResource(GreetingService greetingService) {
        this.greetingService = greetingService;
    }



    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello !!!!!";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/name/{name}")
    public String helloName(String name){
        return greetingService.greetingByName(name);
    }
}
