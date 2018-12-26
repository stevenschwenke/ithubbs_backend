package de.stevenschwenke.java.ithubbs.ithubbsbackend.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends CrudRepository<User,Long> {

    @Override
    Optional<User> findById(Long fg);

    Optional<User> findUserByUsername(String username);

}

