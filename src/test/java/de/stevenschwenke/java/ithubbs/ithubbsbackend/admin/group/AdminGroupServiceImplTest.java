package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogo;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogoRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
class AdminGroupServiceImplTest {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupLogoRepository groupLogoRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AdminGroupServiceImpl adminGroupService;

    @Test
    void editingNotExistingGroupWillThrowException() {

        Group validGroup = new Group(null, null, null);

        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            adminGroupService.editGroup(validGroup);
            groupRepository.findAllByOrderByNameAsc();
        });
    }

    @Test
    void editingExistingGroupWithInvalidDataWillThrowException() {

        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));

        Group validGroup = new Group(null, null, null);
        validGroup.setId(savedGroup.getId());

        assertThrows(ConstraintViolationException.class, () -> {
            adminGroupService.editGroup(validGroup);
            groupRepository.findAllByOrderByNameAsc();
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
    void deleteExistingGroupWillDeleteGroupAndAssociatedLogo() throws Exception {

        groupRepository.deleteAll();
        groupLogoRepository.deleteAll();

        String filename = "spock.jpg";
        Path pathOfSpock = Paths.get(ClassLoader.getSystemResource(filename).toURI());
        byte[] content = Files.readAllBytes(pathOfSpock);
        String fileType = "image/jpeg";
        GroupLogo groupLogo = new GroupLogo(filename, fileType, ArrayUtils.toObject(content));
        groupLogoRepository.save(groupLogo);
        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));
        savedGroup.setGroupLogo(groupLogo);

        assertEquals(1, groupRepository.count());
        assertEquals(1, groupLogoRepository.count());

        Group groupToDelete = new Group();
        groupToDelete.setId(savedGroup.getId());
        adminGroupService.deleteGroup(groupToDelete);

        assertEquals(0, groupRepository.count());
        assertEquals(0, groupLogoRepository.count());
    }

    @Test
    void deleteExistingGroupWithEventsWillThrowIllegalArgumentExceptionAndDeleteNothing() {

        groupRepository.deleteAll();
        groupLogoRepository.deleteAll();

        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));
        Event savedEvent = eventRepository.save(new Event("name", ZonedDateTime.now(), "url", false));
        savedEvent.setGroup(savedGroup);

        assertEquals(1, groupRepository.count());
        assertEquals(1, eventRepository.count());

        Group groupToDelete = new Group();
        groupToDelete.setId(savedGroup.getId());
        assertThrows(IllegalArgumentException.class, () -> adminGroupService.deleteGroup(groupToDelete));

        assertEquals(1, eventRepository.count());
        assertEquals(1, groupRepository.count());
    }


    @Test
    void uploadGroupLogoForNonExistingGroupWillThrowException() throws URISyntaxException, IOException {

        groupRepository.deleteAll();

        String filename = "spock.jpg";
        Path pathOfSpock = Paths.get(ClassLoader.getSystemResource(filename).toURI());
        byte[] content = Files.readAllBytes(pathOfSpock);
        String fileType = "image/jpeg";

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> adminGroupService.uploadGroupLogo(42L, new MockMultipartFile(filename, filename, fileType, content)));
        assertEquals("Group not found.", exception.getMessage());
    }

    @Test
    void uploadGroupLogoForExistingGroupWithoutLogoWillSaveLogoToGroup() throws URISyntaxException, IOException, GroupNotFoundException {

        groupLogoRepository.deleteAll();

        groupRepository.deleteAll();
        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));

        String filename = "spock.jpg";
        Path pathOfSpock = Paths.get(ClassLoader.getSystemResource(filename).toURI());
        byte[] content = Files.readAllBytes(pathOfSpock);
        String fileType = "image/jpeg";

        adminGroupService.uploadGroupLogo(savedGroup.getId(), new MockMultipartFile(filename, filename, fileType, content));

        GroupLogo savedGroupLogo = groupRepository.findById(savedGroup.getId()).orElseThrow().getGroupLogo();
        assertNotNull(savedGroupLogo);
        assertArrayEquals(ArrayUtils.toObject(content), savedGroupLogo.getContent());
        assertEquals("spock.jpg", savedGroupLogo.getFilename());
    }

    @Test
    void editingGroupWithExistingLogoWillNotDeleteOldLogo() throws IOException, GroupNotFoundException, URISyntaxException {

        groupLogoRepository.deleteAll();

        groupRepository.deleteAll();
        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));

        // Save Logo 1

        String filename = "spock.jpg";
        Path pathOfSpock = Paths.get(ClassLoader.getSystemResource(filename).toURI());
        byte[] content = Files.readAllBytes(pathOfSpock);
        String fileType = "image/jpeg";

        adminGroupService.uploadGroupLogo(savedGroup.getId(), new MockMultipartFile(filename, filename, fileType, content));

        GroupLogo savedGroupLogo = groupRepository.findById(savedGroup.getId()).orElseThrow().getGroupLogo();
        assertNotNull(savedGroupLogo);
        assertArrayEquals(ArrayUtils.toObject(content), savedGroupLogo.getContent());
        assertEquals("spock.jpg", savedGroupLogo.getFilename());

        // Edit group, but not upload new logo

        Group newValue = new Group("new name", "new url", "new description");
        newValue.setId(savedGroup.getId());
        Group editedGroup = adminGroupService.editGroup(newValue);

        // Check if logo is still present in group

        savedGroupLogo = groupRepository.findById(editedGroup.getId()).orElseThrow().getGroupLogo();
        assertNotNull(savedGroupLogo);
        assertArrayEquals(ArrayUtils.toObject(content), savedGroupLogo.getContent());
        assertEquals("spock.jpg", savedGroupLogo.getFilename());
    }

    @Test
    void uploadGroupLogoForExistingGroupWithLogoWillReplaceOldLogo() throws IOException, GroupNotFoundException, URISyntaxException {

        groupLogoRepository.deleteAll();

        groupRepository.deleteAll();
        assertEquals(0, groupLogoRepository.count());
        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));

        // Save Logo 1

        String filename = "spock.jpg";
        Path pathOfSpock = Paths.get(ClassLoader.getSystemResource(filename).toURI());
        byte[] content = Files.readAllBytes(pathOfSpock);
        String fileType = "image/jpeg";

        adminGroupService.uploadGroupLogo(savedGroup.getId(), new MockMultipartFile(filename, filename, fileType, content));

        assertEquals(1, groupLogoRepository.count());
        GroupLogo savedGroupLogo = groupRepository.findById(savedGroup.getId()).orElseThrow().getGroupLogo();
        assertNotNull(savedGroupLogo);
        assertArrayEquals(ArrayUtils.toObject(content), savedGroupLogo.getContent());
        assertEquals("spock.jpg", savedGroupLogo.getFilename());

        // Save Logo 2

        String filenameZulu = "zulu.jpg";
        Path pathOfZulu = Paths.get(ClassLoader.getSystemResource(filename).toURI());
        byte[] contentZulu = Files.readAllBytes(pathOfZulu);
        String fileTypeZulu = "image/jpeg";

        adminGroupService.uploadGroupLogo(savedGroup.getId(), new MockMultipartFile(filenameZulu, filenameZulu, fileTypeZulu, contentZulu));

        assertEquals(1, groupLogoRepository.count());
        savedGroupLogo = groupRepository.findById(savedGroup.getId()).orElseThrow().getGroupLogo();
        assertNotNull(savedGroupLogo);
        assertArrayEquals(ArrayUtils.toObject(contentZulu), savedGroupLogo.getContent());
        assertEquals("zulu.jpg", savedGroupLogo.getFilename());
    }
}
