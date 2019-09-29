package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface AdminGroupService {

    void editGroup(Group newValue);

    void deleteGroup(Group newValue);

    void uploadGroupLogo(Long groupID, MultipartFile file) throws IOException, GroupNotFoundException;
}
