package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.authentication.TokenProvider;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.user.User;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.user.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(GroupController.class)
// TODO Wenn unten alles läuft, geht das auch nur mit @WebMvcTest anstelle von @SpringBootTest und @AutoConfigureMockMvc?
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("junit")
class AdminGroupControllerTest {


    @Autowired
    MockMvc mockMvc;

//    @MockBean
//    private UserDetailsService userDetailsService;
//    @MockBean
//    private TokenProvider tokenProvider;
    @MockBean
    private GroupRepository groupRepository;

    @Autowired
    private  TokenProvider tokenProvider;
@Autowired
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;
//    @Autowired
//    private AdminGroupController adminGroupController;


    // TODO Test aus Service, muss hier nun auf Controller-Ebene getestet werden:
//    @Test
//    void creatingNewGroupWillAddImageURIToReturnedGroup() {
//
//        Group newGroup = new Group("new Group", "url", "description");
//
//        Group savedGroup = adminGroupService.createNewGroup(newGroup);
//
//        assertEquals("http://localhost:8090/ithubbs/api/groups/" + savedGroup.getId() + "/logo", savedGroup.getImageURI());
//    }
//    @Test
//    void editingExistingGroupWithValidDataWillReturnEditedGroupWithLogoInformation() throws IOException, URISyntaxException, GroupNotFoundException {
//
//        Group savedGroup = groupRepository.save(new Group("name", "url", "description"));
//        String filename = "spock.jpg";
//        Path pathOfSpock = Paths.get(ClassLoader.getSystemResource(filename).toURI());
//        byte[] content = Files.readAllBytes(pathOfSpock);
//        String fileType = "image/jpeg";
//        adminGroupService.uploadGroupLogo(savedGroup.getId(), new MockMultipartFile(filename, filename, fileType, content));
//
//        Group validGroup = new Group("new name", "new url", "new description");
//        validGroup.setId(savedGroup.getId());
//
//        Group editedGroup = adminGroupService.editGroup(validGroup);
//
//        assertEquals("http://localhost:8090/ithubbs/api/groups/" + savedGroup.getId() + "/logo", editedGroup.getImageURI());
//    }

    @Test
    void creatingValidGroupWillReturnHTTP200() throws Exception {

        when(userRepository.findUserByUsername("steven")).thenReturn(Optional.of(new User("steven", "steven")));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("steven", "steven");

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);


        this.mockMvc.perform(post("/api/admin/groups")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"name\", \"url\":\"url\"")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("[]"));

//        ResponseEntity<?> response = adminGroupController.createNewGroup(validGroup);

//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(validGroup, response.getBody());
    }


    // TODO Sobald der Gruppen-Erstellungstest oben funktioniert, ohne Auth versuchen, eine Gruppe zu erstellen. Erwartung: HTTP-403.

    // TODO Aus Stackoverflow: For a missing JWT, however, APIs should return 401 Unauthorized, not 403 Forbidden. Hence mvc.perform(MockMvcRequestBuilders.get("/test")).andExpect(status().isUnauthorized()); would be more appropriate. – Andrea Ligios Nov 25 at 11:40

//    @Test
//    void creatingInvalidGroupWillReturnHTTP422() {
//
//        Group emptyInvalidGroup = new Group();
//
//        ResponseEntity<?> response = adminGroupController.createNewGroup(emptyInvalidGroup);
//
//        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
//    }
//
//    @Test
//    void creatingValidGroupEditWillReturnHTTP200() {
//
//        Group savedGroup = new Group("name", "url", "description");
//        ResponseEntity<?> response = adminGroupController.createNewGroup(savedGroup);
//        Group returnedGroup = (Group) response.getBody();
//
//        ResponseEntity<?> response2 = adminGroupController.editGroup(returnedGroup);
//
//        assertEquals(HttpStatus.OK, response2.getStatusCode());
//        assertEquals(returnedGroup, response.getBody());
//    }
//
//    @Test
//    void creatingInvalidGroupEditWillReturnHTTP422() {
//
//        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);
//        Mockito.doThrow(IllegalArgumentException.class).when(adminGroupServiceMock).editGroup(any());
//
//        AdminGroupController adminGroupController = new AdminGroupController(adminGroupServiceMock);
//
//        ResponseEntity<?> response = adminGroupController.editGroup(new Group());
//
//        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
//    }
//
//    @Test
//    void creatingValidGroupDeletionWillReturnHTTP200() {
//
//        AdminGroupController adminGroupController = new AdminGroupController(Mockito.mock(AdminGroupService.class));
//
//        ResponseEntity<?> response = adminGroupController.deleteGroup(new Group());
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void creatingInvalidGroupDeletionWillReturnHTTP422() {
//
//        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);
//        Mockito.doThrow(IllegalArgumentException.class).when(adminGroupServiceMock).deleteGroup(any());
//
//        AdminGroupController adminGroupController = new AdminGroupController(adminGroupServiceMock);
//
//        ResponseEntity<?> response = adminGroupController.deleteGroup(new Group());
//
//        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
//    }
//
//    @Test
//    void uploadingGroupLogoForExistingGroupWithoutErrorWillReturnHTTP200() {
//
//        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);
//
//        AdminGroupController adminGroupController = new AdminGroupController(adminGroupServiceMock);
//
//        ResponseEntity<?> response = adminGroupController.uploadGroupLogo(42L, null);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//    }
//
//    @Test
//    void uploadingGroupLogoForNonExistingGroupWillReturnHTTP422() throws IOException, GroupNotFoundException {
//
//        AdminGroupService adminGroupServiceMock = Mockito.mock(AdminGroupService.class);
//        Mockito.doThrow(GroupNotFoundException.class).when(adminGroupServiceMock).uploadGroupLogo(42L, null);
//
//        AdminGroupController adminGroupController = new AdminGroupController(adminGroupServiceMock);
//
//        ResponseEntity<?> response = adminGroupController.uploadGroupLogo(42L, null);
//
//        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
//    }
}
