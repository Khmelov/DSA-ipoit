package by.it.group510972.bahutskaya.lesson04;

import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class Lesson04Test {
    @Test
    public void checkA() throws Exception {
        InputStream inputStream = TaskA.class.getResourceAsStream("dataA.txt");
        TaskA instance = new TaskA();
        int[] result = instance.findIndex(inputStream);
        StringBuilder sb = new StringBuilder();
        for (int index : result) {
            sb.append(index).append(" ");
        }
        boolean ok = sb.toString().trim().equals("3 1 -1 1 -1");
        assertTrue("A failed", ok);
    }

    @Test
    public void checkB() throws Exception {
        InputStream inputStream = TaskB.class.getResourceAsStream("dataB.txt");
        TaskB instance = new TaskB();
        int[] result = instance.getMergeSort(inputStream);
        boolean ok = result.length > 3;
        int test[] = new int[result.length];
        System.arraycopy(result, 0, test, 0, result.length);
        Arrays.sort(test);
        for (int i = 0; i < result.length; i++) {
            ok = ok && (result[i] == test[i]);
        }
        assertTrue("B failed", ok);
    }

    @Test
    public void checkC() throws Exception {
        InputStream inputStream = TaskC.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        int result = instance.calc(inputStream);
        boolean ok = (2 == result);
        assertTrue("C failed", ok);

    }

}
