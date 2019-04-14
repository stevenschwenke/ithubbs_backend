package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/groups")
public class AdminGroupController {

    private final AdminGroupRepository adminGroupRepository;
    private final AdminGroupService adminGroupService;

    @Autowired
    public AdminGroupController(AdminGroupRepository adminGroupRepository,
                                AdminGroupService adminGroupService) {
        this.adminGroupRepository = adminGroupRepository;
        this.adminGroupService = adminGroupService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Group>> getAllEvents() {

        return new ResponseEntity<>(adminGroupRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createNewGroup(@RequestBody Group group) {

        Group savedGroup;
        try {
            savedGroup = adminGroupRepository.save(group);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(savedGroup.getId(), HttpStatus.OK);
    }

    @PostMapping(value = "edit")
    public ResponseEntity<?> editGroup(@RequestBody Group group) {

        try {
            adminGroupService.editGroup(group);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> deleteGroup(@RequestBody Group group) {

        try {
            adminGroupService.deleteGroup(group);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
