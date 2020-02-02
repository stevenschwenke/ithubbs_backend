package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @NotNull
    private ZonedDateTime datetime;

    @Size(min = 1, max = 100)
    @NotNull
    private String name;

    @Size(min = 1, max = 255)
    @NotNull
    private String url;

    private Boolean generalPublic;

    public Event() {
    }

    public Event(String name, ZonedDateTime datetime, String url, Boolean generalPublic) {
        this.datetime = datetime;
        this.name = name;
        this.url = url;
        this.generalPublic = generalPublic;
    }

    public ZonedDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
