package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {

    private final AdminEventRepository adminEventRepository;

    @Autowired
    public AdminEventController(AdminEventRepository adminEventRepository) {
        this.adminEventRepository = adminEventRepository;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Event>> getAllEvents() {

        return new ResponseEntity<>(adminEventRepository.findAll(), HttpStatus.OK);
    }
}
