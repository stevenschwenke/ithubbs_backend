package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.stereotype.Service;

@Service
public interface EventService {
    EventStatistics calculateEventStatistics();
}
