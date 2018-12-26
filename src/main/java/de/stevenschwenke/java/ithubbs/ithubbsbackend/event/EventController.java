package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events/")
public class EventController {

    @GetMapping(value = "")
    public ResponseEntity<List<Event>> getAllEvents() {
        return new ResponseEntity<>(List.of(new Event("Event 1"), new Event("Event 2")), HttpStatus.OK);
    }
}
