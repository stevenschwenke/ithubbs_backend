package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime datetime;

    private String name;

    private String url;

    public Event() {
    }

    public Event(LocalDateTime date, String name, String url) {
        this.datetime = date;
        this.name = name;
        this.url = url;
    }

    public LocalDateTime getDate() {
        return datetime;
    }

    public void setDate(LocalDateTime datetime) {
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
}
