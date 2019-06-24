package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
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
class AdminEventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    void persistingEventWithNullNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(null, ZonedDateTime.now(), "url"));
            eventRepository.findAllByOrderByDatetimeAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("", ZonedDateTime.now(), "url"));
            eventRepository.findAllByOrderByDatetimeAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", ZonedDateTime.now(), "url"));
            eventRepository.findAllByOrderByDatetimeAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), null));
            eventRepository.findAllByOrderByDatetimeAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), ""));
            eventRepository.findAllByOrderByDatetimeAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), "Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!"));
            eventRepository.findAllByOrderByDatetimeAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullDateThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", null, "url"));
            eventRepository.findAllByOrderByDatetimeAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("datetime", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void eventsAreOrderedByDatetimeAscending() {

        Event today = new Event("today", ZonedDateTime.now(), "today");
        Event yesterday = new Event("yesterday", ZonedDateTime.now().minusDays(1), "yesterday");
        Event tomorrow = new Event("tomorrow", ZonedDateTime.now().plusDays(1), "tomorrow");

        eventRepository.save(today);
        eventRepository.save(yesterday);
        eventRepository.save(tomorrow);

        List<Event> events = eventRepository.findAllByOrderByDatetimeAsc();

        assertEquals(yesterday, events.get(0));
        assertEquals(today, events.get(1));
        assertEquals(tomorrow, events.get(2));
    }

}
