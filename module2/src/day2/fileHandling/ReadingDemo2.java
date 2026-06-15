package day2.fileHandling;

import java.io.*;

public class ReadingDemo2 {
    public static void main(String[] args) {
        try(Reader fr = new FileReader("myfile.txt")){
            BufferedReader bfr = new BufferedReader(fr);
            String line;
            do{
                line = bfr.readLine();
                if(line!=null)
                System.out.println(line);
            }while(line!=null);

        }catch(FileNotFoundException e){
            System.out.println("File not found!");
        }catch(IOException e){
            throw new RuntimeException(e);
        }

    }
}
