package day2.fileHandling.outputStream;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Demo1 {
    public static void main(String[] args) {
        OutputStream output = null;

        try {
            // Create OutputStream object (writes to file)
            output = new FileOutputStream("example.txt");

            String data = "Hello, OutputStream in Java!";

            // Convert string to bytes
            byte[] dataBytes = data.getBytes();

            // Write bytes to file
            output.write(dataBytes);

            System.out.println("Data written successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close(); // Always close the stream
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}