package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.acme.clients.CatFactsClient;
import org.acme.clients.models.BreedsResponse;
import org.acme.clients.models.CatFact;
import org.acme.clients.models.FactListResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class CatFactsService {
    @RestClient
    CatFactsClient catFactsClient;

    public BreedsResponse getListOfBreeds(int limit) {
        return catFactsClient.getListOfBreeds(limit);
    }

    public CatFact getSingleFact(int maxLength) {
        return catFactsClient.getSingleFact(maxLength);
    }

    public FactListResponse getFactList(int limit, int maxLength) {
        return catFactsClient.getListOfFacts(limit, maxLength);
    }
}
