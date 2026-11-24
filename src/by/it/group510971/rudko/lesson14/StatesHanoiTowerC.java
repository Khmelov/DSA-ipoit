package by.it.group510971.rudko.lesson14;

import java.util.Arrays;
import java.util.Scanner;

//Ханойские башни: перенос пирамиды из N колец со стержня A на B (через C).
//Каждый ход - это "состояние" (сейчас только высоты стержней). Ходы с одинаковой наибольшей высотой
//объединяем в одно множество DSU. Выводим размеры множеств по возрастанию.
//Коллекций не используем: только массивы и собственная DSU.
public class StatesHanoiTowerC {

    private static int[] heights = new int[3];      //высоты стержней A=0, B=1, C=2
    private static int[] representativeByHeight;     //первый ход с данной наибольшей высотой
    private static DisjointSetUnion dsu;
    private static int moveIndex;

    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        System.out.println(solve(n));
    }

    static String solve(int n) {
        int moves = (1 << n) - 1;                    //число ходов в решении
        dsu = new DisjointSetUnion(moves);
        representativeByHeight = new int[n + 1];
        Arrays.fill(representativeByHeight, -1);
        heights = new int[]{n, 0, 0};
        moveIndex = 0;
        hanoi(n, 0, 1, 2);

        int[] sizes = dsu.componentSizesAscending();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < sizes.length; i++) {
            out.append(sizes[i]);
            if (i < sizes.length - 1) out.append(' ');
        }
        return out.toString();
    }

    //перенос n колец с stick "from" на "to" через "via"
    private static void hanoi(int n, int from, int to, int via) {
        if (n == 0) return;
        hanoi(n - 1, from, via, to);
        move(from, to);
        hanoi(n - 1, via, to, from);
    }

    private static void move(int from, int to) {
        heights[from]--;
        heights[to]++;
        int highest = Math.max(heights[0], Math.max(heights[1], heights[2]));
        if (representativeByHeight[highest] == -1) {
            representativeByHeight[highest] = moveIndex;
        } else {
            dsu.union(moveIndex, representativeByHeight[highest]);
        }
        moveIndex++;
    }
}
