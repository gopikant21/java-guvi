import java.util.Scanner;

public class StudentGrade {
    public static void main(String[] args) {
        System.out.println("Enter student marks: ");
        Scanner sc = new Scanner(System.in);
        int marks = sc.nextInt();

        checkGrade(marks);
        sc.close();


    }

    private static void checkGrade(int marks){
        if(marks>=0 && marks<=100){
            if (marks <= 30) {
                System.out.println("student grade is P");
            }else if(marks <= 40) {
                System.out.println("student grade is D");
            }else if(marks <= 60) {
                System.out.println("student grade is C");
            }else if(marks <= 90) {
                System.out.println("student grade is B");
            }else {
                System.out.println("student grade is A");
            }
        }else{
            System.out.println("entered marks is invalid. please enter mark between 0 to 100");
            System.out.println();
        }
    }
}
