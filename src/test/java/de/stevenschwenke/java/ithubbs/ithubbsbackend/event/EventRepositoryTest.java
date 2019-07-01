package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

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

    @Test
    void persistingEventWithoutDatetimeThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", null, "URL"));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("datetime", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(null, ZonedDateTime.now(), "URL"));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("", ZonedDateTime.now(), "URL"));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", ZonedDateTime.now(), "URL"));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), null));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), ""));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), "Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!"));
            eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void eventsAreOrderedByDatetimeAscending() {

        Event tPlus1 = new Event("1", ZonedDateTime.now().plusDays(1), "1");
        Event tPlus2 = new Event("2", ZonedDateTime.now().plusDays(2), "2");
        Event tPlus3 = new Event("3", ZonedDateTime.now().plusDays(3), "3");

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

        Event today = new Event("today", ZonedDateTime.now(), "today");
        Event yesterday = new Event("yesterday", ZonedDateTime.now().minusDays(1), "yesterday");
        Event tomorrow = new Event("tomorrow", ZonedDateTime.now().plusDays(1), "tomorrow");

        eventRepository.save(today);
        eventRepository.save(yesterday);
        eventRepository.save(tomorrow);

        List<Event> events = eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());

        assertEquals(1, events.size());
        assertEquals(tomorrow, events.get(0));
    }
}
