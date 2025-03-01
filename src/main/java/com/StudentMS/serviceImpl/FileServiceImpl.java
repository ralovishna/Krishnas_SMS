package com.StudentMS.serviceImpl;

import com.StudentMS.entity.Course;
import com.StudentMS.entity.File;
import com.StudentMS.entity.User;
import com.StudentMS.repository.FileRepo;
import com.StudentMS.service.FileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final String UPLOAD_DIR = "uploads/";
    @Autowired
    private FileRepo fileRepo;

    // Ensure directory exists or create it
    private void ensureDirectoryExists() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    // Store file with validation
    public void storeFile(@NotNull MultipartFile file, User uploader, boolean visibility, Course course) throws IOException {
        // Ensure the uploads directory exists
        ensureDirectoryExists();

        // File extension validation
        String fileExtension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        if (!isValidExtension(fileExtension)) {
            throw new IllegalArgumentException("Invalid file type");
        }

        // File size validation (limit to 10MB as an example)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds the limit of 10MB");
        }

        // Generating a unique file name
        String uniqueFileName = UUID.randomUUID() + "." + fileExtension;
        String filePath = UPLOAD_DIR + uniqueFileName;

        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        File fileEntity = new File();
        fileEntity.setFileName(uniqueFileName);
        fileEntity.setOriginalFileName(file.getOriginalFilename());
        fileEntity.setFilePath(filePath);
        fileEntity.setUploadedBy(uploader);
        fileEntity.setUploadDate(LocalDateTime.now()); // Ensure uploadDate is set
        fileEntity.setCourse(course);
        fileEntity.setVisibleToStudents(visibility);

        System.out.println("fileName: " + fileEntity.getFileName()); // Add this line
        System.out.println("originalFileName: " + fileEntity.getOriginalFileName()); // add this line.


        fileRepo.save(fileEntity);

        fileRepo.save(fileEntity);
    }

    // Load file as resource with specific exception handling
    public Resource loadFileAsResource(Long fileId) throws MalformedURLException, FileNotFoundException {
        File fileEntity = fileRepo.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File with ID " + fileId + " not found"));
        Path path = Paths.get(fileEntity.getFilePath());
        return new UrlResource(path.toUri());
    }

    public void deleteFile(Long fileId) {
        fileRepo.deleteById(fileId);
    }

    @Override
    public List<File> findAll() {
        return fileRepo.findAll();
    }

    // Helper method to get the file extension
    private @NotNull String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return (index > 0) ? fileName.substring(index + 1) : "";
    }

    // Helper method to validate allowed file extensions
    private boolean isValidExtension(String extension) {
        // Example extensions, can be adjusted based on requirements
        String[] allowedExtensions = {"jpg", "jpeg", "png", "pdf", "txt"};
        for (String ext : allowedExtensions) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}