package day2.fileHandling;

import java.io.IOException;
import java.nio.file.*;

public class FileCopyExample {
    public static void main(String[] args) {
        Path source = Paths.get("source.txt");
        Path destination = Paths.get("copy.txt");

        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
