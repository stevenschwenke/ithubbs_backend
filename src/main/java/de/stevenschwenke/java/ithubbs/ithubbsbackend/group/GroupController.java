package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;
    private final GroupRepository groupRepository;

    @Autowired
    public GroupController(GroupService groupService, GroupRepository groupRepository) {
        this.groupService = groupService;
        this.groupRepository = groupRepository;
    }

    @GetMapping(value = "")
    public ResponseEntity<CollectionModel<GroupModel>> getAllGroups() {

        List<GroupModel> groupModels = groupRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map((group) -> new GroupResourceAssembler(this.getClass(), GroupModel.class, groupService).toModel(group))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new CollectionModel<>(groupModels));
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<GroupModel> getGroup(@PathVariable("groupId") long groupId) {

        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();

            GroupModel groupModel = new GroupResourceAssembler(this.getClass(), GroupModel.class, groupService).toModel(group);
            return ResponseEntity.ok(groupModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseBody
    @GetMapping(value = "/{groupId}/logo", produces = "image/png")
    public ResponseEntity<byte[]> logoOfGroup(@PathVariable("groupId") long groupId) {

        Group group = this.groupRepository.findById(groupId).orElseThrow();
        GroupLogo groupLogo = group.getGroupLogo();
        byte[] content = ArrayUtils.toPrimitive((groupLogo != null && groupLogo.getContent() != null) ? groupLogo.getContent() : new Byte[]{});

        return ResponseEntity.ok(content);
    }
}
