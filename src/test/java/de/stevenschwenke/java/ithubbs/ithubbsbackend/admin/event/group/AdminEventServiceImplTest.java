package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event.AdminEventServiceImpl;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
class AdminEventServiceImplTest {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AdminEventServiceImpl adminEventService;

    @Test
    void editingNotExistingEventWillThrowException() {

        Event validEvent = new Event(null, null, null);

        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            adminEventService.editEvent(validEvent);
            eventRepository.findAllByOrderByDatetimeAsc();
        });
    }

    @Test
    void editingExistingEventWithInvalidDataWillThrowException() {

        Event savedEvent = eventRepository.save(new Event("name", ZonedDateTime.now(), "url"));

        Event validEvent = new Event(null, null, null);
        validEvent.setId(savedEvent.getId());

        assertThrows(ConstraintViolationException.class, () -> {
            adminEventService.editEvent(validEvent);
            eventRepository.findAllByOrderByDatetimeAsc();
        });
    }

    @Test
    void editingExistingEventWithValidDataWillChangeData() {

        ZonedDateTime date = ZonedDateTime.now();
        Event savedEvent = eventRepository.save(new Event("name", date, "url"));

        Event validEvent = new Event("new name", date, "new url");
        validEvent.setId(savedEvent.getId());

        adminEventService.editEvent(validEvent);

        Optional<Event> reloadedEventOptional = eventRepository.findById(savedEvent.getId());
        assertTrue(reloadedEventOptional.isPresent());
        assertEquals("new name", reloadedEventOptional.get().getName());
        assertEquals("new url", reloadedEventOptional.get().getUrl());
        assertEquals(date, reloadedEventOptional.get().getDatetime());
    }

    @Test
    void deleteNotExistingEventWillThrowException() {

        Event validEvent = new Event(null, null, null);

        assertThrows(ConstraintViolationException.class, () -> {
            adminEventService.deleteEvent(validEvent);
            eventRepository.findAllByOrderByDatetimeAsc();
        });
    }

    @Test
    void deleteExistingEventWillDeleteEvent() {

        eventRepository.deleteAll();

        Event savedEvent = eventRepository.save(new Event("name", ZonedDateTime.now(), "url"));

        assertEquals(1, eventRepository.count());

        adminEventService.deleteEvent(savedEvent);

        assertEquals(0, eventRepository.count());
    }
}
