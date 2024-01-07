package org.acme.clients;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.acme.clients.models.BreedsResponse;
import org.acme.clients.models.CatFact;
import org.acme.clients.models.FactListResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

@RegisterRestClient(configKey = "cat-facts")
public interface CatFactsClient {
    @GET
    @Path("/breeds")
    BreedsResponse getListOfBreeds(@RestQuery int limit);

    @GET
    @Path("/fact")
    CatFact getSingleFact(@QueryParam("max_length") int maxLength);

    @GET
    @Path("/facts")
    FactListResponse getListOfFacts(@RestQuery int limit, @QueryParam("max_length") int maxLength);
}
