package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event.AdminEventController;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event.AdminEventService;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("junit")
class AdminEventControllerTest {

    @Autowired
    private AdminEventController adminEventController;

    @Test
    void creatingValidEventWillReturnHTTP200() {

        Event validGroup = new Event("name", ZonedDateTime.now(), "url");

        ResponseEntity<?> response = adminEventController.createNewEvent(validGroup);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validGroup.getId(), response.getBody());
    }

    @Test
    void creatingInvalidGroupWillReturnHTTP422() {

        Event emptyInvalidEvent = new Event();

        ResponseEntity<?> response = adminEventController.createNewEvent(emptyInvalidEvent);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void creatingValidGroupEditWillReturnHTTP200() {

        AdminEventController adminEventController = new AdminEventController(null, Mockito.mock(AdminEventService.class));

        ResponseEntity<?> response = adminEventController.editEvent(new Event());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void creatingInvalidEventEditWillReturnHTTP422() {

        AdminEventService adminEventServiceMock = Mockito.mock(AdminEventService.class);
        Mockito.doThrow(IllegalArgumentException.class).when(adminEventServiceMock).editEvent(any());

        AdminEventController adminEventController = new AdminEventController(null, adminEventServiceMock);

        ResponseEntity<?> response = adminEventController.editEvent(new Event());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void creatingValidEventDeletionWillReturnHTTP200() {

        AdminEventController adminEventController = new AdminEventController(null, Mockito.mock(AdminEventService.class));

        ResponseEntity<?> response = adminEventController.deleteEvent(new Event());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void creatingInvalidEventDeletionWillReturnHTTP422() {

        AdminEventService adminEventServiceMock = Mockito.mock(AdminEventService.class);
        Mockito.doThrow(IllegalArgumentException.class).when(adminEventServiceMock).deleteEvent(any());

        AdminEventController adminEventController = new AdminEventController(null, adminEventServiceMock);

        ResponseEntity<?> response = adminEventController.deleteEvent(new Event());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @AfterEach
    void tearDown(@Autowired EventRepository repository) {
        repository.deleteAll();
    }
}
