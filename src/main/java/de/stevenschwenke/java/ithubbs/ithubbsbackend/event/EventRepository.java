package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    @Query("select e from Event e where e.datetime >= :datetime order by e.datetime asc ")
    List<Event> findAllWithDatetimeAfter(@Param("datetime") ZonedDateTime datetime);

    List<Event> findAllByOrderByDatetimeAsc();

    @Query("select e from Event e where e.generalPublic = true")
    List<Event> findAllGeneralPublic();
}
