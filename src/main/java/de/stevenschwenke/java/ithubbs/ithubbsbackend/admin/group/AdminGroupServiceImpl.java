package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogo;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogoRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
@Transactional
public class AdminGroupServiceImpl implements AdminGroupService {

    private final AdminGroupRepository adminGroupRepository;

    private final GroupLogoRepository groupLogoRepository;

    private final Tika tika = new Tika();

    @Autowired
    public AdminGroupServiceImpl(AdminGroupRepository adminGroupRepository, GroupLogoRepository groupLogoRepository) {
        this.adminGroupRepository = adminGroupRepository;
        this.groupLogoRepository = groupLogoRepository;
    }

    @Override
    public Group createNewGroup(@RequestBody Group group) {
        Group savedGroup = adminGroupRepository.save(group);
        savedGroup.setImageURI("http://localhost:8090/ithubbs/api/groups/" + savedGroup.getId() + "/logo");
        return savedGroup;
    }

    @Override
    public Group editGroup(Group newValue) {

        Group changedGroup = adminGroupRepository.findById(newValue.getId()).orElseThrow();
        changedGroup.setName(newValue.getName());
        changedGroup.setUrl(newValue.getUrl());
        changedGroup.setDescription(newValue.getDescription());
        Group savedGroup = adminGroupRepository.save(newValue);

        savedGroup.setImageURI("http://localhost:8090/ithubbs/api/groups/" + savedGroup.getId() + "/logo");

        return savedGroup;
    }

    @Override
    public void deleteGroup(Group group) {

        adminGroupRepository.delete(group);
    }

    @Override
    public String uploadGroupLogo(Long groupID, MultipartFile file) throws IOException, GroupNotFoundException {

        Group group = adminGroupRepository.findById(groupID).orElseThrow(() -> new GroupNotFoundException("Group not found."));

        String originalFilename = file.getOriginalFilename();
        Path fileAsPath = multipartFileToPath(file);
        saveLogo(fileAsPath, originalFilename, group);

        return "http://localhost:8090/ithubbs/api/groups/" + groupID + "/logo";
    }

    public void saveLogo(Path fileAsPath, String originalFilename, Group group) throws IOException {
        GroupLogo groupLogo = groupLogoRepository.save(new GroupLogo(originalFilename, tika.detect(fileAsPath), ArrayUtils.toObject(Files.readAllBytes(fileAsPath))));
        group.setGroupLogo(groupLogo);
        this.adminGroupRepository.save(group);
    }

    static Path multipartFileToPath(MultipartFile multipartFile) throws IOException {

        File file = File.createTempFile(Objects.requireNonNull(multipartFile.getOriginalFilename()), "");
        multipartFile.transferTo(file);
        return file.toPath();
    }

}
