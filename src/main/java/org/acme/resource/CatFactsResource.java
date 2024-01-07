package org.acme.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.acme.clients.models.BreedsResponse;
import org.acme.clients.models.CatFact;
import org.acme.clients.models.FactListResponse;
import org.acme.service.CatFactsService;

@Path("/catFacts")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class CatFactsResource {
    private final CatFactsService catFactsService;

    @GET
    @Path("/breeds")
    public Response getBreeds(@QueryParam("limit") int limit) {
        BreedsResponse breeds = catFactsService.getListOfBreeds(limit);
        return Response.ok(breeds).build();
    }

    @GET
    @Path("/fact")
    public Response getSingleFact(@QueryParam("max_length") int maxLength) {
        CatFact fact = catFactsService.getSingleFact(maxLength);
        return Response
                .status(Response.Status.OK)
                .entity(fact)
                .build();
    }

    @GET
    @Path("/facts")
    public Response getFactList(@QueryParam("limit") int limit, @QueryParam("max_length") int maxLength) {
        FactListResponse facts = catFactsService.getFactList(limit, maxLength);
        return Response
                .status(Response.Status.OK)
                .entity(facts)
                .build();
    }
}
