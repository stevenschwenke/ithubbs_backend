package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminEventRepository extends CrudRepository<Event, Long> {

    List<Event> findAllByOrderByDatetimeAsc();
}
