package com.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import javafx.scene.image.Image;

public class ImageService {
    private final Path storageDirectory;

    public ImageService(){
        String userHome = System.getProperty("user.home"); // C:\Users\<user> |/home/<user>
        this.storageDirectory = Paths.get(userHome,".tracking-anime", "images");

        try {
            Files.createDirectories(storageDirectory);
        }catch(IOException e){
            System.err.println("Can't create image folder");
            e.printStackTrace();
        }
    }

    public String saveImage(File sourceFile){
        if (sourceFile == null || !sourceFile.exists()) {
            return null;
        }

        try{
            String uniqueFileName = UUID.randomUUID().toString() + getFileExtension(sourceFile.getName());
            Path destinationPath = storageDirectory.resolve(uniqueFileName);

            Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        }catch(IOException e){
            System.err.println("Can't save image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Image getImage(String filename) {
        if (filename == null || filename.isBlank()) {
            return null;
        }

        try {
            Path imagePath = storageDirectory.resolve(filename);
            if (Files.exists(imagePath)) {
                return new Image(imagePath.toUri().toString());
            }
        } catch (Exception e) {
            System.err.println("Can't get file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

     public boolean deleteImage(String filename) {
        if (filename == null || filename.isBlank()) {
            return false;
        }
        try {
            Path imagePath = storageDirectory.resolve(filename);
            return Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            System.err.println("Can't delete image: " + e.getMessage());
            return false;
        }
    }


     private String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".")))
                .orElse("");
    }
}
