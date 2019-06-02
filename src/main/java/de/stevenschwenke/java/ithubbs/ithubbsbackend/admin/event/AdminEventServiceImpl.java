package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminEventServiceImpl implements AdminEventService {

    private final AdminEventRepository adminEventRepository;

    @Autowired
    public AdminEventServiceImpl(AdminEventRepository adminEventRepository) {
        this.adminEventRepository = adminEventRepository;
    }

    @Override
    public void editEvent(Event newValue) {

        Event changedEvent = adminEventRepository.findById(newValue.getId()).orElseThrow();
        changedEvent.setName(newValue.getName());
        changedEvent.setUrl(newValue.getUrl());
        changedEvent.setDatetime(newValue.getDatetime());
        adminEventRepository.save(newValue);
    }

    @Override
    public void deleteEvent(Event event) {

        adminEventRepository.delete(event);
    }
}
