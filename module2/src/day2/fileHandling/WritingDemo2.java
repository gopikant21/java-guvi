package day2.fileHandling;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WritingDemo2 {
    public static void main(String[] args) {
        try(Writer fw = new FileWriter("myfile.txt", true)){
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("hello vdhs");
            bw.newLine();
            bw.write("bhsdj djk");
            bw.write("vhjd dgsu jdsg hgejs");
            System.out.println("Data has been appended to the file!!!");
        }catch(IOException e){
            throw new RuntimeException();
        }finally{
            System.out.println("bdsjk");
        }
    }
}
