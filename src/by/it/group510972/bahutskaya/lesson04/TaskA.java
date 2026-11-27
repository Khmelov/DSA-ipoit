package by.it.group510972.bahutskaya.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Ищем числа в отсортированном массиве. В ответе индекс с 1, если числа нет — -1.

public class TaskA {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskA.class.getResourceAsStream("dataA.txt");
        TaskA instance = new TaskA();
        int[] result = instance.findIndex(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        int k = scanner.nextInt();
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            // Делим отрезок пополам, пока не найдём число
            int left = 0;
            int right = n - 1;
            int found = -1;
            while (left <= right) {
                int mid = (left + right) >>> 1;
                if (a[mid] == value) {
                    found = mid + 1;
                    break;
                }
                if (a[mid] < value) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            result[i] = found;
        }
        return result;
    }

}
