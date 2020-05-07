package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import org.springframework.hateoas.RepresentationModel;

public class EventModel extends RepresentationModel<EventModel> {

    private Long id;

    private long datetime;

    private String name;

    private String url;

    private Boolean generalPublic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
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

    public Boolean getGeneralPublic() {
        return generalPublic;
    }

    public void setGeneralPublic(Boolean generalPublic) {
        this.generalPublic = generalPublic;
    }
}
