package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.MONTHS;


@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventStatistics calculateEventStatistics() {

        long numberOfEvents = eventRepository.count();
        Integer numberOfFutureEvents = eventRepository.countAllByDatetimeAfter(now());

        ZonedDateTime datetimeOfEarliestKnownEvent = eventRepository.findTopByOrderByDatetimeAsc().getDatetime();

        ZonedDateTime datetimeOfLatestKnownEvent = eventRepository.findTopByOrderByDatetimeDesc().getDatetime();
        double averageNumberOfEventsPerMonth = (double) numberOfEvents / (double) (datetimeOfEarliestKnownEvent.until(datetimeOfLatestKnownEvent, MONTHS) + 1);

        return new EventStatistics(numberOfEvents, numberOfFutureEvents, round(averageNumberOfEventsPerMonth, 1));
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
