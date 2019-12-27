package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<GroupModel>> getAllGroups() {

        List<GroupModel> groupModels = groupRepository.findAll().stream().map((group) -> {

            GroupModel groupModel = new GroupResourceAssembler(this.getClass(), GroupModel.class).toModel(group);
            if (group.getGroupLogo() != null) {
                URI imageURI = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).logoOfGroup(group.getId())).toUri();
                groupModel.setImageURI(imageURI);
            }

            return groupModel;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(groupModels, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/{groupId}/logo")
    public ResponseEntity<byte[]> logoOfGroup(@PathVariable("groupId") long groupId) {

        Group group = this.groupRepository.findById(groupId).orElseThrow();
        GroupLogo groupLogo = group.getGroupLogo();
        byte[] content = ArrayUtils.toPrimitive((groupLogo != null && groupLogo.getContent() != null) ? groupLogo.getContent() : new Byte[]{});

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
