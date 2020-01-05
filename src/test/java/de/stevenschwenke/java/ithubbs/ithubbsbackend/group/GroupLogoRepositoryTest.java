package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
class GroupLogoRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupLogoRepository groupLogoRepository;

    @Test
    void mappingBetweenGroupAndGroupLogoIsWorking() {

        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));
        assertNotNull(savedGroup.getId());
        GroupLogo savedGroupLogo = groupLogoRepository.save(new GroupLogo("filename", "filetype", new Byte[]{}));
        assertNotNull(savedGroupLogo.getId());

        savedGroup.setGroupLogo(savedGroupLogo);

        // "flush"
        groupRepository.findAll();
        groupLogoRepository.findAll();

        Group groupReloaded = groupRepository.findById(savedGroup.getId()).orElseThrow();
        assertEquals(savedGroupLogo, groupReloaded.getGroupLogo());
    }

    @Test
    void savingAGroupWithoutGroupsLogoIsPossible() {

        groupRepository.deleteAll();
        groupLogoRepository.deleteAll();

        assertTrue(groupRepository.findAll().isEmpty());
        assertTrue(groupLogoRepository.findAll().isEmpty());

        Group group = new Group("name", "url", "description");
        group.setGroupLogo(null);

        Group savedGroup = groupRepository.save(group);

        // "flush"
        groupRepository.findAll();
        groupLogoRepository.findAll();
    }

    @Test
    void savingAGroupSavesTheGroupsLogo() {

        groupRepository.deleteAll();
        groupLogoRepository.deleteAll();

        assertTrue(groupRepository.findAll().isEmpty());
        assertTrue(groupLogoRepository.findAll().isEmpty());

        Group group = new Group("name", "url", "description");
        GroupLogo groupLogo = new GroupLogo("filename", "filetype", new Byte[]{});
        group.setGroupLogo(groupLogo);

        Group savedGroup = groupRepository.save(group);

        // "flush"
        groupRepository.findAll();
        groupLogoRepository.findAll();

        assertNotNull(savedGroup.getId());
        assertNotNull(savedGroup.getGroupLogo());

        GroupLogo savedGroupLogo = groupLogoRepository.save(groupLogo);
        assertNotNull(savedGroupLogo.getId());

        assertEquals(savedGroupLogo, savedGroup.getGroupLogo());
    }
}
