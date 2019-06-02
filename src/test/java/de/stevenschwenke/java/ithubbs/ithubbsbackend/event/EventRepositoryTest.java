package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;

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
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("datetime", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(null, ZonedDateTime.now(), "URL"));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("", ZonedDateTime.now(), "URL"));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", ZonedDateTime.now(), "URL"));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), null));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), ""));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event("name", ZonedDateTime.now(), "Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!"));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }
}
