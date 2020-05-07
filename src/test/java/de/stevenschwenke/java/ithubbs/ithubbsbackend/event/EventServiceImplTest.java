package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("junit")
@Transactional
public class EventServiceImplTest {

    @SpyBean
    private EventServiceImpl eventService;
    @Autowired
    private EventRepository eventRepository;

    @Test
    void eventStatisticsTest() {

        ZonedDateTime baseDateTime = ZonedDateTime.of(2020,1,4,1,0,0,0, ZoneId.systemDefault());
        when(eventService.now()).thenReturn(baseDateTime.minusDays(1));

        Event eventAtBasetime = new Event("eventAtBasetime", baseDateTime, "uri", true);
        Event eventOneDayAfter = new Event("eventOneDayAfter", baseDateTime.plusDays(1), "uri", true);
        Event eventOneMonthAfter = new Event("eventOneMonthAfter", baseDateTime.plusMonths(1), "uri", true);
        Event eventOneMonthAndOneDayAfter = new Event("eventOneMonthAndOneDayAfter", baseDateTime.plusMonths(1).plusDays(1), "uri", true);

        eventRepository.save(eventAtBasetime);
        eventRepository.save(eventOneDayAfter);
        eventRepository.save(eventOneMonthAfter);
        eventRepository.save(eventOneMonthAndOneDayAfter);

        EventStatistics eventStatistics = eventService.calculateEventStatistics();

        assertEquals(Double.valueOf(2), eventStatistics.getAverageNumberOfEventsPerMonth());
        assertEquals(Long.valueOf(4), eventStatistics.getTotalNumberOfEvents());
        assertEquals(Integer.valueOf(4), eventStatistics.getNumberOfFutureEvents());
    }
}
