package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

        List<GroupModel> groupModels = groupRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map((group) -> new GroupResourceAssembler(this.getClass(), GroupModel.class).toModel(group))
                .collect(Collectors.toList());

        return new ResponseEntity<>(groupModels, HttpStatus.OK);
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<GroupModel> getGroup(@PathVariable("groupId") long groupId) {

        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if(groupOptional.isPresent()) {
            Group group = groupOptional.get();

            GroupModel groupModel = new GroupResourceAssembler(this.getClass(), GroupModel.class).toModel(group);
            return new ResponseEntity<>(groupModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
