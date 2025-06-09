package by.it.group410972.kimstach.lesson05;

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
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // Читаем число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            result[i] = countSegments(segments, points[i]);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    void quickSort(Segment[] segments, int left, int right) {
        while (left < right) {
            int lt = left, gt = right;
            Segment pivot = segments[left];
            int i = left;

            while (i <= gt) {
                if (segments[i].start < pivot.start) {
                    swap(segments, lt++, i++);
                } else if (segments[i].start > pivot.start) {
                    swap(segments, i, gt--);
                } else {
                    i++;
                }
            }

            quickSort(segments, left, lt - 1);
            left = gt + 1; // Элиминация хвостовой рекурсии
        }
    }

    // Метод бинарного поиска для первого отрезка
    int countSegments(Segment[] segments, int point) {
        int left = 0, right = segments.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point && segments[mid].stop >= point) {
                return countRemaining(segments, mid, point);
            }
            if (segments[mid].start < point) left = mid + 1;
            else right = mid - 1;
        }
        return 0;
    }

    // Подсчет всех подходящих отрезков после нахождения первого
    int countRemaining(Segment[] segments, int index, int point) {
        int count = 0;
        for (int i = index; i < segments.length && segments[i].start <= point; i++) {
            if (segments[i].stop >= point) count++;
        }
        return count;
    }

    void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
