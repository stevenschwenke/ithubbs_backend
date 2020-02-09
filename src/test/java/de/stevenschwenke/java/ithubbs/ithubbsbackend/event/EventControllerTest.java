package de.stevenschwenke.java.ithubbs.ithubbsbackend.event;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.authentication.TokenProvider;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

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

    @Test
    void gettingAllEventsEvenWithoutAuthWillReturnEventsAndHTTP200() throws Exception {

        Event event = new Event("name", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.of("Europe/Berlin")), "url", false);
        event.setId(42L);
        doReturn(List.of(event)).when(eventRepository).findAllWithDatetimeAfter(any());

        this.mockMvc.perform(get("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0]['id']").isNumber())
                .andExpect(jsonPath("$[0]['name']").value("name"))
                .andExpect(jsonPath("$[0]['url']").value("url"))
                .andExpect(jsonPath("$[0]['datetime']").value("1577901600"));
    }
}
