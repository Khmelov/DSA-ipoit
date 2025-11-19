package by.it.group410972.kazyukevich.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
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

        return countInversions(a, 0, a.length - 1);
    }

    private int countInversions(int[] a, int left, int right) {
        int inversions = 0;

        if (left < right) {
            int mid = left + (right - left) / 2;

            inversions += countInversions(a, left, mid);
            inversions += countInversions(a, mid + 1, right);

            inversions += mergeAndCount(a, left, mid, right);
        }

        return inversions;
    }

    private int mergeAndCount(int[] a, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        for (int i = 0; i < n1; i++) {
            leftArray[i] = a[left + i];
        }
        for (int i = 0; i < n2; i++) {
            rightArray[i] = a[mid + 1 + i];
        }

        int i = 0, j = 0, k = left;
        int inversions = 0;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                a[k] = leftArray[i];
                i++;
            } else {
                a[k] = rightArray[j];
                j++;
                inversions += (mid + 1) - (left + i); // Подсчет инверсий
            }
            k++;
        }

        while (i < n1) {
            a[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            a[k] = rightArray[j];
            j++;
            k++;
        }

        return inversions;
    }
}