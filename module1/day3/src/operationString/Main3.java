package operationString;

public class Main3 {
    public static void main(String[] args) {
        /*String d = "Hello world";
        System.out.println(d.charAt(0));
        System.out.println(d.length());
        System.out.println(d.charAt(d.length()-1));
        System.out.println(d.substring(0,4));
        System.out.println(d.substring(3));
        System.out.println(d.replace("world", "Java"));
        System.out.println(d.toUpperCase());
        System.out.println(d.toLowerCase());
        System.out.println(d.trim());
        System.out.println(d.indexOf("o"));
        System.out.println(d.lastIndexOf("o"));
        System.out.println(d.contains("z"));
        System.out.println(d.split(" "));
        System.out.println(d.startsWith("Hell"));
        System.out.println(d.endsWith("rld"));
        System.out.println(d.concat("Galaxy"));*/

        String s1 = "Sachin";
        String s2 = "Saurav";
        System.out.println(s2.compareTo(s1));
        System.out.println(s2.compareToIgnoreCase(s1));

        String s3 = "Sachin";
        String s4 = "sachin";
        System.out.println(s3.equals(s4));
        System.out.println(s3.equalsIgnoreCase(s4));
    }
}
