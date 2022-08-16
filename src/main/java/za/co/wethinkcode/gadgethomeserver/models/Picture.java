package za.co.wethinkcode.gadgethomeserver.models;

import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Entity
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;

    private String fileType;

    private long size;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Picture(MultipartFile file, Post post) throws IOException {
        this.post = post;
        this.image = file.getBytes();
        this.fileName = file.getOriginalFilename();
        this.fileType = file.getContentType();
        this.size = file.getSize();
    }

    public Picture(String fileName, String fileType, long size, byte[] image, Post post) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.image = image;
        this.post = post;
    }

    public Picture() {

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
