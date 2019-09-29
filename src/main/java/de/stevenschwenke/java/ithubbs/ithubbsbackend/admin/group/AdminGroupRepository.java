package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminGroupRepository extends CrudRepository<Group, Long> {
// TODO join this class with GroupRepository, same for the service class and the same for events
    @Override
    List<Group> findAll();
}
