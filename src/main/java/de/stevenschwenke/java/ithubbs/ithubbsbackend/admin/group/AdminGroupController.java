package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupController;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupModel;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/admin/groups")
public class AdminGroupController {

    private final AdminGroupService adminGroupService;

    @Autowired
    public AdminGroupController(AdminGroupService adminGroupService) {
        this.adminGroupService = adminGroupService;
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createNewGroup(@RequestBody Group group) {
        GroupModel groupModel;
        try {
            Group savedGroup = adminGroupService.createNewGroup(group);

            groupModel = new GroupResourceAssembler(this.getClass(), GroupModel.class).toModel(savedGroup);
            if (savedGroup.getGroupLogo() != null) {
                URI imageURI = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).logoOfGroup(savedGroup.getId())).toUri();
                groupModel.setImageURI(imageURI);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(groupModel, HttpStatus.OK);
    }

    @PostMapping(value = "logo")
    public ResponseEntity<?> uploadGroupLogo(
            @RequestParam("groupID") Long groupID,
            @RequestParam("file") MultipartFile file) {

        try {
            adminGroupService.uploadGroupLogo(groupID, file);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (GroupNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        URI imageURI = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).logoOfGroup(groupID)).toUri();

        return new ResponseEntity<>("{\"logoURI\":\""+imageURI+"\"}", HttpStatus.CREATED);
    }

    @PostMapping(value = "edit")
    public ResponseEntity<?> editGroup(@RequestBody Group group) {

        GroupModel groupModel;

        try {
            Group editedGroup = adminGroupService.editGroup(group);

            groupModel = new GroupResourceAssembler(this.getClass(), GroupModel.class).toModel(editedGroup);
            if (group.getGroupLogo() != null) {
                URI imageURI = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).logoOfGroup(group.getId())).toUri();
                groupModel.setImageURI(imageURI);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(groupModel, HttpStatus.OK);
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<?> deleteGroup(@RequestBody Group group) {

        try {
            adminGroupService.deleteGroup(group);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
