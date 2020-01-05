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
    public ResponseEntity<?> createOrUpdate(@RequestBody Group group) {

        try {
            if (group.getId() == null) {

                Group newGroup = adminGroupService.createNewGroup(group);

                return addLogoAndConvertToResponseEntity(newGroup);

            } else {

                Group editedGroup = adminGroupService.editGroup(group);

                return addLogoAndConvertToResponseEntity(editedGroup);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
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

        return new ResponseEntity<>("{\"logoURI\":\"" + imageURI + "\"}", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "")
    public ResponseEntity<?> delete(@RequestBody Group group) {

        try {
            adminGroupService.deleteGroup(group);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<?> addLogoAndConvertToResponseEntity(Group savedGroup) {

        GroupModel groupModel = new GroupResourceAssembler(this.getClass(), GroupModel.class).toModel(savedGroup);

        if (savedGroup.getGroupLogo() != null) {
            URI imageURI = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).logoOfGroup(savedGroup.getId())).toUri();
            groupModel.setImageURI(imageURI);
        }

        return new ResponseEntity<>(groupModel, HttpStatus.OK);
    }
}
