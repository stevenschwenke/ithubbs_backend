package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupLogoRepository extends CrudRepository<GroupLogo, Long> {

    @Override
    List<GroupLogo> findAll();
}
