package by.it.group510972.bahutskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Та же задача с камерами, только отрезки сортируем на месте.

public class TaskC {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskC.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        // Сортируем отрезки на месте
        quickSort(segments, 0, segments.length - 1);
        for (int i = 0; i < m; i++) {
            int right = upperBound(segments, points[i]);
            int count = 0;
            for (int j = 0; j < right; j++) {
                if (segments[j].stop >= points[i]) {
                    count++;
                }
            }
            result[i] = count;
        }
        return result;
    }

    private int upperBound(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length;
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (segments[mid].start <= point) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    private void quickSort(Segment[] a, int from, int to) {
        while (from < to) {
            int[] bounds = partition3(a, from, to);
            if (bounds[0] - from < to - bounds[1]) {
                quickSort(a, from, bounds[0]);
                from = bounds[1];
            } else {
                quickSort(a, bounds[1], to);
                to = bounds[0];
            }
        }
    }

    private int[] partition3(Segment[] a, int left, int right) {
        Segment pivot = a[left + (right - left) / 2];
        int i = left;
        int lt = left;
        int gt = right;
        while (i <= gt) {
            int cmp = a[i].compareTo(pivot);
            if (cmp < 0) {
                swap(a, lt++, i++);
            } else if (cmp > 0) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt - 1, gt + 1};
    }

    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            int byStart = Integer.compare(start, other.start);
            if (byStart != 0) {
                return byStart;
            }
            return Integer.compare(stop, other.stop);
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

}
