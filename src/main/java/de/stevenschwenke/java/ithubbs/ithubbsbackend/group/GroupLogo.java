package de.stevenschwenke.java.ithubbs.ithubbsbackend.group;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "grouplogos")
public class GroupLogo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Byte[] content;

    @NotNull
    private String filename;

    @NotNull
    private String fileType;

    public GroupLogo() {
    }

    public GroupLogo(@NotNull String filename, @NotNull String fileType, @NotNull Byte[] content) {
        this.content = content;
        this.filename = filename;
        this.fileType = fileType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte[] getContent() {
        return content;
    }

    public void setContent(Byte[] content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
