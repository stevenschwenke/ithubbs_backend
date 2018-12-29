package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

    @Test
    void creatingInvalidGroupWillReturnHTTP422() {

        Group emptyInvalidGroup = new Group();

        ResponseEntity<?> response = adminGroupController.createNewGroup(emptyInvalidGroup);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}