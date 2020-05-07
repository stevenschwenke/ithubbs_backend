package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final EventRepository eventRepository;

    @Autowired
    public GroupServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Integer calculateTotalNumberOfEvents(Group group) {
        return eventRepository.countAllByGroup(group);
    }

    @Override
    public long calculateDaysPassedSinceFirstKnownEvent(Group group) {

        Event firstEvent = eventRepository.findFirstByGroupOrderByDatetimeAsc(group);

        if (firstEvent != null) {
            ZonedDateTime event = firstEvent.getDatetime();
            return event.until(now(), DAYS);
        }
        return 0;
    }

    @Override
    public long calculateDaysPassedSinceLastKnownEvent(Group group) {

        Event lastEvent = eventRepository.findFirstByGroupOrderByDatetimeDesc(group);

        if (lastEvent != null) {
            ZonedDateTime event = lastEvent.getDatetime();
            return event.until(now(), DAYS);
        }
        return 0;
    }

    @Override
    public Double calculateAverageNumberOfEventsPerMonth(Group group) {

        Event firstEvent = eventRepository.findFirstByGroupOrderByDatetimeAsc(group);

        if (firstEvent != null) {
            long monthsPassedSinceFirstEvent = firstEvent.getDatetime().until(now(), MONTHS);
            Integer numberOfTotalEvents = eventRepository.countAllByGroup(group);
            return (double) numberOfTotalEvents / (monthsPassedSinceFirstEvent + 1);
        }

        return (double) 0;
    }

    ZonedDateTime now() {
        return ZonedDateTime.now();
    }
}
