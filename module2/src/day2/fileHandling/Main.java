package day2.fileHandling;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Main {
    public static void main(String[] args) {
        try(Writer fw = new FileWriter("myfile.txt", true)){
            fw.write("hello vdhs\n");
            fw.write("bhsdj djk\n");
            fw.write("vhjd dgsu jdsg hgejs\n");
            System.out.println("Data has been appended to the file!!!");
        }catch(IOException e){
            throw new RuntimeException();
        }finally{
            System.out.println("bdsjk");
        }
    }
}
