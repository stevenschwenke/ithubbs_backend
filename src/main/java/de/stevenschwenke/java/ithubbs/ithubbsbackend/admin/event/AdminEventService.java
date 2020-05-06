package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import org.springframework.stereotype.Service;

@Service
public interface AdminEventService {

    Event saveNewEvent(EventUpdateDTO eventUpdateDTO);

    void editEvent(EventUpdateDTO eventUpdateDTO);

    void deleteEvent(Event newValue);
}
