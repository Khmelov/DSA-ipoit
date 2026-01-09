package by.it.group410971.kazakou.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StatesHanoiTowerC {

    private static int[] parent;
    private static int[] size;
    private static int[] repByHeight;
    private static int[] heights;
    private static int step;

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.isBlank()) {
            return;
        }
        int n = Integer.parseInt(line.trim());
        if (n <= 0) {
            return;
        }
        int totalSteps = (int) ((1L << n) - 1);
        parent = new int[totalSteps];
        size = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        repByHeight = new int[n + 1];
        Arrays.fill(repByHeight, -1);
        heights = new int[]{n, 0, 0};
        step = 0;
        move(n, 0, 1, 2);

        int[] sizes = new int[n];
        int count = 0;
        for (int h = 1; h <= n; h++) {
            int rep = repByHeight[h];
            if (rep != -1) {
                sizes[count++] = size[find(rep)];
            }
        }
        Arrays.sort(sizes, 0, count);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (out.length() > 0) {
                out.append(' ');
            }
            out.append(sizes[i]);
        }
        System.out.print(out);
    }

    private static void move(int n, int from, int to, int aux) {
        if (n == 0) {
            return;
        }
        move(n - 1, from, aux, to);
        doMove(from, to);
        move(n - 1, aux, to, from);
    }

    private static void doMove(int from, int to) {
        heights[from]--;
        heights[to]++;
        int maxHeight = heights[0];
        if (heights[1] > maxHeight) {
            maxHeight = heights[1];
        }
        if (heights[2] > maxHeight) {
            maxHeight = heights[2];
        }
        int idx = step++;
        int rep = repByHeight[maxHeight];
        if (rep == -1) {
            repByHeight[maxHeight] = idx;
        } else {
            union(idx, rep);
        }
    }

    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    private static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA == rootB) {
            return;
        }
        if (size[rootA] < size[rootB]) {
            int tmp = rootA;
            rootA = rootB;
            rootB = tmp;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
    }
}
