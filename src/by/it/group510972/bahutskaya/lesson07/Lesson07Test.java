package by.it.group510972.bahutskaya.lesson07;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Lesson07Test {
    @Test
    public void A() throws Exception {
        TaskA instance = new TaskA();
        assertEquals("A1 failed", instance.getDistanceEdinting("ab","ab"),0);
        assertEquals("A2 failed", instance.getDistanceEdinting("short","ports"),3);
        assertEquals("A3 failed", instance.getDistanceEdinting("distance","editing"),5);
    }

    @Test
    public void B() throws Exception {
        TaskB instance = new TaskB();
        assertEquals("B1 failed", instance.getDistanceEdinting("ab","ab"),0);
        assertEquals("B2 failed", instance.getDistanceEdinting("short","ports"),3);
        assertEquals("B3 failed", instance.getDistanceEdinting("distance","editing"),5);
    }

    @Test
    public void C() throws Exception {
        TaskC instance = new TaskC();
        assertEquals("C1 failed", instance.getDistanceEdinting("ab","ab"),"#,#,");
        assertEquals("C2 failed", instance.getDistanceEdinting("short","ports").split("#").length,4);

        assertEquals("C3 failed", instance.getDistanceEdinting("distance","editing").split("#").length,5);
    }

}
