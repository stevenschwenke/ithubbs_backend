package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventModel;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {

    private final EventRepository eventRepository;
    private final AdminEventService adminEventService;

    @Autowired
    public AdminEventController(EventRepository eventRepository,
                                AdminEventService adminEventService) {
        this.eventRepository = eventRepository;
        this.adminEventService = adminEventService;
    }

    @GetMapping(value = "")
    public ResponseEntity<CollectionModel<EventModel>> getAllEvents() {

        List<EventModel> eventModels = eventRepository.findAllByOrderByDatetimeAsc().stream().map((event) -> new EventResourceAssembler(this.getClass(), EventModel.class).toModel(event)).collect(Collectors.toList());

        return new ResponseEntity<>(new CollectionModel<>(eventModels), HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createOrUpdate(@RequestBody Event event) {

        try {
            if (event.getId() == null) {

                // Create

                Event savedEvent = eventRepository.save(event);
                EventModel eventModel = new EventResourceAssembler(this.getClass(), EventModel.class).toModel(savedEvent);
                return new ResponseEntity<>(eventModel, HttpStatus.CREATED);

            } else {

                // Edit
                adminEventService.editEvent(event);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping(value = "")
    public ResponseEntity<?> delete(@RequestBody Event event) {

        try {
            adminEventService.deleteEvent(event);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
