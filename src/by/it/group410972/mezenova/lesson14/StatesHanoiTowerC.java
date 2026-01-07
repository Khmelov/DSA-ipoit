package by.it.group410972.mezenova.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

    static class State {
        int a, b, c;
        State(int a, int b, int c) { this.a = a; this.b = b; this.c = c; }
    }

    static List<State> steps = new ArrayList<>();

    static void hanoi(int n, int from, int to, int aux, int[] heights) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to, heights);

        heights[from]--;
        heights[to]++;
        steps.add(new State(heights[0], heights[1], heights[2]));

        hanoi(n - 1, aux, to, from, heights);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] heights = new int[]{N, 0, 0};
        steps.clear();

        hanoi(N, 0, 1, 2, heights);

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (State s : steps) {
            int maxH = Math.max(s.a, Math.max(s.b, s.c));
            clusterSizes.put(maxH, clusterSizes.getOrDefault(maxH, 0) + 1);
        }

        int[] sizes = new int[clusterSizes.size()];
        int idx = 0;
        for (int sz : clusterSizes.values()) sizes[idx++] = sz;
        Arrays.sort(sizes);
        for (int sz : sizes) System.out.print(sz + " ");
    }
}
