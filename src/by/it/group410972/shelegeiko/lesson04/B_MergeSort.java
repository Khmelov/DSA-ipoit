package by.it.group410972.shelegeiko.lesson04;

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

        // Запускаем рекурсивную сортировку для всего массива
        mergeSort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    // Рекурсивная функция, которая делит массив
    private void mergeSort(int[] array, int left, int right) {
        // Базовый случай рекурсии: если в подмассиве 1 или 0 элементов, он уже отсортирован
        if (left < right) {
            // Находим середину
            int mid = left + (right - left) / 2;

            // Рекурсивно сортируем левую и правую половины
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // Сливаем две отсортированные половины
            merge(array, left, mid, right);
        }
    }

    // Функция слияния двух подмассивов: array[left..mid] и array[mid+1..right]
    private void merge(int[] array, int left, int mid, int right) {
        // 1. Создаем временные массивы для хранения левой и правой частей
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // 2. Копируем данные во временные массивы
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        // 3. Сливаем временные массивы обратно в основной
        int i = 0; // Индекс для левого временного массива
        int j = 0; // Индекс для правого временного массива
        int k = left; // Индекс для основного (слитого) массива

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // 4. Копируем оставшиеся элементы, если они есть
        // (один из циклов while точно не выполнится)
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}