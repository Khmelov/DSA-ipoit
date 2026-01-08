package by.it.group410971.lukashonok.lesson13;

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
        run("3 -> 4", true).include("3 4");
        run("B -> C, A -> C", true).include("A B C");
        run("A -> D, B -> D, C -> D", true).include("A B C D");
        run("2 -> 4, 1 -> 4, 0 -> 1, 0 -> 2", true).include("0 1 2 4");
        run("1 -> 2, 1 -> 3, 3 -> 4", true).include("1 2 3 4");
        run("A -> B, B -> C, A -> C, C -> D", true).include("A B C D");
        run("X -> Z, Y -> Z", true).include("X Y Z");
        run("5 -> 7, 5 -> 6, 6 -> 7", true).include("5 6 7");
        run("M -> N, A -> N, A -> M", true).include("A M N");
        run("1 -> 4, 2 -> 4, 3 -> 4", true).include("1 2 3 4");
        run("0 -> 3, 1 -> 3, 1 -> 2", true).include("0 1 2 3");
        run("A -> C, B -> C, B -> D", true).include("A B C D");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3", true).include("no").exclude("yes");
        run("A -> A", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> B", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X, Z -> W", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->B, B->A", true).include("AB");
        run("A->B, B->C, C->A, C->D", true).include("ABC\nD");
        run("1->2, 2->1, 2->3, 3->4, 4->3", true).include("12\n34");
        run("A->B, B->C, C->D", true).include("A\nB\nC\nD");
        run("M->N, N->O, O->M, P->Q, Q->P, O->P", true).include("MNO\nPQ");
        run("X->Y, Y->Z, Z->Y, Z->W", true).include("X\nYZ\nW");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}
