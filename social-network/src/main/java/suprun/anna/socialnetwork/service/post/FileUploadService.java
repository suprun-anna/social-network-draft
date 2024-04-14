package suprun.anna.socialnetwork.service.post;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface FileUploadService {
    public String getDir();

    String saveFile(MultipartFile picture) throws IOException;

    String deleteFile(String filename);

    String getFilePath(String filename);

    byte[] getPicture(String filename) throws IOException;

    Optional<File> getImageFile(String imagePath);
}
