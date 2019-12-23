package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.authentication.TokenProvider;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupController;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogo;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GroupController.class)
@ActiveProfiles("junit")
public class GroupControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private GroupRepository groupRepository;

    @Test
    void requestAllGroupsWillReturnHTTP200AndAllGroups() throws Exception {

        this.mockMvc.perform(get("/api/groups")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("[]"));
    }

    @Test
    void requestAllGroupsWillReturnHTTP200AndAllGroupsIfGroupsExist() throws Exception {

        Group group = new Group("groupname", "groupurl", "groupdescription");
        group.setId(42L);
        when(groupRepository.findAll()).thenReturn(Collections.singletonList(group));

        this.mockMvc.perform(get("/api/groups")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect( jsonPath("$[0]['name']").value("groupname"))
                .andExpect( jsonPath("$[0]['url']").value("groupurl"))
                .andExpect( jsonPath("$[0]['description']").value("groupdescription"));
    }

    @Test
    void requestGroupLogoForGroupWithExistingLogoWillReturnHTTP200AndLogoAsContent() throws Exception {

        Group group = new Group("groupname", "groupurl", "groupdescription");
        group.setId(1L);
        group.setGroupLogo(new GroupLogo("filename", "png", new Byte[]{1,1,0}));
        when(groupRepository.findById(1L)).thenReturn(java.util.Optional.of(group));

        this.mockMvc.perform(get("/api/groups/1/logo")
                .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"))
                .andExpect(content().bytes(new byte[]{1,1,0}));
    }

    @Test
    void requestGroupLogoForGroupWithoutExistingLogoWillReturnHTTP200AndEmptyContent() throws Exception {

        Group group = new Group("groupname", "groupurl", "groupdescription");
        group.setId(1L);
        when(groupRepository.findById(1L)).thenReturn(java.util.Optional.of(group));

        this.mockMvc.perform(get("/api/groups/1/logo")
                .accept(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"))
                .andExpect(content().bytes(new byte[]{}));
    }
}
