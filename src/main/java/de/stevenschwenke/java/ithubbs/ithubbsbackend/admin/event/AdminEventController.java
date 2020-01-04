package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventModel;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<EventModel>> getAllEvents() {

        List<EventModel> eventModels = eventRepository.findAllByOrderByDatetimeAsc().stream().map((event) -> new EventResourceAssembler(this.getClass(), EventModel.class).toModel(event)).collect(Collectors.toList());

        return new ResponseEntity<>(eventModels, HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createNewEvent(@RequestBody Event event) {

        EventModel eventModel;

        try {
            Event savedEvent = eventRepository.save(event);

            eventModel = new EventResourceAssembler(this.getClass(), EventModel.class).toModel(savedEvent);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(eventModel, HttpStatus.OK);
    }

    @PostMapping(value = "edit")
    public ResponseEntity<?> editEvent(@RequestBody Event event) {

        try {
            adminEventService.editEvent(event);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO URIs anpassen: "delete" und "edit" sollte nur in Verben stehen, nicht in URI (überall, inklusive frontend)
    @DeleteMapping(value = "delete")
    public ResponseEntity<?> deleteEvent(@RequestBody Event event) {

        try {
            adminEventService.deleteEvent(event);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
