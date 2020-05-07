package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final EventRepository eventRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(EventRepository eventRepository, GroupRepository groupRepository) {
        this.eventRepository = eventRepository;
        this.groupRepository = groupRepository;
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

    @Override
    public GroupStatistics calculateGroupStatistics() {

        Long totalNumberOfGroups = groupRepository.count();

        return new GroupStatistics(totalNumberOfGroups);
    }

    /**
     * Round a double value as proposed in https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
     *
     * @param value  to round
     * @param places to round to
     * @return rounded value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    ZonedDateTime now() {
        return ZonedDateTime.now();
    }
}
