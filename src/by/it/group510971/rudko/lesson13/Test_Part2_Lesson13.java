package by.it.group510971.rudko.lesson13;

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
        //свои примеры (лексикографический порядок среди готовых вершин)
        run("3 -> 1, 1 -> 2", true).include("3 1 2");
        run("b -> a", true).include("b a");
        run("5 -> 1, 5 -> 2, 5 -> 3", true).include("5 1 2 3");
        run("2 -> 1, 3 -> 1", true).include("2 3 1");
        run("a -> b, b -> c, c -> d, d -> e", true).include("a b c d e");
        run("0 -> 1, 0 -> 2, 0 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("x -> z, y -> z", true).include("x y z");
        run("1 -> 2, 2 -> 3, 1 -> 3, 3 -> 4", true).include("1 2 3 4");
        run("10 -> 2, 2 -> 3", true).include("10 2 3");
        run("m -> n, n -> o, m -> o", true).include("m n o");
        run("a -> b, c -> b, d -> a", true).include("c d a b");
        run("q -> w, e -> r, t -> y", true).include("e q r t w y");
        run("k -> l", true).include("k l");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 21).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //свои примеры
        run("0 -> 0", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 1, 3 -> 4", true).include("yes").exclude("no");
        run("a -> b, b -> a", true).include("yes").exclude("no");
        run("1 -> 2, 3 -> 4, 4 -> 3", true).include("yes").exclude("no");
        run("5 -> 1, 5 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("p -> q, q -> r, r -> s, s -> q, s -> t", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 1 -> 3, 3 -> 2", true).include("yes").exclude("no");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 13).
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        //свои примеры
        run("1->2, 2->1", true).include("12");
        run("a->b, b->c, c->a", true).include("abc");
        run("a->b, b->c", true).include("a\nb\nc");
        run("1->2, 2->3, 3->4, 4->3", true).include("1\n2\n34");
        run("A->B, B->A, B->C", true).include("AB\nC");
        run("x->y, y->z, z->x, z->w", true).include("xyz\nw");
        run("1->1", true).include("1");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 9).
    }


}