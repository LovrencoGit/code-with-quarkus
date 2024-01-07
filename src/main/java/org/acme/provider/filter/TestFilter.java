package org.acme.provider.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestFilter {
//    @ServerRequestFilter(preMatching = true)
    public void preMatchingFilter(ContainerRequestContext requestContext) {
        log.info("TEST pre matching filter: {} - {}", requestContext.getRequest().getMethod(), requestContext.getUriInfo().getPath());
        // make sure we don't lose cheese lovers
//        if("yes".equals(requestContext.getHeaderString("Cheese"))) {
//            requestContext.setRequestUri(URI.create("/cheese"));
//        }
    }

//    @ServerRequestFilter
    public void defaultFilter(ContainerRequestContext requestContext) {
        log.info("TEST default filter: {} - {}", requestContext.getRequest().getMethod(), requestContext.getUriInfo().getPath());
    }

//    @ServerRequestFilter
//    public Optional<RestResponse<Void>> factFilter(ContainerRequestContext ctx) {
//        // only allow GET methods for now
//        if(ctx.getUriInfo().getPath().contains("/fact")) {
//            return Optional.of(RestResponse.status(Response.Status.METHOD_NOT_ALLOWED));
//
//        return Optional.empty();
//    }

//    @ServerResponseFilter
    public void defaultResponseFilter(ContainerResponseContext responseContext) {
        log.info("Response entity {}", responseContext.getEntity());
    }
}
