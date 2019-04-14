package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

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
        assertEquals(validGroup.getId(), response.getBody());
    }

    @Test
    void creatingInvalidGroupWillReturnHTTP422() {

        Group emptyInvalidGroup = new Group();

        ResponseEntity<?> response = adminGroupController.createNewGroup(emptyInvalidGroup);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void creatingValidGroupEditWillReturnHTTP200() {

        AdminGroupController adminGroupController = new AdminGroupController(null, Mockito.mock(AdminGroupService.class));

        ResponseEntity<?> response = adminGroupController.editGroup(new Group());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void creatingInvalidGroupEditWillReturnHTTP422() {

        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);
        Mockito.doThrow(IllegalArgumentException.class).when(adminGroupServiceMock).editGroup(any());

        AdminGroupController adminGroupController = new AdminGroupController(null, adminGroupServiceMock);

        ResponseEntity<?> response = adminGroupController.editGroup(new Group());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void creatingValidGroupDeletionWillReturnHTTP200() {

        AdminGroupController adminGroupController = new AdminGroupController(null, Mockito.mock(AdminGroupService.class));

        ResponseEntity<?> response = adminGroupController.deleteGroup(new Group());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void creatingInvalidGroupDeletionWillReturnHTTP422() {

        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);
        Mockito.doThrow(IllegalArgumentException.class).when(adminGroupServiceMock).deleteGroup(any());

        AdminGroupController adminGroupController = new AdminGroupController(null, adminGroupServiceMock);

        ResponseEntity<?> response = adminGroupController.deleteGroup(new Group());

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
