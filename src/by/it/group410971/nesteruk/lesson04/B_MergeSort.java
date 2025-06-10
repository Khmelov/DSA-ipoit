package by.it.group410971.nesteruk.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
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
        }

        class MergeSorter {
            void sort(int[] arr, int l, int r) {
                if (l < r) {
                    int m = (l + r) / 2;
                    sort(arr, l, m);
                    sort(arr, m + 1, r);

                    int n1 = m - l + 1;
                    int n2 = r - m;
                    int[] L = new int[n1];
                    int[] R = new int[n2];

                    System.arraycopy(arr, l, L, 0, n1);
                    System.arraycopy(arr, m + 1, R, 0, n2);

                    int i = 0, j = 0, k = l;
                    while (i < n1 && j < n2) {
                        if (L[i] <= R[j]) {
                            arr[k++] = L[i++];
                        } else {
                            arr[k++] = R[j++];
                        }
                    }

                    while (i < n1) arr[k++] = L[i++];
                    while (j < n2) arr[k++] = R[j++];
                }
            }
        }

        new MergeSorter().sort(a, 0, a.length - 1);
        return a;
    }


}
