package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import org.springframework.hateoas.RepresentationModel;

import java.net.URI;

public class GroupModel extends RepresentationModel<GroupModel> {

    private Long id;
    private String name;
    private String url;
    private String description;

    private URI imageURI;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getImageURI() {
        return imageURI;
    }

    public void setImageURI(URI imageURI) {
        this.imageURI = imageURI;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
