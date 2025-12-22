package by.it.group410972.kapur.lesson13;

import by.it.group410972.kapur.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13Test extends HomeWork {

    @Test
    public void testGraphA() {
        // исходные
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
        run("X -> Y, Y -> Z, X -> Z").include("X Y Z");
        run("M -> N, N -> O, O -> P, M -> P").include("M N O P");

        run("1 -> 2, 1 -> 3, 3 -> 4").include("1 2 3 4");
        run("A -> C, B -> C").include("A B C");
        run("A -> B, B -> C, A -> C").include("A B C");
        run("K -> L, L -> M, K -> M, M -> N").include("K L M N");
        run("1 -> 3, 2 -> 3").include("1 2 3");
        run("A -> B, C -> D").include("A B C D");
        run("X -> Y").include("X Y");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5").include("1 2 3 4 5");
        run("A -> D, B -> D, C -> D").include("A B C D");
        run("Q -> R, Q -> S, R -> T, S -> T").include("Q R S T");
    }

    @Test
    public void testGraphB() {
        // исходные
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D").include("no").exclude("yes");
        run("A -> B, B -> C, C -> A, D -> E").include("yes").exclude("no");

        run("1 -> 2, 2 -> 3, 3 -> 4").include("no");
        run("A -> B, B -> A").include("yes");
        run("A -> B, B -> C, C -> D, D -> B").include("yes");
        run("X -> Y, Y -> Z").include("no");
        run("X -> Y, Y -> Z, Z -> X").include("yes");
        run("1 -> 2, 2 -> 3, 3 -> 1, 4 -> 5").include("yes");
        run("A -> B, C -> D").include("no");
        run("M -> N, N -> O, O -> M").include("yes");
        run("P -> Q").include("no");
        run("A -> B, B -> C, C -> D, D -> E, E -> A").include("yes");
    }

    @Test
    public void testGraphC() {
        // исходные
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
            .include("123\n456");

        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
            .include("C\nABDHI\nE\nFGK");

        run("A->B, B->C, C->A, D->E").include("D\nABC\nE");
        run("X->Y, Y->Z, Z->X, P->Q, Q->R").include("P\nXYZ\nQ\nR");

        run("A->A").include("A");
        run("1->2, 2->1").include("12");
        run("A->B, B->C, C->D").include("A\nB\nC\nD");
        run("A->B, B->A, C->D, D->C").include("AB\nCD");
        run("1->2, 2->3, 3->1, 4->5").include("4\n123\n5");
        run("M->N, N->O, O->M").include("MNO");
        run("A->B, B->C, C->A, D->E, E->F, F->D").include("ABC\nDEF");
        run("X->Y").include("X\nY");
        run("1->2, 2->3, 3->4, 4->2").include("1\n234");
        run("A->B, C->D, E->F").include("A\nC\nE\nB\nD\nF");
    }
}