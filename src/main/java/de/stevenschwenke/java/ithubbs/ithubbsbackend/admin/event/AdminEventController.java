package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {

    private final AdminEventRepository adminEventRepository;
    private final AdminEventService adminEventService;

    @Autowired
    public AdminEventController(AdminEventRepository adminEventRepository,
                                AdminEventService adminEventService) {
        this.adminEventRepository = adminEventRepository;
        this.adminEventService = adminEventService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Event>> getAllEvents() {

        return new ResponseEntity<>(adminEventRepository.findAllByOrderByDatetimeAsc(), HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createNewEvent(@RequestBody Event event) {

        Event savedEvent;
        try {

            savedEvent = adminEventRepository.save(event);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(savedEvent.getId(), HttpStatus.OK);
    }

    @PostMapping(value = "edit")
    public ResponseEntity<?> editEvent(@RequestBody Event event) {

        try {
            adminEventService.editEvent(event);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> deleteEvent(@RequestBody Event event) {

        try {
            adminEventService.deleteEvent(event);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
