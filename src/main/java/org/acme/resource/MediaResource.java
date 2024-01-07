package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.acme.response.FileListResponse;
import org.acme.service.MediaService;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

@Path("/file")
@AllArgsConstructor
public class MediaResource {

    final MediaService mediaService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(MultipartFormDataInput input) {
        mediaService.uploadFiles(input);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{file}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("file") String fileName) {
        var file = mediaService.downloadFile(fileName);
        return Response.ok(file)
                .header("Content-Disposition", "attachment;filename=" + file.getFileName().toString())
                .build();
    }

    @DELETE
    @Path("/{file}")
    public Response delete(@PathParam("file") String fileName) {
        mediaService.deleteFile(fileName);
        return Response.noContent().build();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listFiles() {
        FileListResponse fileList = mediaService.listFiles();
        return Response.ok(fileList).build();
    }
}
