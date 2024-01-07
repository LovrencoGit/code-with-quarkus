package org.acme.provider.filter;

import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class LogApiFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;


    @Override
    public void filter(ContainerRequestContext context) {
        final String method = context.getMethod();
        final String path = info.getPath();
        final String querystring = info.getRequestUri().getQuery();
        final String address = request.remoteAddress().toString();

        log.info("----- START ----- {} {} {} | IP: {}", method, path, querystring, address);
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        final String method = containerRequestContext.getMethod();
        final String path = info.getPath();
        final String querystring = info.getRequestUri().getQuery();
        final String address = request.remoteAddress().toString();

        log.info("----- END ----- {} {} {} | IP: {}", method, path, querystring, address);
    }
}
