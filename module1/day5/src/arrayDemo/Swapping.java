package arrayDemo;

import java.util.Scanner;

public class Swapping {
    public static void main(String[] args) {
        String[] sr = {"g", "k", "l", "q"};
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter First index: ");
        int i1 = sc.nextInt();
        System.out.println("Enter Second index: ");
        int i2 = sc.nextInt();

        String temp = sr[i1];
        sr[i1] = sr[i2];
        sr[i2] = temp;

        for(int i=0;i<sr.length;i++){
            System.out.println(sr[i]);
        }
    }
}
