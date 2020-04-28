package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.authentication.TokenProvider;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("junit")
class EventControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private GroupRepository groupRepository;

    @Test
    void gettingAllEventsEvenWithoutAuthWillReturnEventsAndHTTP200() throws Exception {

        Group group = new Group("HackTalk", "https://www.hacktalk.de", "Cool community-event in Brunswick");
        group.setId(41L);
        Event eventWithGroup = new Event("event with group", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.of("Europe/Berlin")), "url", false);
        eventWithGroup.setId(42L);
        eventWithGroup.setGroup(group);
        Event eventWithoutGroup = new Event("event without group", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.of("Europe/Berlin")), "url", false);
        eventWithoutGroup.setId(43L);

        doReturn(Optional.of(group)).when(groupRepository).findById(41L);
        doReturn(List.of(eventWithGroup, eventWithoutGroup)).when(eventRepository).findAllWithDatetimeAfter(any());

        this.mockMvc.perform(get("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.eventModelList[0].id").isNumber())
                .andExpect(jsonPath("$._embedded.eventModelList[0].name").value("event with group"))
                .andExpect(jsonPath("$._embedded.eventModelList[0].url").value("url"))
                .andExpect(jsonPath("$._embedded.eventModelList[0].datetime").value("1577901600"))
                .andExpect(jsonPath("$._embedded.eventModelList[0]._links.group.href").value("http://localhost/api/groups/41"))
                .andExpect(jsonPath("$._embedded.eventModelList[1].id").isNumber())
                .andExpect(jsonPath("$._embedded.eventModelList[1].name").value("event without group"))
                .andExpect(jsonPath("$._embedded.eventModelList[1].url").value("url"))
                .andExpect(jsonPath("$._embedded.eventModelList[1].datetime").value("1577901600"));
    }
}
