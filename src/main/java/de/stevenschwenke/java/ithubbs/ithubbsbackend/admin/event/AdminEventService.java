package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import org.springframework.stereotype.Service;

@Service
public interface AdminEventService {

    void editEvent(Event newValue);

    void deleteEvent(Event newValue);
}
