package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminGroupRepository extends CrudRepository<Group, Long> {

    @Override
    List<Group> findAll();
}
