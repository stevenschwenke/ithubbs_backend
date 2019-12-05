package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<List<Group>> getAllGroup() {

        List<Group> all = adminGroupRepository.findAll();

        for (Group g : all) {
            g.setImageURI("http://localhost:8090/ithubbs/api/groups/" + g.getId() + "/logo");
        }

        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping(value = "")
    public ResponseEntity<?> createNewGroup(@RequestBody Group group) {
        Group savedGroup;
        try {
            savedGroup = adminGroupService.createNewGroup(group);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(savedGroup, HttpStatus.OK);
    }

    @PostMapping(value = "logo")
    public ResponseEntity<?> uploadGroupLogo(
            @RequestParam("groupID") Long groupID,
            @RequestParam("file") MultipartFile file) {

        String logoURI;

        try {
            logoURI = adminGroupService.uploadGroupLogo(groupID, file);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (GroupNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(logoURI, HttpStatus.CREATED);
    }

    @PostMapping(value = "edit")
    public ResponseEntity<?> editGroup(@RequestBody Group group) {

        Group editedGroup;

        try {
            editedGroup = adminGroupService.editGroup(group);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(editedGroup, HttpStatus.OK);
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
