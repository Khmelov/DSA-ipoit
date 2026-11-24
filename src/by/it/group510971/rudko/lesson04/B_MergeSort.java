package by.it.group510971.rudko.lesson04;

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

    //сортировка слиянием отрезка [from, to) массива a, buf - вспомогательный буфер
    static void mergeSort(int[] a, int[] buf, int from, int to) {
        if (to - from < 2) return;
        int mid = (from + to) >>> 1;
        mergeSort(a, buf, from, mid);
        mergeSort(a, buf, mid, to);
        int i = from, j = mid, k = from;
        while (i < mid && j < to) {
            buf[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        }
        while (i < mid) buf[k++] = a[i++];
        while (j < to) buf[k++] = a[j++];
        System.arraycopy(buf, from, a, from, to - from);
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // сортировка слиянием: делим отрезок пополам, сортируем части и сливаем
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        mergeSort(a, new int[n], 0, n);


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
