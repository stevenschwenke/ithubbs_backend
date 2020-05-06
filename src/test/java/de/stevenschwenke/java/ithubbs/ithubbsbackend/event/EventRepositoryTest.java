package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void persistingEventWithoutDatetimeThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", null, "URL", false));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("datetime", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(null, ZonedDateTime.now(), "URL", false));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("", ZonedDateTime.now(), "URL", false));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", ZonedDateTime.now(), "URL", false));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), null, false));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), "", false));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), "Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", false));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void eventsAreOrderedByDatetimeAscending() {

        Event tPlus1 = new Event("1", ZonedDateTime.now().plusDays(1), "1", false);
        Event tPlus2 = new Event("2", ZonedDateTime.now().plusDays(2), "2", false);
        Event tPlus3 = new Event("3", ZonedDateTime.now().plusDays(3), "3", false);

        // saving in random order
        eventRepository.save(tPlus3);
        eventRepository.save(tPlus1);
        eventRepository.save(tPlus2);

        List<Event> events = eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());

        assertEquals(tPlus1, events.get(0));
        assertEquals(tPlus2, events.get(1));
        assertEquals(tPlus3, events.get(2));
    }

    @Test
    void onlyEventsInTheFutureAreReturned() {

        Event today = new Event("today", ZonedDateTime.now(), "today", false);
        Event yesterday = new Event("yesterday", ZonedDateTime.now().minusDays(1), "yesterday", false);
        Event tomorrow = new Event("tomorrow", ZonedDateTime.now().plusDays(1), "tomorrow", false);

        eventRepository.save(today);
        eventRepository.save(yesterday);
        eventRepository.save(tomorrow);

        List<Event> events = eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());

        assertEquals(1, events.size());
        assertEquals(tomorrow, events.get(0));
    }

    @Test
    void onlyEventsWithTagGeneralPublicAreReturnedWhenSearchingForThem() {

        Event general1 = new Event("general1", ZonedDateTime.now(), "general1", true);
        Event specific1 = new Event("specific1", ZonedDateTime.now().minusDays(1), "specific1", false);
        Event general2 = new Event("general2", ZonedDateTime.now().plusDays(1), "general2", true);

        eventRepository.save(general1);
        eventRepository.save(specific1);
        eventRepository.save(general2);

        List<Event> events = eventRepository.findAllGeneralPublic();

        assertEquals(2, events.size());
        assertEquals(general1, events.get(0));
        assertEquals(general2, events.get(1));
    }

    @Test
    void countEventsByGroupTest() {

        Group group = new Group("groupt", "group URI", "group description");
        group = groupRepository.save(group);

        Event event1 = new Event("event1", ZonedDateTime.now(), "event1", true);
        Event event2 = new Event("event2", ZonedDateTime.now(), "event2", true);
        eventRepository.save(event1);
        eventRepository.save(event2);

        assertEquals(Integer.valueOf(0), eventRepository.countAllByGroup(group));

        event1.setGroup(group);
        eventRepository.save(event1);
        assertEquals(Integer.valueOf(1), eventRepository.countAllByGroup(group));

        event2.setGroup(group);
        eventRepository.save(event2);
        assertEquals(Integer.valueOf(2), eventRepository.countAllByGroup(group));
    }

}
