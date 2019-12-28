package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 1, max = 100)
    @NotNull
    private String name;

    @Size(min = 1, max = 255)
    @NotNull
    private String url;

    @Size(min = 1, max = 2000)
    @NotNull
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private GroupLogo groupLogo;

    public Group() {
    }

    public Group(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroupLogo getGroupLogo() {
        return groupLogo;
    }

    public void setGroupLogo(GroupLogo groupLogo) {
        this.groupLogo = groupLogo;
    }
}
