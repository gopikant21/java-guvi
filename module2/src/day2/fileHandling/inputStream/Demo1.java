package day2.fileHandling.inputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Demo1 {
    public static void main(String[] args) {
        InputStream input = null;

        try {
            // Create InputStream object (reads from file)
            input = new FileInputStream("example.txt");

            int data;

            // Read one byte at a time
            while ((data = input.read()) != -1) {
                // Convert byte to char and print
                System.out.print((char) data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close(); // Always close the stream
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
