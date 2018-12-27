package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    @Override
    List<Event> findAll();
}
