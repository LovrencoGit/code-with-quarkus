package org.acme.provider.filter;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Provider
public class RestClientFilter implements ClientRequestFilter, ClientResponseFilter {
    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        final String method = requestContext.getMethod();
        final String path = uriInfo.getPath();
        final String querystring = uriInfo.getRequestUri().getQuery();
        // TODO possibilmente si potrebbero loggare body e header della richiesta

        log.info("----- START Calling client ----- {} {} {}", method, path, querystring);
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        final String method = requestContext.getMethod();
        final String path = uriInfo.getPath();
        final String querystring = uriInfo.getRequestUri().getQuery();
        // TODO possibilmente si potrebbero loggare body della risposta

        log.info("----- END Calling client ----- {} {} {}", method, path, querystring);
    }
}
