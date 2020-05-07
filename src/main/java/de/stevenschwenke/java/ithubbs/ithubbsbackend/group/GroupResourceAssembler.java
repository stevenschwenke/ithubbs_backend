package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class GroupResourceAssembler extends RepresentationModelAssemblerSupport<Group, GroupModel> {

    private final GroupService groupService;

    public GroupResourceAssembler(Class<?> controllerClass, Class<GroupModel> resourceType, GroupService groupService) {
        super(controllerClass, resourceType);
        this.groupService = groupService;
    }

    @Override
    public GroupModel toModel(Group entity) {
        GroupModel gr = super.createModelWithId(entity.getId(), entity);
        gr.setId(entity.getId());
        gr.setName(entity.getName());
        gr.setUrl(entity.getUrl());
        gr.setDescription(entity.getDescription());
        gr.setTotalNumberOfEvents(groupService.calculateTotalNumberOfEvents(entity));
        gr.setAverageNumberOfEventsPerMonth(groupService.calculateAverageNumberOfEventsPerMonth(entity));
        gr.setDaysPassedSinceFirstKnownEvent(groupService.calculateDaysPassedSinceFirstKnownEvent(entity));
        gr.setDaysPassedSinceLastKnownEvent(groupService.calculateDaysPassedSinceLastKnownEvent(entity));

        if (entity.getGroupLogo() != null) {
            Link linkToImage = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).logoOfGroup(entity.getId())).withRel("image");
            gr.add(linkToImage);
        }

        return gr;
    }
}
