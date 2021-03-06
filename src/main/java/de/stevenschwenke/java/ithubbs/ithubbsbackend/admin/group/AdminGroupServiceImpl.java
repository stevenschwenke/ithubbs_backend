package de.stevenschwenke.java.ithubbs.ithubbsbackend.admin.group;

import de.stevenschwenke.java.ithubbs.ithubbsbackend.event.EventRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.Group;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogo;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupLogoRepository;
import de.stevenschwenke.java.ithubbs.ithubbsbackend.group.GroupRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
@Transactional
public class AdminGroupServiceImpl implements AdminGroupService {

    private final GroupRepository groupRepository;
    private final EventRepository eventRepository;

    private final GroupLogoRepository groupLogoRepository;

    private final Tika tika = new Tika();

    @Autowired
    public AdminGroupServiceImpl(GroupRepository groupRepository, EventRepository eventRepository, GroupLogoRepository groupLogoRepository) {
        this.groupRepository = groupRepository;
        this.eventRepository = eventRepository;
        this.groupLogoRepository = groupLogoRepository;
    }

    @Override
    public Group createNewGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group editGroup(Group newValue) {

        Group changedGroup = groupRepository.findById(newValue.getId()).orElseThrow();
        changedGroup.setName(newValue.getName());
        changedGroup.setUrl(newValue.getUrl());
        changedGroup.setDescription(newValue.getDescription());
        return groupRepository.save(changedGroup);
    }

    @Override
    public void deleteGroup(Group group) {

        Group groupFromDatabase = groupRepository.findById(group.getId()).orElseThrow();

        if(eventRepository.countAllByGroup(groupFromDatabase) > 0) {
            throw new IllegalArgumentException("Only groups with no events can be deleted");
        } else {
            groupRepository.deleteById(group.getId());
        }
    }

    @Override
    public void uploadGroupLogo(Long groupID, MultipartFile file) throws IOException, GroupNotFoundException {

        Group group = groupRepository.findById(groupID).orElseThrow(() -> new GroupNotFoundException("Group not found."));

        String originalFilename = file.getOriginalFilename();
        Path fileAsPath = multipartFileToPath(file);
        if (group.getGroupLogo() != null) {
            groupLogoRepository.delete(group.getGroupLogo());
        }
        saveLogo(fileAsPath, originalFilename, group);
    }

    public void saveLogo(Path fileAsPath, String originalFilename, Group group) throws IOException {
        GroupLogo groupLogo = groupLogoRepository.save(new GroupLogo(originalFilename, tika.detect(fileAsPath), ArrayUtils.toObject(Files.readAllBytes(fileAsPath))));
        group.setGroupLogo(groupLogo);
        this.groupRepository.save(group);
    }

    static Path multipartFileToPath(MultipartFile multipartFile) throws IOException {

        File file = File.createTempFile(Objects.requireNonNull(multipartFile.getOriginalFilename()), "");
        multipartFile.transferTo(file);
        return file.toPath();
    }

}
