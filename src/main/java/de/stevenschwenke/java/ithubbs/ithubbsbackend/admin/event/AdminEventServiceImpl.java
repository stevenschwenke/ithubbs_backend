package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository adminEventRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public AdminEventServiceImpl(EventRepository adminEventRepository, GroupRepository groupRepository) {
        this.adminEventRepository = adminEventRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public Event saveNewEvent(EventUpdateDTO eventUpdateDTO) {
        Event eventEntity = new Event(eventUpdateDTO.getName(), eventUpdateDTO.getDatetime(), eventUpdateDTO.getUrl(), eventUpdateDTO.getGeneralPublic());
        eventEntity.setGroup(eventUpdateDTO.getGroupID() == null ? null : groupRepository.findById(eventUpdateDTO.getGroupID()).orElseThrow());
        return adminEventRepository.save(eventEntity);
    }

    @Override
    public void editEvent(EventUpdateDTO eventUpdateDTO) {

        Event changedEvent = adminEventRepository.findById(eventUpdateDTO.getId()).orElseThrow();
        changedEvent.setName(eventUpdateDTO.getName());
        changedEvent.setUrl(eventUpdateDTO.getUrl());
        changedEvent.setDatetime(eventUpdateDTO.getDatetime());
        changedEvent.setGroup(eventUpdateDTO.getGroupID() == null ? null : groupRepository.findById(eventUpdateDTO.getGroupID()).orElseThrow());
        adminEventRepository.save(changedEvent);
    }

    @Override
    public void deleteEvent(Event event) {

        adminEventRepository.delete(event);
    }
}
