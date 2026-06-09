package arrayDemo;

//stored in contiguous memory
public class Demo1 {
    public static void main(String[] args) {
        int[] arr = new int[5];
        // float[] arr = new float[5];
        // double[] arr = new double[5];

        arr[0] = 1;
        arr[1] = 2;
        arr[2] = 3;
        arr[3] = 4;
        arr[4] = 5;
        System.out.println("Length of array is: " + arr.length);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
