package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @GetMapping(value = "/{groupId}/logo")
    public ResponseEntity<byte[]> alskdfalsd(@PathVariable("groupId") long groupId) {

        Group group = this.groupRepository.findById(groupId).orElseThrow();
        GroupLogo groupLogo = group.getGroupLogo();
        byte[] content = ArrayUtils.toPrimitive(groupLogo.getContent());

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
