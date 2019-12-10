package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("junit")
class AdminGroupControllerTest {

    @Autowired
    private AdminGroupController adminGroupController;

    @Test
    void creatingValidGroupWillReturnHTTP200() {

        Group validGroup = new Group("name", "url", "description");

        ResponseEntity<?> response = adminGroupController.createNewGroup(validGroup);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validGroup, response.getBody());
    }

    @Test
    void creatingInvalidGroupWillReturnHTTP422() {

        Group emptyInvalidGroup = new Group();

        ResponseEntity<?> response = adminGroupController.createNewGroup(emptyInvalidGroup);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void creatingValidGroupEditWillReturnHTTP200() {

        Group savedGroup = new Group("name", "url", "description");
        ResponseEntity<?> response = adminGroupController.createNewGroup(savedGroup);
        Group returnedGroup = (Group) response.getBody();

        ResponseEntity<?> response2 = adminGroupController.editGroup(returnedGroup);

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(returnedGroup, response.getBody());
    }

    @Test
    void creatingInvalidGroupEditWillReturnHTTP422() {

        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);
        Mockito.doThrow(IllegalArgumentException.class).when(adminGroupServiceMock).editGroup(any());

        AdminGroupController adminGroupController = new AdminGroupController(adminGroupServiceMock);

        ResponseEntity<?> response = adminGroupController.editGroup(new Group());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void creatingValidGroupDeletionWillReturnHTTP200() {

        AdminGroupController adminGroupController = new AdminGroupController(Mockito.mock(AdminGroupService.class));

        ResponseEntity<?> response = adminGroupController.deleteGroup(new Group());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void creatingInvalidGroupDeletionWillReturnHTTP422() {

        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);
        Mockito.doThrow(IllegalArgumentException.class).when(adminGroupServiceMock).deleteGroup(any());

        AdminGroupController adminGroupController = new AdminGroupController(adminGroupServiceMock);

        ResponseEntity<?> response = adminGroupController.deleteGroup(new Group());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void uploadingGroupLogoForExistingGroupWithoutErrorWillReturnHTTP200() {

        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);

        AdminGroupController adminGroupController = new AdminGroupController(adminGroupServiceMock);

        ResponseEntity<?> response = adminGroupController.uploadGroupLogo(42L, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void uploadingGroupLogoForNonExistingGroupWillReturnHTTP422() throws IOException, GroupNotFoundException {

        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);
        Mockito.doThrow(GroupNotFoundException.class).when(adminGroupServiceMock).uploadGroupLogo(42L, null);

        AdminGroupController adminGroupController = new AdminGroupController(adminGroupServiceMock);

        ResponseEntity<?> response = adminGroupController.uploadGroupLogo(42L, null);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
