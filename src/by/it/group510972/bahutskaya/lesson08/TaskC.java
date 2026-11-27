package by.it.group510972.bahutskaya.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Идём шагом в 1 или 2 ступеньки и набираем максимальную сумму.

public class TaskC {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n=scanner.nextInt();
        int stairs[]=new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        if (n == 0) {
            return 0;
        }
        // На ступеньку приходим с предыдущей или через одну
        int prev2 = 0;
        int prev1 = stairs[0];
        for (int i = 1; i < n; i++) {
            int current = stairs[i] + Math.max(prev1, prev2);
            prev2 = prev1;
            prev1 = current;
        }
        return prev1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskC.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
