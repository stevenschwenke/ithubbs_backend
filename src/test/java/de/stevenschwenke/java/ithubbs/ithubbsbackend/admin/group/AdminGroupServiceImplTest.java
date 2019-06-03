package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
class AdminGroupServiceImplTest {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private AdminGroupServiceImpl adminGroupService;

    @Test
    void editingNotExistingGroupWillThrowException() {

        Group validGroup = new Group(null, null, null);

        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            adminGroupService.editGroup(validGroup);
            groupRepository.findAll();
        });
    }

    @Test
    void editingExistingGroupWithInvalidDataWillThrowException() {

        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));

        Group validGroup = new Group(null, null, null);
        validGroup.setId(savedGroup.getId());

        assertThrows(ConstraintViolationException.class, () -> {
            adminGroupService.editGroup(validGroup);
            groupRepository.findAll();
        });
    }

    @Test
    void editingExistingGroupWithValidDataWillChangeData() {

        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));

        Group validGroup = new Group("new name", "new url", "new description");
        validGroup.setId(savedGroup.getId());

        adminGroupService.editGroup(validGroup);

        Optional<Group> reloadedGroupOptional = groupRepository.findById(savedGroup.getId());
        assertTrue(reloadedGroupOptional.isPresent());
        assertEquals("new name", reloadedGroupOptional.get().getName());
        assertEquals("new url", reloadedGroupOptional.get().getUrl());
        assertEquals("new description", reloadedGroupOptional.get().getDescription());
    }

    @Test
    void deleteNotExistingGroupWillThrowException() {

        Group validGroup = new Group(null, null, null);

        assertThrows(ConstraintViolationException.class, () -> {
            adminGroupService.deleteGroup(validGroup);
            groupRepository.findAll();
        });
    }


    @Test
    void deleteExistingGroupWillDeleteGroup() {

        groupRepository.deleteAll();

        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));

        assertEquals(1, groupRepository.count());

        adminGroupService.deleteGroup(savedGroup);

        assertEquals(0, groupRepository.count());
    }
}
