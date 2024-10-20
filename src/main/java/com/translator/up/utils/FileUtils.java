package com.translator.up.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileUtils() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory to store files.", ex);
        }
    }

    // luu tru file
    public String storeFile(MultipartFile file, Long projectId) throws IOException {
        // Normalize file name and save it
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        Path subFolder = this.fileStorageLocation.resolve(String.valueOf(projectId));
        if (!Files.exists(subFolder)) {
            Files.createDirectories(subFolder);
        }
        Path targetLocation = subFolder.resolve(fileName);
        if (Files.exists(targetLocation)) {
            Files.delete(targetLocation);
        }
        Files.copy(file.getInputStream(), targetLocation);
        return fileName;
    }

    // Tạo URI cho file download
    private String createDownloadUri(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/v1/user/download/project/")
                .path(fileName)
                .toUriString();
    }

    // Tải file từ hệ thống
    public Resource loadFileAsResource(Long projectId, String fileName) {
        try {
            Path subFolder = this.fileStorageLocation.resolve(String.valueOf(projectId));
            if (!Files.exists(subFolder)) {
                throw new RuntimeException("File not found " + fileName);
            }
            Path targetLocation = subFolder.resolve(fileName).normalize();
            Resource resource = new UrlResource(targetLocation.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    // Xoa file khoi storage
    public void deleteFile(String fileName) throws IOException {
        // Construct the file path
        Path filePath = this.fileStorageLocation.resolve(fileName);
        // Delete the file if it exists
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            throw new IOException("File not found: " + fileName);
        }
    }
}
