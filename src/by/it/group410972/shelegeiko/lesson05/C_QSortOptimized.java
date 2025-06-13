package by.it.group410972.shelegeiko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_QSortOptimized {

    // --- Вспомогательные методы для сортировки и поиска ---

    /**
     * Быстрая сортировка с 3-way partitioning (Дейкстры) и элиминацией хвостовой рекурсии.
     */
    private void quickSort3Way(int[] a, int lo, int hi) {
        while (lo < hi) {
            // Базовый случай для маленьких массивов, можно переключиться на Insertion Sort
            // (здесь для простоты не делаем)

            int lt = lo, gt = hi;
            int v = a[lo]; // Опорный элемент
            int i = lo + 1;

            while (i <= gt) {
                int cmp = Integer.compare(a[i], v);
                if (cmp < 0) {
                    swap(a, lt++, i++);
                } else if (cmp > 0) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }
            // a[lo..lt-1] < v
            // a[lt..gt] == v
            // a[gt+1..hi] > v

            // Элиминация хвостовой рекурсии: рекурсивно вызываемся для меньшего подмассива,
            // а для большего продолжаем цикл.
            if (lt - lo < hi - gt) {
                quickSort3Way(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSort3Way(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * Бинарный поиск для подсчета количества элементов <= value.
     */
    private int countLessOrEqual(int[] array, int value) {
        int left = 0;
        int right = array.length - 1;
        int count = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (array[mid] <= value) {
                count = mid + 1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return count;
    }

    /**
     * Бинарный поиск для подсчета количества элементов < value.
     */
    private int countLessThan(int[] array, int value) {
        int left = 0;
        int right = array.length - 1;
        int count = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (array[mid] < value) {
                count = mid + 1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return count;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = scanner.nextInt();
        int[] starts = new int[n];
        int[] stops = new int[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            stops[i] = scanner.nextInt();
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // 1. Сортируем массивы начал и концов отрезков
        quickSort3Way(starts, 0, n - 1);
        quickSort3Way(stops, 0, n - 1);

        // 2. Для каждой точки ищем количество покрывающих ее отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Количество отрезков, которые начались не позже точки p
            int startedCount = countLessOrEqual(starts, point);

            // Количество отрезков, которые закончились строго до точки p
            int stoppedCount = countLessThan(stops, point);

            result[i] = startedCount - stoppedCount;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    // Класс Segment остается неиспользованным, так как алгоритм
    // требует раздельной обработки начал и концов отрезков.
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start > stop) { // Защита от неправильного порядка
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // Для этого алгоритма сравнение объектов Segment не требуется,
            // но если бы оно было нужно, логично было бы сравнивать по началу.
            return Integer.compare(this.start, o.start);
        }
    }
}