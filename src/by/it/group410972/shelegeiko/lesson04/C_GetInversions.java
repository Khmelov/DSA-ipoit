package by.it.group410972.shelegeiko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)
...
*/

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    // Рекурсивный метод для сортировки и подсчета инверсий
    private long mergeSortAndCount(int[] arr, int left, int right) {
        long inversionCount = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Инверсии в левой половине
            inversionCount += mergeSortAndCount(arr, left, mid);
            // Инверсии в правой половине
            inversionCount += mergeSortAndCount(arr, mid + 1, right);
            // Инверсии при слиянии
            inversionCount += mergeAndCount(arr, left, mid, right);
        }
        return inversionCount;
    }

    // Метод для слияния и подсчета инверсий между половинами
    private long mergeAndCount(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(arr, left, leftArray, 0, n1);
        System.arraycopy(arr, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        long swaps = 0;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                arr[k++] = leftArray[i++];
            } else {
                arr[k++] = rightArray[j++];
                swaps += (n1 - i);
            }
        }

        while (i < n1) {
            arr[k++] = leftArray[i++];
        }
        while (j < n2) {
            arr[k++] = rightArray[j++];
        }

        return swaps;
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем рекурсивный метод и приводим результат к int,
        // так как по условиям задачи он помещается в этот тип.
        long totalInversions = mergeSortAndCount(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return (int) totalInversions;
    }
}