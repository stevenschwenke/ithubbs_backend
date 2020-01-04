package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.authentication.TokenProvider;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogo;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.user.User;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.user.UserRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("junit")
class AdminGroupControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AdminGroupService adminGroupService;

    @Test
    void creatingValidGroupWithoutProperAuthWillReturnHTTP403() throws Exception {

        this.mockMvc.perform(post("/api/admin/groups")
                .header("Authorization", "my fake JWT")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new Group("name", "url", "description")))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void creatingValidGroupWillReturnHTTP200() throws Exception {

        Group requestGroup = new Group("name", "url", "description");
        Group savedGroup = new Group("name", "url", "description");
        savedGroup.setId(42L);
        doReturn(savedGroup).when(adminGroupService).createNewGroup(any());

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(post("/api/admin/groups")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$['id']").isNumber())
                .andExpect(jsonPath("$['name']").value("name"))
                .andExpect(jsonPath("$['url']").value("url"))
                .andExpect(jsonPath("$['description']").value("description"))
                .andExpect(jsonPath("$['imageURI']").isEmpty());
    }

    @Test
    void creatingInvalidGroupWillReturnHTTP422() throws Exception {

        doThrow(RuntimeException.class).when(adminGroupService).createNewGroup(any());

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(post("/api/admin/groups")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new Group()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void editingExistingGroupWithValidDataWillReturnEditedGroupWithLogoInformation() throws Exception {

        Group requestGroup = new Group("new name", "new url", "new description");
        requestGroup.setId(42L);

        Group savedGroup = new Group("new name", "new url", "new description");
        savedGroup.setId(42L);
        String filename = "spock.jpg";
        Path pathOfSpock = Paths.get(ClassLoader.getSystemResource(filename).toURI());
        byte[] content = Files.readAllBytes(pathOfSpock);
        savedGroup.setGroupLogo(new GroupLogo("spock.jpg", "image/jpeg", ArrayUtils.toObject(content)));
        doReturn(savedGroup).when(adminGroupService).editGroup(any());

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(post("/api/admin/groups/edit")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$['id']").isNumber())
                .andExpect(jsonPath("$['name']").value("new name"))
                .andExpect(jsonPath("$['url']").value("new url"))
                .andExpect(jsonPath("$['description']").value("new description"))
                .andExpect(jsonPath("$['imageURI']").isNotEmpty());
    }


    @Test
    void uploadingGroupLogoForExistingGroupWithoutErrorWillReturnHTTP201() throws Exception {

        doNothing().when(adminGroupService).uploadGroupLogo(any(), any());

        String filename = "spock.jpg";
        Path pathOfSpock = Paths.get(ClassLoader.getSystemResource(filename).toURI());
        byte[] content = Files.readAllBytes(pathOfSpock);
        String fileType = "image/jpeg";

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/admin/groups/logo")
                .file(new MockMultipartFile("file", filename, fileType, content))
                .param("groupID", "42")
                .header("Authorization", jwt)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['logoURI']").isNotEmpty());
    }

    @Test
    void uploadingGroupLogoForNonExistingGroupWillReturnHTTP422() throws Exception {

        String filename = "spock.jpg";
        Path pathOfSpock = Paths.get(ClassLoader.getSystemResource(filename).toURI());
        byte[] content = Files.readAllBytes(pathOfSpock);
        String fileType = "image/jpeg";

        doThrow(GroupNotFoundException.class).when(adminGroupService).uploadGroupLogo(any(), any());

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/admin/groups/logo")
                .file(new MockMultipartFile("file", filename, fileType, content))
                .param("groupID", "4234")
                .header("Authorization", jwt)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$['logoURI']").doesNotExist());
    }

    @Test
    void creatingValidGroupDeletionWillReturnHTTP200() throws Exception {

        doNothing().when(adminGroupService).deleteGroup(any());

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(delete("/api/admin/groups/delete")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new Group()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void creatingInvalidGroupDeletionWillReturnHTTP422() throws Exception {

        doThrow(RuntimeException.class).when(adminGroupService).deleteGroup(any());

        String jwt = registerUserAndReturnJWT();

        this.mockMvc.perform(delete("/api/admin/groups/delete")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new Group()))
                .accept(MediaType.APPLICATION_JSON))
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
