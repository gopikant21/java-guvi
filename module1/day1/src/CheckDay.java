import java.util.Scanner;

// 1st june = monday, take date as input and print day
class CheckDay{
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int date = sc.nextInt();
        checkday(date);
        sc.close();
    }

    private static void checkday(int date){
        switch(date%7){
            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wednesday");
                break;
            case 4:
                System.out.println("Thursday");
                break;
            case 5:
                System.out.println("Friday");
                break;
            case 6:
                System.out.println("Saturday");
                break;
            case 7:
                System.out.println("Sunday");
        }
    }
}