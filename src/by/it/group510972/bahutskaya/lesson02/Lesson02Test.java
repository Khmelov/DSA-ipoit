package by.it.group510972.bahutskaya.lesson02;

import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Lesson02Test {
    @Test(timeout = 2000)
    public void checkA_videoRegistrator() {
        TaskA instance = new TaskA();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1);
        boolean ok = starts.toString().equals("[1.0, 2.2, 3.7, 5.5, 8.1]");
        assertTrue("slowA failed", ok);
    }

    @Test(timeout = 2000)
    public void checkB_sheduler() {
        TaskB instance = new TaskB();
        TaskB.Event[] events = {new TaskB.Event(0, 3), new TaskB.Event(0, 1), new TaskB.Event(1, 2), new TaskB.Event(3, 5),
                new TaskB.Event(1, 3), new TaskB.Event(1, 3), new TaskB.Event(1, 3), new TaskB.Event(3, 6),
                new TaskB.Event(2, 7), new TaskB.Event(2, 3), new TaskB.Event(2, 7), new TaskB.Event(7, 9),
                new TaskB.Event(3, 5), new TaskB.Event(2, 4), new TaskB.Event(2, 3), new TaskB.Event(3, 7),
                new TaskB.Event(4, 5), new TaskB.Event(6, 7), new TaskB.Event(6, 9), new TaskB.Event(7, 9),
                new TaskB.Event(8, 9), new TaskB.Event(4, 6), new TaskB.Event(8, 10), new TaskB.Event(7, 10)
        };

        List<TaskB.Event> starts = instance.calcStartTimes(events, 0, 10);
        boolean ok = starts.toString().equals("[(0:1), (1:2), (2:3), (3:5), (6:7), (7:9)]");
        assertTrue("TaskB failed", ok);
    }

    @Test(timeout = 2000)
    public void checkC_greedyKnapsack() throws Exception {
        InputStream inputStream = TaskC.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new TaskC().calc(inputStream);
        boolean ok = costFinal == 200;
        assertTrue("TaskB failed", ok);
    }

}
