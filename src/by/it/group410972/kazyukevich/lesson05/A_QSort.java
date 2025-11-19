package by.it.group410972.kazyukevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Чтение количества отрезков и точек
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        // Чтение отрезков
        Segment[] segments = new Segment[n];
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }

        // Чтение точек
        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортировка отрезков по начальной точке
        Arrays.sort(segments);

        // Результат для каждой точки
        int[] result = new int[m];

        // Для каждой точки считаем количество отрезков, которые ее покрывают
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Бинарный поиск для нахождения отрезков, которые начинаются до или в момент точки
            int left = 0;
            int right = n - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // Проверяем, попадает ли точка в интервал каждого из найденных отрезков
            for (int j = 0; j <= right; j++) {
                if (segments[j].start <= point && point <= segments[j].stop) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    // Класс отрезка
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Сортировка по начальной точке, затем по конечной
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}
