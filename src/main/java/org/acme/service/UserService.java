package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.acme.FailureException;
import org.acme.dto.DTOUserPathPartial;
import org.acme.entity.User;
import org.acme.enumeration.ResponseEnum;
import org.acme.mapper.UserMapper;
import org.acme.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
public class UserService {

    //@Inject
    final UserMapper userMapper;
    final UserRepository userRepository;



    public List<User> findAll() {
        return userRepository.listAll();
    }
    public User findById( long id ) {
        return this.findByIdIfPresent(id)
                .orElseThrow(() -> new FailureException(Response.Status.NOT_FOUND, ResponseEnum.NOT_FOUND, "NOT_FOUND - No User found for id: '"+id+"'"));
    }
    public Optional<User> findByIdIfPresent( long id ) {
        return userRepository.findByIdOptional(id);
    }

    public User create( User user ){
        // ... input validation ...
        Validate.notNull(user, "User can not be null");
        Validate.isTrue(user.getId()==null, "'id' must be null");
        Validate.notBlank(user.getName(), "'name' can not be empty");
        Validate.notBlank(user.getEmail(), "'email'' can not be empty");

        // ... business logic (data normalization logic) ...
        String email = StringUtils.lowerCase( user.getEmail() );
        user.setEmail( email );
        String name = StringUtils.capitalize( user.getName() );
        user.setName( name );

        userRepository.persist( user );
        return user;
    }

    public User replace(long id, User user) {
        Validate.notNull(user, "User can not be null");
        Validate.notNull(user.getId(), "user.id can not be null");
        Validate.isTrue(id == user.getId(), "user.id and param 'id' must be the same");

        User userSaved = this.findById(id);
        userMapper.copyUser(user, userSaved);

        userRepository.persist(userSaved);
        return user;
    }

    public User update(long id, DTOUserPathPartial userPartial) {
        Validate.notNull(userPartial, "DTOUserPathPartial can not be null");
        Validate.notNull(userPartial.getId(), "userPartial.id can not be null");
        Validate.isTrue(id == userPartial.getId(), "userPartial.id and param 'id' must be the same");

        User userSaved = this.findById(id);
        userMapper.updateUserFromDTOUserPatchPartial(userPartial, userSaved);

        userRepository.persist(userSaved);
        return userSaved;
    }

    public User deleteById(long id) {
        User userDeleted = this.findById(id);
        userRepository.deleteById(id);
        return userDeleted;
    }

    /*******************************/

    public List<User> searchBy(String name, String email){
        if( StringUtils.isNotBlank(name) && StringUtils.isNotBlank(email) ){
            return this.findByNameAndEmail(name, email);
        } else if( StringUtils.isNotBlank(name) ){
            return this.findByName(name);
        } else if( StringUtils.isNotBlank(email) ){
            return this.findByEmail(email);
        }
        return this.findAll();
        //throw new FailureException(Response.Status.INTERNAL_SERVER_ERROR, ResponseEnum.INTERNAL_SERVER_ERROR, "UNEXPECTED ERROR - Unexpected case, missing logic")
    }
    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }
    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public List<User> findByNameAndEmail(String name, String email) {
        return userRepository.findByNameAndEmail(name, email);
    }

}

