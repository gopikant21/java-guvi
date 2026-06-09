package operationString;

public class CountSpaces {
    public static void main(String[] args) {
        String s1 = "Sachin Tendulkar Rama swami";
        int l = s1.length();
        int count = 0;
        for(int i=0;i<l;i++){
            if(s1.charAt(i)==' '){
                count++;
            }
        }
        System.out.println(count);
    }
}
