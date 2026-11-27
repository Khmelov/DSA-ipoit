package by.it.group510972.bahutskaya.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Сортировка слиянием.
public class TaskB {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskB.class.getResourceAsStream("dataB.txt");
        TaskB instance = new TaskB();
        int[] result = instance.getMergeSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        mergeSort(a, new int[n], 0, n - 1);
        return a;
    }

    private void mergeSort(int[] a, int[] temp, int left, int right) {
        if (left >= right) {
            return;
        }
        // Делим пополам, сортируем части и сливаем их
        int mid = (left + right) >>> 1;
        mergeSort(a, temp, left, mid);
        mergeSort(a, temp, mid + 1, right);
        int i = left;
        int j = mid + 1;
        int k = left;
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
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
    }

}
