package arrayDemo;

import java.util.Arrays;

public class StringDemo {
    public static void main(String[] args) {
        String[] sr= new String[4];
        String[] sr2= {"dgvsjhg", "jhdgsj", "ghfdshg"};
        Arrays.fill(sr,"adshg");
        sr[0]="abcd";
        sr[1]="dsgyd";
        sr[2]="geuiue";
        sr[3]="kashi";
        System.out.println("Length of array is: " + sr.length);
        for (int i = 0; i < sr.length; i++) System.out.println(sr[i]);

    }
}
