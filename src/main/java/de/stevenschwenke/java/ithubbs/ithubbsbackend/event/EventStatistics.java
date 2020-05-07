package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import lombok.Value;

@Value
public class EventStatistics {

    Long totalNumberOfEvents;
    Integer numberOfFutureEvents;
    Double averageNumberOfEventsPerMonth;

}
