package by.it.group510972.bahutskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

// Для каждого события считаем, сколько камер его сняли.

public class TaskA {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskA.class.getResourceAsStream("dataA.txt");
        TaskA instance = new TaskA();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
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
        // Сортируем отрезки по началу
        Arrays.sort(segments);
        for (int i = 0; i < m; i++) {
            int count = 0;
            for (Segment segment : segments) {
                if (segment.start > points[i]) {
                    break;
                }
                if (segment.stop >= points[i]) {
                    count++;
                }
            }
            result[i] = count;
        }
        return result;
    }

    private class Segment implements Comparable<Segment> {
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
        public int compareTo(Segment o) {
            int byStart = Integer.compare(start, o.start);
            if (byStart != 0) {
                return byStart;
            }
            return Integer.compare(stop, o.stop);
        }
    }

}
