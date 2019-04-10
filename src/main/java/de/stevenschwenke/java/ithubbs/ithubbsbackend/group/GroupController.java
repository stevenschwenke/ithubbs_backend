package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Group>> getAllGroups() {

        return new ResponseEntity<>(groupRepository.findAll(), HttpStatus.OK);
    }
}
