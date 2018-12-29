package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    void persistingEventWithoutDatetimeThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(null, "name", "URL"));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("datetime", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(LocalDateTime.now(), null, "URL"));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(LocalDateTime.now(), "", "URL"));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(LocalDateTime.now(), "Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", "URL"));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithNullURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(LocalDateTime.now(), "name", null));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithEmptyURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(LocalDateTime.now(), "name", ""));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            eventRepository.save(new Event(LocalDateTime.now(), "name", "Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!"));
            eventRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }
}