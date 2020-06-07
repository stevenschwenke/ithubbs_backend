package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final EventRepository eventRepository;

    @Autowired
    public EventController(EventService eventService, EventRepository eventRepository) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    @GetMapping(value = "/years")
    public ResponseEntity<CollectionModel<Integer>> getYearsOfEvents() {
        List<Integer> years = eventService.retrieveYearsWithEvents();
        return ResponseEntity.ok(new CollectionModel<>(years));
    }

    @GetMapping(value = "")
    public ResponseEntity<CollectionModel<EventModel>> getAllEvents(@RequestParam(required = false) Integer year) {

        List<Event> requestedEvents;

        if (year != null) {
            requestedEvents = eventRepository.findAllInYear(year);
        } else {
            requestedEvents = eventRepository.findAllWithDatetimeAfter(ZonedDateTime.now());
        }

        List<EventModel> eventModels = requestedEvents
                .stream()
                .map((event) -> new EventResourceAssembler(this.getClass(), EventModel.class).toModel(event))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CollectionModel<>(eventModels));
    }

    @GetMapping(value = "/statistics")
    public ResponseEntity<EventStatistics> getStatisticsForEvents() {
        return ResponseEntity.ok(eventService.calculateEventStatistics());
    }
}
