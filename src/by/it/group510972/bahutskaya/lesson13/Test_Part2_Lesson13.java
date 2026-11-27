package by.it.group510972.bahutskaya.lesson13;

import by.it.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 extends HomeWork {

    @Test
    public void testTaskA() {
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("0 1 2 3");
        run("A -> B", true).include("A B");
        run("B -> A", true).include("B A");
        run("A -> C, B -> C", true).include("A B C");
        run("C -> A, C -> B", true).include("C A B");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4", true).include("1 2 3 4");
        run("Z -> Y, Y -> X", true).include("Z Y X");
        run("A -> B, B -> C, A -> C", true).include("A B C");
        run("M -> N, M -> O, N -> P, O -> P", true).include("M N O P");
        run("0 -> 2, 0 -> 1, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("B -> C, A -> B", true).include("A B C");
        run("D -> E, C -> D, B -> C, A -> B", true).include("A B C D E");
    }

    @Test
    public void testTaskB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("A -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 3 -> 4", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> B", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 1, 2 -> 3", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes").exclude("no");
    }

    @Test
    public void testTaskC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->B", true).include("A\nB");
        run("A->B, B->A", true).include("AB");
        run("A->B, B->C", true).include("A\nB\nC");
        run("A->B, B->C, C->A", true).include("ABC");
        run("A->B, C->D", true).include("A\nB\nC\nD");
        run("A->B, B->A, B->C", true).include("AB\nC");
        run("X->Y, Y->Z, Z->Y", true).include("X\nYZ");
    }

}
