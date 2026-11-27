package by.it.group510972.bahutskaya.lesson06;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class Lesson06Test {
    @Test
    public void checkA() throws Exception {
        InputStream inputStream = TaskA.class.getResourceAsStream("dataA.txt");
        TaskA instance = new TaskA();
        int result = instance.getSeqSize(inputStream);
        boolean ok = (result == 3);
        assertTrue("A failed", ok);
    }

    @Test
    public void checkB() throws Exception {
        InputStream inputStream = TaskB.class.getResourceAsStream("dataB.txt");
        TaskB instance = new TaskB();
        int result = instance.getDivSeqSize(inputStream);
        boolean ok = (result == 3);
        assertTrue("B failed", ok);
    }

    @Test(timeout = 1000)
    public void checkC() throws Exception {
        InputStream inputStream = TaskC.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        int result = instance.getNotUpSeqSize(inputStream);
        boolean ok = (result == 4);
        assertTrue("C failed", ok);
    }

}
