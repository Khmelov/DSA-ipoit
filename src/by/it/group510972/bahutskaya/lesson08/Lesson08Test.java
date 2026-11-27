package by.it.group510972.bahutskaya.lesson08;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class Lesson08Test {
    @Test
    public void A() throws Exception {
        InputStream stream = TaskA.class.getResourceAsStream("dataA.txt");
        TaskA instance = new TaskA();
        int res=instance.getMaxWeight(stream);
        assertEquals("A failed", res, 14);
    }

    @Test
    public void B() throws Exception {
        InputStream stream = TaskB.class.getResourceAsStream("dataB.txt");
        TaskB instance = new TaskB();
        int res=instance.getMaxWeight(stream);
        assertEquals("B failed", res, 9);
    }

    @Test
    public void C() throws Exception {
        InputStream stream = TaskC.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        int res=instance.getMaxSum(stream);
        assertEquals("C failed", res, 3);
    }

}
