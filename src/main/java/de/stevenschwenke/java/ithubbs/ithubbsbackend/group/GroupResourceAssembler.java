package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        gr.setAverageNumberOfEventsPerMonth(round(groupService.calculateAverageNumberOfEventsPerMonth(entity), 1));
        gr.setDaysPassedSinceFirstKnownEvent(groupService.calculateDaysPassedSinceFirstKnownEvent(entity));
        gr.setDaysPassedSinceLastKnownEvent(groupService.calculateDaysPassedSinceLastKnownEvent(entity));

        if (entity.getGroupLogo() != null) {
            Link linkToImage = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).logoOfGroup(entity.getId())).withRel("image");
            gr.add(linkToImage);
        }

        return gr;
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
}
