package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.User;

import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public List<User> findByName(String name) {
        return this.list("name", name);
    }

    public List<User> findByEmail(String email) {
        return this.list("email", email);
    }

    public List<User> findByNameAndEmail(String name, String email) {
        return this.list("name = ?1 and email = ?2", name, email);
    }

//TODO    public int updateById(User user) {
//        return this.update("id", id);
//    }
}
