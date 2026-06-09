package operationString;

public class PalindromeOrNot {
    public static void main(String[] args) {
        String s = "ABCDCBA";
        boolean flag = true;
        for(int i = 0; i < s.length()/2; i++){
            if(s.charAt(i) != s.charAt(s.length()-i-1)){
                System.out.println("Not palindrome");
                flag = false;
                return;
            }
        }
        if(flag){
            System.out.println("palindrome");
        }

    }
}
