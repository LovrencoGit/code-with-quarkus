package org.acme.resource;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.acme.dto.DTOUser;
import org.acme.dto.DTOUserPathPartial;
import org.acme.entity.User;
import org.acme.mapper.UserMapper;
import org.acme.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class UserResource {

    //@Inject
    final UserService userService;
    final UserMapper userMapper;

    @GET
    public Response findAll() {
        List<User> users = userService.findAll();
        List<DTOUser> dtoUsers = userMapper.toDTOUserList(users);
        return Response.ok(dtoUsers).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") long id) {
        User user = userService.findById(id);
        DTOUser dtoUser = userMapper.toDTOUser(user);
        return Response.ok(dtoUser).build();
    }

    @POST
    @Transactional
    public Response create(@Valid DTOUser user) {
        User userSaved = userMapper.toUser(user);
        userSaved = userService.create(userSaved);
        DTOUser dtoUserSaved = userMapper.toDTOUser(userSaved);
        return Response.status(Response.Status.CREATED).entity(dtoUserSaved).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response replace(@PathParam("id") long id, @Valid DTOUser user) {
        User userSaved = userMapper.toUser(user);
        userSaved = userService.replace(id, userSaved);
        DTOUser dtoUserSaved = userMapper.toDTOUser(userSaved);
        return Response.ok(dtoUserSaved).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") long id, @Valid DTOUserPathPartial dtoUserPartial) {
        userService.update(id, dtoUserPartial);
        return Response.ok(dtoUserPartial).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") long id) {
        User userDeleted = userService.deleteById(id);
        DTOUser dtoUserDeleted = userMapper.toDTOUser(userDeleted);
        return Response.ok(dtoUserDeleted).build();
    }

    /**************************************************************/

    @POST
    @Path("/list")
    @Transactional
    public Response createList(@Valid List<DTOUser> users) {
        List<User> usersSaved = new ArrayList<>();
        users.forEach(dto -> {
            User u = userMapper.toUser(dto);
            u = userService.create(u);
            usersSaved.add(u);
        });
        List<DTOUser> dtoUsersSaved = userMapper.toDTOUserList(usersSaved);
        return Response.status(Response.Status.CREATED).entity(dtoUsersSaved).build();
    }

    @GET
    @Path("/search")
    public Response searchBy(
            @QueryParam("name") @DefaultValue("") String name,
            @QueryParam("email") @DefaultValue("") String email
    ) {
        List<User> users = userService.searchBy(name, email);
        List<DTOUser> dtoUsers = userMapper.toDTOUserList(users);
        return Response.ok().entity(dtoUsers).build();
    }

}
