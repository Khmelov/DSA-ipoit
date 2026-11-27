package by.it.group510972.bahutskaya.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Самая длинная невозрастающая подпоследовательность.

public class TaskC {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskB.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        // Удлиняем цепочку, если число не больше предыдущего
        int[] length = new int[n];
        int result = 0;
        for (int i = 0; i < n; i++) {
            length[i] = 1;
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && length[j] + 1 > length[i]) {
                    length[i] = length[j] + 1;
                }
            }
            if (length[i] > result) {
                result = length[i];
            }
        }
        return result;
    }

}
