package by.it.group410971.tishuk.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt(); // количество отрезков
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt(); // количество точек
        int[] points = new int[m];
        int[] result = new int[m];

        // Чтение отрезков
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        // Чтение точек
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по началу с помощью быстрой сортировки с 3-разбиением
        quickSort(segments, 0, segments.length - 1);

        // Для каждой точки ищем количество отрезков, содержащих её
        for (int i = 0; i < m; i++) {
            int point = points[i];
            result[i] = countSegmentsContainingPoint(segments, point);
        }

        return result;
    }

    // Быстрая сортировка с трёхпутевым разбиением
    private void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = arr[low];
            int i = low + 1;

            while (i <= gt) {
                if (arr[i].compareTo(pivot) < 0) {
                    swap(arr, lt++, i++);
                } else if (arr[i].compareTo(pivot) > 0) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            // Рекурсия на меньшую часть (элиминация хвостовой рекурсии)
            if (lt - low < high - gt) {
                quickSort(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    // Подсчёт количества отрезков, содержащих точку
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        int count = 0;

        // Ищем индекс первого отрезка, у которого начало <= point
        int left = 0, right = segments.length - 1;
        int first = segments.length; // если все больше point

        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point) {
                left = mid + 1;
            } else {
                first = mid;
                right = mid - 1;
            }
        }

        // Перебираем отрезки с индексами [0, first)
        for (int i = 0; i < first; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }

    // Обмен элементов массива
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Класс отрезка
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}
