package by.it.group410972.shelegeiko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)

Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Максимальное возможное значение по условию
        int max_val = 10;

        // 1. Создаем массив для подсчета частот.
        // Размер max_val + 1, чтобы вместить индекс 10.
        int[] counts = new int[max_val + 1];

        // Читаем точки и сразу считаем их частоту
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
            counts[points[i]]++;
        }

        // 2. Восстанавливаем отсортированный массив
        int resultIndex = 0;
        // Идем по массиву частот
        for (int i = 0; i <= max_val; i++) {
            // Для каждого числа i, добавляем его в результат counts[i] раз
            for (int j = 0; j < counts[i]; j++) {
                points[resultIndex] = i;
                resultIndex++;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
}