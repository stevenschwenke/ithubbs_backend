package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class GroupResourceAssembler extends RepresentationModelAssemblerSupport<Group, GroupModel> {

    public GroupResourceAssembler(Class<?> controllerClass, Class<GroupModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public GroupModel toModel(Group entity) {
        GroupModel gr = super.createModelWithId(entity.getId(), entity);
        gr.setId(entity.getId());
        gr.setName(entity.getName());
        gr.setUrl(entity.getUrl());
        gr.setDescription(entity.getDescription());

        if (entity.getGroupLogo() != null) {
            Link linkToImage = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GroupController.class).logoOfGroup(entity.getId())).withRel("image");
            gr.add(linkToImage);
        }

        return gr;
    }
}
