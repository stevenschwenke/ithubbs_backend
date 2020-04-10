package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    @Autowired
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<EventModel>> getAllEvents() {

        List<EventModel> eventModels = eventRepository
                .findAllWithDatetimeAfter(ZonedDateTime.now())
                .stream()
                .map((event) -> new EventResourceAssembler(this.getClass(), EventModel.class).toModel(event))
                .collect(Collectors.toList());

        return new ResponseEntity<>(eventModels, HttpStatus.OK);
    }
}
