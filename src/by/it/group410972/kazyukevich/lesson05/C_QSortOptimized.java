package by.it.group410972.kazyukevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

        int n = scanner.nextInt();
        int m = scanner.nextInt();

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

        quickSort(segments, 0, segments.length - 1);

        // Результат для каждой точки
        int[] result = new int[m];

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            int left = 0;
            int right = n - 1;
            int firstSegmentIndex = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    firstSegmentIndex = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            if (firstSegmentIndex != -1) {
                for (int j = 0; j <= firstSegmentIndex; j++) {
                    if (segments[j].start <= point && point <= segments[j].stop) {
                        count++;
                    }
                }
            }

            result[i] = count;
        }

        return result;
    }

    private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int[] pivotIndices = partition(segments, low, high);
            quickSort(segments, low, pivotIndices[0] - 1);
            low = pivotIndices[1] + 1;
        }
    }

    private int[] partition(Segment[] segments, int low, int high) {
        Segment pivot = segments[low];
        int lt = low;
        int gt = high;
        int i = low + 1;

        while (i <= gt) {
            int cmp = segments[i].compareTo(pivot);
            if (cmp < 0) {
                swap(segments, lt++, i++);
            } else if (cmp > 0) {
                swap(segments, i, gt--);
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
}