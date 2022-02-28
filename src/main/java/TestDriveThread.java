import java.util.Arrays;

public class TestDriveThread {

    static int size = 10000000;
    static float[] arr = new float[size];
    static int h = size / 2;


    public static void main(String[] args) {

        firstMethod();
        secondMethod();

    }

    public static void firstMethod() {

        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1.0f;
        }

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) ((arr[i] * Math.sin(0.2f + i / 5)) * Math.cos(0.2f + i / 5) * Math.cos(0.4 + i / 2));
        }

        System.out.println("One thread time: " + (System.currentTimeMillis() - startTime) + " ms.");
        // System.out.println(Arrays.toString(TestDriveThread.arr));
    }

    public static void secondMethod() {

        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1.0f;
        }
        float[] leftHalf = new float[h];
        float[] rightHalf = new float[h];

        long startTime = System.currentTimeMillis();

        long splittingArr = System.currentTimeMillis();

        System.arraycopy(TestDriveThread.arr, 0, leftHalf, 0, h);
        System.arraycopy(TestDriveThread.arr, h, rightHalf, 0, h);

        System.out.println("Splitting the array time: " + (System.currentTimeMillis() - splittingArr) + " ms.");

        Thread t1 = new Thread(() -> {
            long startTimeT1 = System.currentTimeMillis();

            for (int i = 0; i < leftHalf.length; i++) {
                leftHalf[i] = (float) ((leftHalf[i] * Math.sin(0.2f + i / 5)) * Math.cos(0.2f + i / 5) * Math.cos(0.4 + i / 2));
            }

            System.out.println("Thread t1 time: " + (System.currentTimeMillis() - startTimeT1) + " ms.");
        });

        Thread t2 = new Thread(() -> {
            long startTimeT2 = System.currentTimeMillis();

            for (int i = rightHalf.length; i < arr.length; i++) {
                rightHalf[i - rightHalf.length] = (float) ((rightHalf[i - rightHalf.length] * Math.sin(0.2f + i / 5)) * Math.cos(0.2f + i / 5) * Math.cos(0.4 + i / 2));
            }

            System.out.println("Thread t2  time: " + (System.currentTimeMillis() - startTimeT2) + " ms.");
        });

        t1.start();

        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // System.out.println(Arrays.toString(leftHalf) + " left");
        // System.out.println(Arrays.toString(rightHalf) + " right");

        float[] mergeArray = new float[size];

        long gluingLeftHalfAndRightHalfStart = System.currentTimeMillis();

        System.arraycopy(leftHalf, 0, mergeArray, 0, h);
        System.arraycopy(rightHalf, 0, mergeArray, h, h);

        System.out.println("Glue the left half and the right half time: " + (System.currentTimeMillis() - gluingLeftHalfAndRightHalfStart) + " ms.");

        System.out.println("Multithreaded time: " + (System.currentTimeMillis() - startTime) + " ms.");
        // System.out.println(Arrays.toString(mergeArray) + " array 2");

    }
}
