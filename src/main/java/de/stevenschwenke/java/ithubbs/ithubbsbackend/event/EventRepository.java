package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    @Query("select e from Event e where e.datetime >= :datetime order by e.datetime asc ")
    List<Event> findAllWithDatetimeAfter(@Param("datetime") ZonedDateTime datetime);

    Integer countAllByDatetimeAfter(@Param("datetime") ZonedDateTime datetime);

    List<Event> findAllByOrderByDatetimeDesc();

    @Query("select e from Event e where e.generalPublic = true order by e.datetime desc ")
    List<Event> findAllGeneralPublicSortDesc();

    Integer countAllByGroup(Group group);

    /**
     * @param group to find event for
     * @return earliest known event of a specific group
     */
    Event findFirstByGroupOrderByDatetimeAsc(Group group);

    /**
     * @return earliest known event
     */
    Event findTopByOrderByDatetimeAsc();

    /**
     * @return latest known event
     */
    Event findTopByOrderByDatetimeDesc();

    /**
     * @param group to find event for
     * @return latest known event of a specific group
     */
    Event findFirstByGroupOrderByDatetimeDesc(Group group);
}
