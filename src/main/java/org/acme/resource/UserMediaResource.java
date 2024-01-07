package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.acme.response.ResponseBase;
import org.acme.service.UserMediaService;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

@Path("/media/user")
@AllArgsConstructor
public class UserMediaResource {
    final UserMediaService userMediaService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/{userId}/images/profile")
    public Response uploadProfileImage(@PathParam("userId") String userId, MultipartFormDataInput inputFile) {
        if (inputFile.getValues().get("inputFile").size() > 1) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseBase(Response.Status.BAD_REQUEST.getStatusCode(), "The input must contain only one file"))
                    .build();
        }
        userMediaService.uploadProfileImage(userId, inputFile);
        return Response.ok().build();
    }

    @GET
    @Produces({"image/png", "image/jpeg"})
    @Path("/{userId}/images/profile")
    public Response downloadProfileImage(@PathParam("userId") String userId) {
        var file = userMediaService.downloadProfileImage(userId);
        return Response.ok(file)
                .header("Content-Disposition", "attachment;filename=" + file.getFileName().toString())
                .build();
    }

}
