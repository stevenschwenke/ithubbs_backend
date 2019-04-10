package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminGroupServiceImpl implements AdminGroupService {

    private final AdminGroupRepository adminGroupRepository;

    @Autowired
    public AdminGroupServiceImpl(AdminGroupRepository adminGroupRepository) {
        this.adminGroupRepository = adminGroupRepository;
    }

    @Override
    public void editGroup(Group newValue) {

        Group changedGroup = adminGroupRepository.findById(newValue.getId()).orElseThrow();
        changedGroup.setName(newValue.getName());
        changedGroup.setUrl(newValue.getUrl());
        changedGroup.setDescription(newValue.getDescription());
        adminGroupRepository.save(newValue);
    }

    @Override
    public void deleteGroup(Group group) {

        adminGroupRepository.delete(group);
    }
}
