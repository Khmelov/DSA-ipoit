package by.it.group510972.bahutskaya.lesson14;

import java.util.Arrays;
import java.util.Scanner;

// Ханойские башни. Шаги группируем по самой высокой башне.
public class TaskC {
    private final int[][] pegs;
    private final int[] height;
    private final int[] count;

    public TaskC(int disks) {
        pegs = new int[3][disks];
        height = new int[3];
        count = new int[disks + 1];
        for (int i = 0; i < disks; i++) {
            pegs[0][i] = disks - i;
        }
        height[0] = disks;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int disks = scanner.nextInt();
        TaskC tower = new TaskC(disks);
        tower.solve(disks, 0, 1, 2);
        int groups = 0;
        for (int value : tower.count) {
            if (value > 0) {
                groups++;
            }
        }
        int[] sizes = new int[groups];
        int index = 0;
        for (int value : tower.count) {
            if (value > 0) {
                sizes[index++] = value;
            }
        }
        // Сортируем размеры групп по возрастанию
        Arrays.sort(sizes);
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) {
                System.out.print(' ');
            }
            System.out.print(sizes[i]);
        }
        System.out.println();
    }

    private void solve(int disks, int from, int to, int aux) {
        if (disks == 0) {
            return;
        }
        solve(disks - 1, from, aux, to);
        move(from, to);
        solve(disks - 1, aux, to, from);
    }

    private void move(int from, int to) {
        // Перекладываем верхний диск и запоминаем высоту самой высокой башни
        int disk = pegs[from][--height[from]];
        pegs[to][height[to]++] = disk;
        int max = Math.max(height[0], Math.max(height[1], height[2]));
        count[max]++;
    }
}
