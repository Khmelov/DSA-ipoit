package by.it.group510972.bahutskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Сортировка подсчётом. Числа маленькие, не больше 10.

public class TaskB {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskB.class.getResourceAsStream("dataB.txt");
        TaskB instance = new TaskB();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] points = new int[n];

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        // Считаем, сколько раз встретилось каждое число
        int[] count = new int[11];
        for (int point : points) {
            count[point]++;
        }
        // Выписываем числа от меньшего к большему
        int index = 0;
        for (int value = 0; value < count.length; value++) {
            for (int c = 0; c < count[value]; c++) {
                points[index++] = value;
            }
        }
        return points;
    }

}
