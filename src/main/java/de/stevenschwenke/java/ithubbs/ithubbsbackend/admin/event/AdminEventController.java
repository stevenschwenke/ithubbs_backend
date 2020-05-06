package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventModel;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

        List<EventModel> eventModels = eventRepository.findAllByOrderByDatetimeDesc().stream().map((event) -> new EventResourceAssembler(this.getClass(), EventModel.class).toModel(event)).collect(Collectors.toList());

        return ResponseEntity.ok(new CollectionModel<>(eventModels));
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createOrUpdate(@RequestBody EventUpdateDTO eventDTO) {

        try {
            if (eventDTO.getId() == null) {

                // Create
                Event savedEvent = adminEventService.saveNewEvent(eventDTO);
                EventModel eventModel = new EventResourceAssembler(this.getClass(), EventModel.class).toModel(savedEvent);
                return ResponseEntity
                        .created(eventModel.getLinks().getLink("self").orElseThrow().toUri())
                        .body(eventModel);

            } else {
                // Edit
                adminEventService.editEvent(eventDTO);
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "")
    public ResponseEntity<?> delete(@RequestBody Event event) {

        try {
            adminEventService.deleteEvent(event);

        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }
}
