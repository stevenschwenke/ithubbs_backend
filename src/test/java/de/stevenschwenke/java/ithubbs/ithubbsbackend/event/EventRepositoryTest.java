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

        List<Event> events = eventRepository.findAllGeneralPublicSortDesc();

        assertEquals(2, events.size());
        assertEquals(general2, events.get(0));
        assertEquals(general1, events.get(1));
    }

    @Test
    void countEventsByGroupTest() {

        Group group = new Group("group", "group URI", "group description");
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

    @Test
    void eventsForAtomFeedAreOrderedByDatetimeDescending() {

        Event tPlus1 = new Event("1", ZonedDateTime.now().plusDays(1), "1", true);
        Event tPlus2 = new Event("2", ZonedDateTime.now().plusDays(2), "2", true);
        Event tPlus3 = new Event("3", ZonedDateTime.now().plusDays(3), "3", true);

        // saving in random order
        eventRepository.save(tPlus3);
        eventRepository.save(tPlus1);
        eventRepository.save(tPlus2);

        List<Event> allEventsForFeed = eventRepository.findAllByOrderByDatetimeDesc();
        List<Event> generalPublicEventsForFeed = eventRepository.findAllGeneralPublicSortDesc();

        assertEquals(tPlus3, allEventsForFeed.get(0));
        assertEquals(tPlus2, allEventsForFeed.get(1));
        assertEquals(tPlus1, allEventsForFeed.get(2));

        assertEquals(tPlus3, generalPublicEventsForFeed.get(0));
        assertEquals(tPlus2, generalPublicEventsForFeed.get(1));
        assertEquals(tPlus1, generalPublicEventsForFeed.get(2));
    }

    @Test
    void findOldestEventByGroup() {

        Group group = new Group("group", "group URI", "group description");
        group = groupRepository.save(group);

        Event event1 = new Event("event1", ZonedDateTime.now(), "event1", true);
        event1.setGroup(group);
        Event event2 = new Event("event2", ZonedDateTime.now().plusDays(1), "event2", true);
        event2.setGroup(group);
        eventRepository.save(event1);
        eventRepository.save(event2);

        assertEquals(event1, eventRepository.findFirstByGroupOrderByDatetimeAsc(group));
    }

    @Test
    void findMostRecentEventByGroup() {

        Group group = new Group("group", "group URI", "group description");
        group = groupRepository.save(group);

        Event event1 = new Event("event1", ZonedDateTime.now(), "event1", true);
        event1.setGroup(group);
        Event event2 = new Event("event2", ZonedDateTime.now().plusDays(1), "event2", true);
        event2.setGroup(group);
        eventRepository.save(event1);
        eventRepository.save(event2);

        assertEquals(event2, eventRepository.findFirstByGroupOrderByDatetimeDesc(group));
    }

    @Test
    void countAllByDatetimeAfterReturnsCorrectValue() {

        Event event1 = new Event("event1", ZonedDateTime.now(), "event1", true);
        Event event2 = new Event("event2", ZonedDateTime.now().plusDays(1), "event2", true);
        eventRepository.save(event1);
        eventRepository.save(event2);

        assertEquals(Integer.valueOf(2), eventRepository.countAllByDatetimeAfter(ZonedDateTime.now().minusDays(10)));
    }

    @Test
    void searchForEarliestKnownEventReturnsCorrectValue() {

        Event event1 = new Event("event1", ZonedDateTime.now(), "event1", true);
        Event event2 = new Event("event2", ZonedDateTime.now().plusDays(1), "event2", true);
        eventRepository.save(event1);
        eventRepository.save(event2);

        assertEquals(event1, eventRepository.findTopByOrderByDatetimeAsc());
    }

    @Test
    void searchForLatestKnownEventReturnsCorrectValue() {

        Event event1 = new Event("event1", ZonedDateTime.now(), "event1", true);
        Event event2 = new Event("event2", ZonedDateTime.now().plusDays(1), "event2", true);
        eventRepository.save(event1);
        eventRepository.save(event2);

        assertEquals(event2, eventRepository.findTopByOrderByDatetimeDesc());
    }

    @Test
    void findAllInYearFindsAllEventsInThatYearCorrectly() {

        Event event1 = new Event("event1", ZonedDateTime.now(), "event1", true);
        Event event2 = new Event("event2", ZonedDateTime.now().plusDays(1), "event2", true);
        Event event3 = new Event("event3", ZonedDateTime.now().plusYears(1), "event3", true);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        List<Event> foundEvents = eventRepository.findAllInYear(ZonedDateTime.now().getYear());
        assertEquals(2, foundEvents.size());
        assertEquals(event1, foundEvents.get(0));
        assertEquals(event2, foundEvents.get(1));
    }

    @Test
    void findAllYearsOfEventsFindsAllYearsCorrectly() {

        Event event1 = new Event("event1", ZonedDateTime.now(), "event1", true);
        Event event2 = new Event("event2", ZonedDateTime.now().plusDays(1), "event2", true);
        Event event3 = new Event("event3", ZonedDateTime.now().plusYears(1), "event3", true);
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        List<Integer> datetimeOfEvents = eventRepository.findAllYearsOfEvents();
        assertEquals(2, datetimeOfEvents.size());
        assertEquals(ZonedDateTime.now().getYear(), datetimeOfEvents.get(0).intValue());
        assertEquals(ZonedDateTime.now().getYear() + 1, datetimeOfEvents.get(1).intValue());
    }
}
