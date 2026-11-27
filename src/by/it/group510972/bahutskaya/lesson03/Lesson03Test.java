package by.it.group510972.bahutskaya.lesson03;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class Lesson03Test {
    @Test
    public void checkA() throws Exception {
        InputStream inputStream = TaskA.class.getResourceAsStream("dataA.txt");
        TaskA instance = new TaskA();
        String result = instance.encode(inputStream);
        boolean ok = result.equals("01001100100111");
        assertTrue("A failed", ok);
    }

    @Test
    public void checkB() throws Exception {
        InputStream inputStream = TaskB.class.getResourceAsStream("dataB.txt");
        TaskB instance = new TaskB();
        String result = instance.decode(inputStream);
        boolean ok = result.equals("abacabad");
        assertTrue("B failed", ok);
    }

    @Test
    public void checkC() throws Exception {
        InputStream inputStream = TaskC.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        Long res = instance.findMaxValue(inputStream);
        boolean ok = (res == 500);
        assertTrue("C failed", ok);
    }

}
