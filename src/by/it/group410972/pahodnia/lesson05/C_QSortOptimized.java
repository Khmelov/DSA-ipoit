package by.it.group410972.pahodnia.lesson05;

import java.io.InputStream;
import java.util.Scanner;

public class C_QSortOptimized {
    public static void main(String[] args) {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        // число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        for (int i = 0; i < m; i++) {
            result[i] = countSegmentsContainingPoint(segments, points[i]);
        }

        return result;
    }

    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        int leftIndex = binarySearch(segments, point);
        if (leftIndex == -1) return 0;

        int count = 0;
        for (int i = leftIndex; i < segments.length; i++) {
            if (segments[i].start <= point && segments[i].stop >= point) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

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

        return result;
    }

    private static class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}
