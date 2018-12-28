package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/groups")
public class AdminGroupController {

    private final AdminGroupRepository adminGroupRepository;

    @Autowired
    public AdminGroupController(AdminGroupRepository adminGroupRepository) {
        this.adminGroupRepository = adminGroupRepository;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Group>> getAllEvents() {

        return new ResponseEntity<>(adminGroupRepository.findAll(), HttpStatus.OK);
    }
}
