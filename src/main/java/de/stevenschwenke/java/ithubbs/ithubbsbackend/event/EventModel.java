package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupModel;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;

public class EventModel extends RepresentationModel<GroupModel> {

    private Long id;

    private ZonedDateTime datetime;

    private String name;

    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

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
}
