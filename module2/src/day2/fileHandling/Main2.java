package day2.fileHandling;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Main2 {

    public static void main(String[] args) {
        try(Reader fr = new FileReader("myfile.txt")){
            int value;
            do{
                value = fr.read();
                System.out.println((char)value);
            }while(value != -1);

        }catch(FileNotFoundException e){
            System.out.println("File not found!");
        }catch(IOException e){
            throw new RuntimeException(e);
        }

    }
}
