package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/admin/groups")
public class AdminGroupController {

    private final GroupService groupService;
    private final AdminGroupService adminGroupService;

    @Autowired
    public AdminGroupController(GroupService groupService, AdminGroupService adminGroupService) {
        this.groupService = groupService;
        this.adminGroupService = adminGroupService;
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createOrUpdate(@RequestBody Group group) {

        try {
            if (group.getId() == null) {

                Group newGroup = adminGroupService.createNewGroup(group);
                GroupModel groupModel = new GroupResourceAssembler(this.getClass(), GroupModel.class, groupService).toModel(newGroup);
                return ResponseEntity
                        .created(groupModel.getLinks().getLink("self").orElseThrow().toUri())
                        .body(groupModel);

            } else {

                Group editedGroup = adminGroupService.editGroup(group);
                GroupModel groupModel = new GroupResourceAssembler(this.getClass(), GroupModel.class, groupService).toModel(editedGroup);
                return ResponseEntity.ok(groupModel);
            }

        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PostMapping(value = "logo")
    public ResponseEntity<?> uploadGroupLogo(
            @RequestParam("groupID") Long groupID,
            @RequestParam("file") MultipartFile file) {

        try {
            adminGroupService.uploadGroupLogo(groupID, file);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (GroupNotFoundException e) {
            return ResponseEntity.unprocessableEntity().build();
        }

        URI imageURI = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).logoOfGroup(groupID)).toUri();

        return ResponseEntity.created(imageURI).build();
    }

    @DeleteMapping(value = "")
    public ResponseEntity<?> delete(@RequestBody Group group) {

        try {
            adminGroupService.deleteGroup(group);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }
}
