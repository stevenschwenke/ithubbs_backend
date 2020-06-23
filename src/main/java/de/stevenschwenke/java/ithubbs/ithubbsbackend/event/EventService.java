package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    EventStatistics calculateEventStatistics();

    /**
     * @return list of years in which events took place
     */
    List<Integer> retrieveYearsWithEvents();
}
