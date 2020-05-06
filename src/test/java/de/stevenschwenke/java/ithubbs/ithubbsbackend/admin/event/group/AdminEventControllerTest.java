package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.event.AdminEventService;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.authentication.TokenProvider;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.Event;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.user.User;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("junit")
class AdminEventControllerTest {

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
    private AdminEventService adminEventService;

    @Test
    void gettingAllEventsViaAdminEndpointWithoutProperAuthWillReturnHTTP403() throws Exception {

        this.mockMvc.perform(post("/api/admin/events")
                .header("Authorization", "my fake JWT")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void gettingAllEventsViaAdminEndpointWillReturnEventsAndHTTP200() throws Exception {

        Event event = new Event("name", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.of("Europe/Berlin")), "url", false);
        event.setId(42L);
        doReturn(List.of(event)).when(eventRepository).findAllByOrderByDatetimeAsc();

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(get("/api/admin/events")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.eventModelList[0].id").isNumber())
                .andExpect(jsonPath("$._embedded.eventModelList[0].name").value("name"))
                .andExpect(jsonPath("$._embedded.eventModelList[0].url").value("url"))
                .andExpect(jsonPath("$._embedded.eventModelList[0].datetime").value("1577901600"));
    }

    @Test
    void creatingValidEventWithoutProperAuthWillReturnHTTP403() throws Exception {

        this.mockMvc.perform(post("/api/admin/events")
                .header("Authorization", "my fake JWT")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new Event("name", ZonedDateTime.now(), "url", false)))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void creatingInvalidEventWillReturnHTTP422() throws Exception {

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(post("/api/admin/events")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new Event()))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void creatingValidEventWillReturnHTTP201() throws Exception {

        Event savedEventWithID = new Event("name", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.systemDefault()), "url", false);
        savedEventWithID.setId(42L);
        doReturn(savedEventWithID).when(adminEventService).saveNewEvent(any());

        Event event = new Event("name", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.systemDefault()), "url", false);

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(post("/api/admin/events")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(event))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void creatingValidEventEditWithoutProperAuthWillReturnHTTP403() throws Exception {

        this.mockMvc.perform(post("/api/admin/events")
                .header("Authorization", "my fake JWT")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new Event("name", ZonedDateTime.now(), "url", false)))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void editingValidEventEditWillReturnHTTP200() throws Exception {

        doNothing().when(adminEventService).editEvent(any());

        Event event = new Event("name", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.systemDefault()), "url", false);
        event.setId(42L);

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(post("/api/admin/events")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(event))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void creatingInvalidEventEditWillReturnHTTP422() throws Exception {

        doThrow(RuntimeException.class).when(adminEventService).editEvent(any());

        Event event = new Event("name", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.systemDefault()), "url", false);

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(post("/api/admin/events")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(event))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void creatingValidEventDeletionWithoutProperAuthWillReturnHTTP403() throws Exception {

        this.mockMvc.perform(post("/api/admin/delete")
                .header("Authorization", "my fake JWT")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new Event("name", ZonedDateTime.now(), "url", false)))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void creatingValidEventDeletionWillReturnHTTP200() throws Exception {

        doNothing().when(adminEventService).deleteEvent(any());

        Event event = new Event("name", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.systemDefault()), "url", false);
        event.setId(42L);

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(delete("/api/admin/events")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(event))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void creatingInvalidEventDeletionWillReturnHTTP422() throws Exception {

        doThrow(RuntimeException.class).when(adminEventService).deleteEvent(any());

        Event event = new Event("name", ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 19, 0), ZoneId.systemDefault()), "url", false);

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(delete("/api/admin/events")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(event))
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    /**
     * @return JSON Web Token for user "steven" with password "steven", which has been registered in Spring Security.
     */
    private String registerUserAndReturnJWT() {
        doReturn(Optional.of(new User("steven", new BCryptPasswordEncoder().encode("steven")))).when(userRepository).findUserByUsername("steven");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("steven", "steven", grantedAuthorities);

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(authentication);
    }
}
