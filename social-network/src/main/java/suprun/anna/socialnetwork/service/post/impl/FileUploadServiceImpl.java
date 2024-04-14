package suprun.anna.socialnetwork.service.post.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import suprun.anna.socialnetwork.service.post.FileUploadService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    public String getDir() {
        return UPLOAD_DIR;
    }

    @Override
    public String saveFile(MultipartFile picture) throws IOException {
        String fileName = createNameForFile(picture);
        String filePath = getFilePath(fileName);
        File dest = new File(filePath);
        System.out.println(dest.exists());
        picture.transferTo(dest);
        return fileName;
    }

    public String createNameForFile(MultipartFile picture) {
        String originalFileName = picture.getOriginalFilename();
        return UUID.randomUUID() + "_" + originalFileName;
    }

    @Override
    public String deleteFile(String filename) {
        File file = new File(getFilePath(filename));
        if (file.exists()) {
            file.delete();
        }
        return filename;
    }

    @Override
    public String getFilePath(String filename) {
        return UPLOAD_DIR + File.separator + filename;
    }

    @Override
    public byte[] getPicture(String filename) throws IOException {
        String imagePath = getFilePath(filename);
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            return Files.readAllBytes(Paths.get(imagePath));
        } else {
            throw new RuntimeException("No image with name " + filename);
        }
    }

    @Override
    public Optional<File> getImageFile(String imagePath) {
        return Optional.of(new File(imagePath));
    }
}

