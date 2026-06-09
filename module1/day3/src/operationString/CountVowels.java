package operationString;

public class CountVowels {
    public static void main(String[] args) {
        String str = "Hello World";
        int count = 0;

        for (char ch : str.toCharArray()) {
            ch = Character.toLowerCase(ch);
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                count++;
            }
        }

        System.out.println("Number of vowels: " + count);
    }
}
