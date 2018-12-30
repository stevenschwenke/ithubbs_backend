package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.springframework.stereotype.Service;

@Service
public interface AdminGroupService {

    void editGroup(Group newValue);
}
