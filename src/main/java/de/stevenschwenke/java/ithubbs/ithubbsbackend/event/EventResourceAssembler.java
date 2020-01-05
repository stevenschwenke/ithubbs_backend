package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class EventResourceAssembler extends RepresentationModelAssemblerSupport<Event, EventModel> {

    public EventResourceAssembler(Class<?> controllerClass, Class<EventModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public EventModel toModel(Event entity) {
        EventModel gr = super.createModelWithId(entity.getId(), entity);
        gr.setId(entity.getId());
        gr.setDatetime(entity.getDatetime());
        gr.setName(entity.getName());
        gr.setUrl(entity.getUrl());

        return gr;
    }
}
