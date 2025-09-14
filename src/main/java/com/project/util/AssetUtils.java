package com.project.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.project.Main;

import javafx.scene.image.Image;

public class AssetUtils {

    // public static String getCss(String name) {
    // return Objects.requireNonNull(Main.class.getResource("css/" +
    // name)).toExternalForm();
    // }

    // public static Image getImage(String name) {
    // return new Image(Objects.requireNonNull(Main.class.getResource("images/" +
    // name)).toExternalForm());
    // }

    // public static Image getImage(String name, int size) {
    // return new Image(Objects.requireNonNull(Main.class.getResource("images/" +
    // name)).toExternalForm(), size, size, true, true);
    // }


    // /resource/images/<name>
    public static Image getImage(String path) {
        try {
            InputStream imageStream = Main.class.getResourceAsStream("/images/" + path);
            if (imageStream == null) {
                System.err.println("Can't find image from : " + path);
                return null;
            }
            return new Image(imageStream);

        } catch (Exception e) {
            System.err.println("Can't load image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    // get from computer ~.tracking-anime/images/<filename>
    public static Image getImageFromComputer(String filename) {
        String userHome = System.getProperty("user.home"); // C:\Users\<user> |/home/<user>
        Path storageDirectory = Paths.get(userHome, ".tracking-anime", "images");
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
}
/*
 * src/main/resources/images/logo.png
 */
