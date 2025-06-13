package by.it.group410972.shelegeiko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class A_QSort {

    // Вспомогательный класс для представления событий на временной шкале
    private class Event implements Comparable<Event> {
        int coordinate;
        int type; // -1 для начала отрезка, 0 для точки, 1 для конца отрезка
        int index; // Для точек, чтобы знать, куда записывать результат

        public Event(int coordinate, int type, int index) {
            this.coordinate = coordinate;
            this.type = type;
            this.index = index;
        }

        @Override
        public int compareTo(Event other) {
            // Сначала сравниваем по координате
            int coordCompare = Integer.compare(this.coordinate, other.coordinate);
            if (coordCompare != 0) {
                return coordCompare;
            }
            // Если координаты равны, сортируем по типу: START(-1) < POINT(0) < STOP(1)
            return Integer.compare(this.type, other.type);
        }
    }

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = scanner.nextInt(); // число отрезков
        int m = scanner.nextInt(); // число точек

        final int START = -1;
        final int POINT = 0;
        final int STOP = 1;

        List<Event> events = new ArrayList<>(2 * n + m);

        // Добавляем события начала и конца для каждого отрезка
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            events.add(new Event(start, START, -1));
            events.add(new Event(stop, STOP, -1));
        }

        int[] points = new int[m];
        int[] result = new int[m];
        // Добавляем события для каждой точки, сохраняя их исходный индекс
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
            events.add(new Event(points[i], POINT, i));
        }

        // Сортируем все события. Можно использовать любую O(n log n) сортировку.
        // Collections.sort() - это эффективно и стандартно.
        Collections.sort(events);

        int activeCameras = 0;
        // Проходим по отсортированному списку событий
        for (Event event : events) {
            if (event.type == START) {
                activeCameras++;
            } else if (event.type == STOP) {
                activeCameras--;
            } else { // type == POINT
                // Для точки записываем текущее количество активных камер
                result[event.index] = activeCameras;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Класс Segment из шаблона остается неиспользованным,
    // так как для данного алгоритма удобнее работать с событиями.
    private class Segment implements Comparable<Segment> {
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