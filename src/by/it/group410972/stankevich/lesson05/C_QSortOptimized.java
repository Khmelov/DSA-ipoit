package by.it.group410972.stankevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsContainingPoint(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    // Быстрая сортировка (на основе 3-разбиения)
    private void quickSort(Segment[] segments, int left, int right) {
        while (left < right) { // Элиминация хвостовой рекурсии
            int lt = left, gt = right;
            Segment pivot = segments[left];
            int i = left;

            while (i <= gt) {
                if (segments[i].compareTo(pivot) < 0) {
                    swap(segments, lt++, i++);
                } else if (segments[i].compareTo(pivot) > 0) {
                    swap(segments, i, gt--);
                } else {
                    i++;
                }
            }

            quickSort(segments, left, lt - 1);
            left = gt + 1;
        }
    }

    // Метод для подсчета количества отрезков, содержащих точку (с бинарным поиском)
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        int leftIndex = binarySearch(segments, point);
        if (leftIndex == -1) return 0;

        // Подсчет отрезков, содержащих точку
        int count = 0;
        for (int i = leftIndex; i < segments.length; i++) {
            if (segments[i].start <= point && segments[i].stop >= point) {
                count++;
            } else {
                break; // Прекращаем поиск, как только точка выходит за границы отрезков
            }
        }
        return count;
    }

    // Бинарный поиск первого подходящего отрезка
    private int binarySearch(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start <= point) {
                result = mid;
                right = mid - 1; // Ищем первый подходящий отрезок слева
            } else {
                left = mid + 1;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }
    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            return 0;
        }
    }

}
