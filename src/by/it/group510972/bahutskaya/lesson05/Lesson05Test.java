package by.it.group510972.bahutskaya.lesson05;

import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class Lesson05Test {
    @Test
    public void checkA() throws Exception {
        InputStream inputStream = TaskA.class.getResourceAsStream("dataA.txt");
        TaskA instance = new TaskA();
        int[] result = instance.getAccessory(inputStream);
        boolean ok = Arrays.equals(result, new int[]{1, 0, 0});
        assertTrue("A failed", ok);
    }

    @Test
    public void checkB() throws Exception {
        InputStream inputStream = TaskB.class.getResourceAsStream("dataB.txt");
        TaskB instance = new TaskB();
        int[] result = instance.countSort(inputStream);
        boolean ok = Arrays.equals(result, new int[]{2, 2, 3, 9, 9});
        assertTrue("B failed", ok);
    }

    @Test
    public void checkC() throws Exception {
        InputStream inputStream = TaskC.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        int[] result = instance.getAccessory2(inputStream);
        boolean ok = Arrays.equals(result, new int[]{1, 0, 0});
        assertTrue("C failed", ok);
    }

}
