package by.it.group410971.tishuk.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)
*/

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // читаем элементы массива
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // максимальное значение во входных данных — 10
        int max = 10;

        // создаем массив для подсчета количества каждого значения
        int[] count = new int[max + 1];

        // считаем количество вхождений каждого значения
        for (int i = 0; i < n; i++) {
            count[points[i]]++;
        }

        // восстанавливаем отсортированный массив
        int index = 0;
        for (int i = 0; i <= max; i++) {
            while (count[i] > 0) {
                points[index++] = i;
                count[i]--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
