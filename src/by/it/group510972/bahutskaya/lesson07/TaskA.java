package by.it.group510972.bahutskaya.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class TaskA {

    int getDistanceEdinting(String one, String two) {
        int[][] memo = new int[one.length() + 1][two.length() + 1];
        for (int[] row : memo) {
            java.util.Arrays.fill(row, -1);
        }
        return distance(one, two, one.length(), two.length(), memo);
    }

    private int distance(String one, String two, int i, int j, int[][] memo) {
        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        // Считаем замену, удаление и вставку и берём дешевле
        int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;
        int replace = distance(one, two, i - 1, j - 1, memo) + cost;
        int delete = distance(one, two, i - 1, j, memo) + 1;
        int insert = distance(one, two, i, j - 1, memo) + 1;
        int best = Math.min(replace, Math.min(delete, insert));
        memo[i][j] = best;
        return best;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskA.class.getResourceAsStream("dataABC.txt");
        TaskA instance = new TaskA();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
