package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
class AdminGroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Test
    void persistingGroupWithNullNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group(null, "URL", "Description"));
            groupRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithEmptyNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("", "URL", "Description"));
            groupRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithTooLongNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", "URL", "Description"));
            groupRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithNullURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", null, "description"));
            groupRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithEmptyURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "", "description"));
            groupRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", "description"));
            groupRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithNullDescriptionThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "url", null));
            groupRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("description", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithEmptyDescriptionThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "url", ""));
            groupRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("description", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongDescriptionThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "url", "Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name! Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!"));
            groupRepository.findAll();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("description", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }
}