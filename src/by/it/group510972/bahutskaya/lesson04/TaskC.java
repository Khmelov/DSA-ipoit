package by.it.group510972.bahutskaya.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class TaskC {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskC.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        return mergeCount(a, new int[n], 0, n - 1);
    }

    private int mergeCount(int[] a, int[] temp, int left, int right) {
        if (left >= right) {
            return 0;
        }
        int mid = (left + right) >>> 1;
        int count = mergeCount(a, temp, left, mid) + mergeCount(a, temp, mid + 1, right);
        int i = left;
        int j = mid + 1;
        int k = left;
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                // Правый элемент меньше каждого, кто ещё остался слева
                count += mid - i + 1;
                temp[k++] = a[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = a[i++];
        }
        while (j <= right) {
            temp[k++] = a[j++];
        }
        for (int t = left; t <= right; t++) {
            a[t] = temp[t];
        }
        return count;
    }
}
