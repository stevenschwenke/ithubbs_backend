package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
class GroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Test
    void groupsAreReturnedOrderedByName() {

        groupRepository.save(new Group("x", "URL", "Description"));
        groupRepository.save(new Group("a", "URL", "Description"));
        groupRepository.save(new Group("b", "URL", "Description"));

        List<Group> allByOrderByNameAsc = groupRepository.findAllByOrderByNameAsc();

        assertEquals("a", allByOrderByNameAsc.get(0).getName());
        assertEquals("b", allByOrderByNameAsc.get(1).getName());
        assertEquals("x", allByOrderByNameAsc.get(2).getName());
    }

    @Test
    void persistingGroupWithNullNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group(null, "URL", "Description"));
            groupRepository.findAllByOrderByNameAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithEmptyNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("", "URL", "Description"));
            groupRepository.findAllByOrderByNameAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithTooLongNameThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", "URL", "Description"));
            groupRepository.findAllByOrderByNameAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("name", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithNullURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", null, "description"));
            groupRepository.findAllByOrderByNameAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithEmptyURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "", "description"));
            groupRepository.findAllByOrderByNameAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongURLThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!", "description"));
            groupRepository.findAllByOrderByNameAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("url", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithNullDescriptionThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "url", null));
            groupRepository.findAllByOrderByNameAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("description", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingGroupWithEmptyDescriptionThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "url", ""));
            groupRepository.findAllByOrderByNameAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("description", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }

    @Test
    void persistingEventWithTooLongDescriptionThrowsException() {

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {

            groupRepository.save(new Group("name", "url", "Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name! Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!Way too long name, Way too long name, Way too long name, Way too long   name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name, Way too long name!"));
            groupRepository.findAllByOrderByNameAsc();
        });
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("description", exception.getConstraintViolations().iterator().next().getPropertyPath().toString());
    }
}
