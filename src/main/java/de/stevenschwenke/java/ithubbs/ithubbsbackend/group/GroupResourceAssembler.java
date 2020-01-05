package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

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

        return gr;
    }
}
