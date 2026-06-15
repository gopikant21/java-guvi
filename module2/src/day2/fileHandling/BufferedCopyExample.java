package day2.fileHandling;

import java.io.*;

public class BufferedCopyExample {
    public static void main(String[] args) {
        String sourceFile = "source.txt";
        String destinationFile = "copy.txt";

        try (
                BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile))
        ) {
            String line;

            // Read file line by line and write to new file
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine(); // preserve line breaks
            }

            System.out.println("File copied successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
