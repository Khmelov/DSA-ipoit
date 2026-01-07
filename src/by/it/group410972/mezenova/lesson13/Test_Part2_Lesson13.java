package by.it.group410972.mezenova.lesson13;

import by.it.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 extends HomeWork {

    @Test
    public void testGraphA() {
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");

        // Дополнительные тесты
        run("5 -> 6, 5 -> 7, 6 -> 8, 7 -> 8", true).include("5 6 7 8");
        run("X -> Y, X -> Z, Y -> W, Z -> W", true).include("X Y Z W");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("A -> B, B -> C, A -> D, D -> E, E -> C", true).include("A B D E C");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 4, 3 -> 5, 4 -> 5", true).include("0 1 2 3 4 5");
        run("M -> N, M -> O, N -> P, O -> Q, P -> R, Q -> R", true).include("M N O P Q R");
        run("10 -> 20, 10 -> 30, 20 -> 40, 30 -> 40, 40 -> 50", true).include("10 20 30 40 50");
        run("A -> C, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("1 -> 3, 2 -> 4, 3 -> 5, 4 -> 5, 5 -> 6", true).include("1 2 3 4 5 6");
        run("X1 -> X2, X1 -> X3, X2 -> X4, X3 -> X4", true).include("X1 X2 X3 X4");
        run("a -> b, a -> c, b -> d, c -> e, d -> f, e -> f", true).include("a b c d e f");
        run("0 -> 1, 0 -> 2, 0 -> 3, 1 -> 4, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, A -> C, A -> D, B -> E, C -> E, D -> E", true).include("A B C D E");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 5, 4 -> 6, 5 -> 6", true).include("1 2 3 4 5 6");
        run("start -> middle, middle -> end", true).include("start middle end");
        run("alpha -> beta, beta -> gamma, gamma -> delta", true).include("alpha beta gamma delta");
        run("node1 -> node2, node2 -> node3, node1 -> node3", true).include("node1 node2 node3");
        run("input -> process, process -> output", true).include("input process output");
        run("source -> transform1, source -> transform2, transform1 -> sink, transform2 -> sink", true).include("source transform1 transform2 sink");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        // Дополнительные тесты
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes").exclude("no");
        run("a -> b, b -> c, c -> d, d -> a", true).include("yes").exclude("no");
        run("start -> middle, middle -> end", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z", true).include("no").exclude("yes");
        run("node1 -> node2, node2 -> node3, node3 -> node1", true).include("yes").exclude("no");
        run("input -> process, process -> output", true).include("no").exclude("yes");
        run("alpha -> beta, beta -> gamma, gamma -> alpha", true).include("yes").exclude("no");
        run("source -> transform, transform -> sink", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 1", true).include("yes").exclude("no");
        run("start -> step1, step1 -> step2, step2 -> finish", true).include("no").exclude("yes");
        run("X1 -> X2, X2 -> X3, X3 -> X1", true).include("yes").exclude("no");
        run("a -> b, b -> c, c -> a, c -> d", true).include("yes").exclude("no");
        run("first -> second, second -> third", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0, 2 -> 3", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 1, 4 -> 5", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
    }
}