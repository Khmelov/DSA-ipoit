package by.it.group510972.bahutskaya.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
// Набираем побольше занятий, которые друг на друга не залезают.

public class TaskB {
    public static void main(String[] args) {
        TaskB instance = new TaskB();
        Event[] events = {new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
                new Event(1, 3), new Event(1, 3), new Event(1, 3), new Event(3, 6),
                new Event(2, 7), new Event(2, 3), new Event(2, 7), new Event(7, 9),
                new Event(3, 5), new Event(2, 4), new Event(2, 3), new Event(3, 7),
                new Event(4, 5), new Event(6, 7), new Event(6, 9), new Event(7, 9),
                new Event(8, 9), new Event(4, 6), new Event(8, 10), new Event(7, 10)
        };

        List<Event> starts = instance.calcStartTimes(events, 0, 10);
        System.out.println(starts);
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        List<Event> result = new ArrayList<>();
        // Сортируем по времени окончания
        Arrays.sort(events, Comparator.comparingInt((Event e) -> e.stop).thenComparingInt(e -> e.start));
        int currentEnd = from;
        for (Event event : events) {
            // Берём занятие, если оно не пересекается с уже выбранным
            if (event.start >= currentEnd && event.start >= from && event.stop <= to) {
                result.add(event);
                currentEnd = event.stop;
            }
        }
        return result;
    }

    static class Event {
        int start;
        int stop;

        Event(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public String toString() {
            return "(" + start + ":" + stop + ")";
        }
    }
}
