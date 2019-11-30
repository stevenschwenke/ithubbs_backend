package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogo;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogoRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
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
    private final GroupLogoRepository groupLogoRepository;

    @Autowired
    public AdminGroupController(AdminGroupRepository adminGroupRepository,
                                AdminGroupService adminGroupService,
                                GroupLogoRepository groupLogoRepository) {
        this.adminGroupRepository = adminGroupRepository;
        this.adminGroupService = adminGroupService;
        this.groupLogoRepository = groupLogoRepository;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Group>> getAllGroup() {

        return new ResponseEntity<>(adminGroupRepository.findAll(), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/logo/{logoId}")
    public ResponseEntity<byte[]> handler(@PathVariable("logoId") long logoId) {

        GroupLogo groupLogo = this.groupLogoRepository.findById(logoId).orElseThrow();
        byte[] content = ArrayUtils.toPrimitive(groupLogo.getContent());

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
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

        return new ResponseEntity<>(HttpStatus.CREATED);
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
