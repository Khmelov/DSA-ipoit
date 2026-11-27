package by.it.group510972.bahutskaya.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// Включаем камеру как можно реже, но чтобы все события попали в запись

public class TaskA {

    public static void main(String[] args) {
        TaskA instance = new TaskA();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1);
        System.out.println(starts);
    }

    List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> result = new ArrayList<>();
        if (events.length == 0) {
            return result;
        }
        // Сортируем события по времени
        Arrays.sort(events);
        int i = 0;
        while (i < events.length) {
            // Включаем камеру и пропускаем всё, что она успеет снять
            double start = events[i];
            result.add(start);
            double stop = start + workDuration;
            while (i < events.length && events[i] <= stop) {
                i++;
            }
        }
        return result;
    }
}
