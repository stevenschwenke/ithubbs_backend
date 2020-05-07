package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class GroupModel extends RepresentationModel<GroupModel> {

    private Long id;
    private String name;
    private String url;
    private String description;
    private Integer totalNumberOfEvents;
    private Double averageNumberOfEventsPerMonth;
    private long daysPassedSinceFirstKnownEvent;
    private long daysPassedSinceLastKnownEvent;
}
