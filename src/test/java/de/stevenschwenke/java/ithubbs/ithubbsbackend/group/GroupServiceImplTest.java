package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
public class GroupServiceImplTest {

    @SpyBean
    private GroupServiceImpl groupService;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private GroupRepository groupRepository;

    @Test
    void daysPassedSinceFirstEventWillReturnZeroIfAGroupDidntOrganizeAnyEventsYet() {

        Group group = new Group();
        when(eventRepository.findFirstByGroupOrderByDatetimeAsc(group)).thenReturn(null);

        assertEquals(0, groupService.calculateDaysPassedSinceFirstKnownEvent(group));
    }

    @Test
    void daysPassedSinceFirstEventReturnsCorrectValue() {

        Group group = new Group();
        Event firstEvent = new Event();
        firstEvent.setDatetime(ZonedDateTime.now().minus(42, DAYS));
        when(eventRepository.findFirstByGroupOrderByDatetimeAsc(group)).thenReturn(firstEvent);

        assertEquals(42, groupService.calculateDaysPassedSinceFirstKnownEvent(group));
    }

    @Test
    void daysPassedSinceLastEventWillReturnZeroIfAGroupDidntOrganizeAnyEventsYet() {

        Group group = new Group();
        when(eventRepository.findFirstByGroupOrderByDatetimeAsc(group)).thenReturn(null);

        assertEquals(0, groupService.calculateDaysPassedSinceLastKnownEvent(group));
    }

    @Test
    void daysPassedSinceLastEventReturnsCorrectValue() {

        Group group = new Group();
        Event lastEvent = new Event();
        lastEvent.setDatetime(ZonedDateTime.now().minus(42, DAYS));
        when(eventRepository.findFirstByGroupOrderByDatetimeDesc(group)).thenReturn(lastEvent);

        assertEquals(42, groupService.calculateDaysPassedSinceLastKnownEvent(group));
    }

    @Test
    void averageNumberOfEventsIsZeroIfAGroupDidntOrganizeAnyEventsYet() {

        Group group = new Group();
        when(eventRepository.findFirstByGroupOrderByDatetimeAsc(group)).thenReturn(null);

        assertEquals(Double.valueOf(0), groupService.calculateAverageNumberOfEventsPerMonth(group));
    }

    @Test
    void averageNumberOfEventsReturnsCorrectValueForEventsInSameMonth() {

        doReturn(ZonedDateTime.of(2020, 1, 20, 1, 0, 0, 0, ZoneId.systemDefault())).when(groupService).now();
        Group group = new Group();
        Event event = new Event();
        event.setDatetime(ZonedDateTime.of(2020, 1, 1, 1, 0, 0, 0, ZoneId.systemDefault()));

        when(eventRepository.findFirstByGroupOrderByDatetimeAsc(group)).thenReturn(event);
        when(eventRepository.countAllByGroup(group)).thenReturn(2);

        assertEquals(Double.valueOf(2), groupService.calculateAverageNumberOfEventsPerMonth(group));
    }

    @Test
    void averageNumberOfEventsReturnsCorrectValueForEventsInDifferentMonths() {

        doReturn(ZonedDateTime.of(2020, 2, 20, 1, 0, 0, 0, ZoneId.systemDefault())).when(groupService).now();

        Group group = new Group();
        Event event = new Event();
        event.setDatetime(ZonedDateTime.of(2020, 1, 1, 1, 0, 0, 0, ZoneId.systemDefault()));

        when(eventRepository.findFirstByGroupOrderByDatetimeAsc(group)).thenReturn(event);
        when(eventRepository.countAllByGroup(group)).thenReturn(2);

        assertEquals(Double.valueOf(1), groupService.calculateAverageNumberOfEventsPerMonth(group));
    }

    @Test
    void groupStatisticsTest() {

        doReturn(2L).when(groupRepository).count();

        GroupStatistics groupStatistics = groupService.calculateGroupStatistics();

        assertEquals(Long.valueOf(2), groupStatistics.getTotalNumberOfGroups());
    }

}
