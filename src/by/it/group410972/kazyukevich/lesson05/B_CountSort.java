package by.it.group410972.kazyukevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_CountSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Чтение размера массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Чтение массива
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Создаем массив для подсчета частот
        int[] count = new int[11]; // Числа от 0 до 10

        // Подсчет частот
        for (int num : points) {
            count[num]++;
        }

        // Восстановление отсортированного массива
        int index = 0;
        for (int num = 0; num <= 10; num++) {
            while (count[num] > 0) {
                points[index++] = num;
                count[num]--;
            }
        }

        return points;
    }
}