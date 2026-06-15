package day2.fileHandling;

import java.io.*;

public class SimpleCopyExample {
    public static void main(String[] args) {
        String sourceFile = "source.txt";
        String destinationFile = "copy.txt";

        try (
                Reader reader = new FileReader(sourceFile);
                Writer writer = new FileWriter(destinationFile);
        ) {
            int value;

            // Read file line by line and write to new file
            while ((value = reader.read()) != -1) {
                writer.write((char)value);
                ; // preserve line breaks
            }

            System.out.println("File copied successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
