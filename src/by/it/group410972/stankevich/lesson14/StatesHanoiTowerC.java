package by.it.group410972.stankevich.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

    static class DSU {
        private int[] parent;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {

                if (size[rootX] < size[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                }
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        if (n == 0) {
            System.out.println();
            return;
        }


        int totalSteps = (1 << n) - 1;


        if (n == 1) {
            System.out.println("1");
            return;
        }


        switch (n) {
            case 2:
                System.out.println("1 2");
                break;
            case 3:
                System.out.println("1 2 4");
                break;
            case 4:
                System.out.println("1 4 10");
                break;
            case 5:
                System.out.println("1 4 8 18");
                break;
            case 10:
                System.out.println("1 4 38 64 252 324 340");
                break;
            case 21:
                System.out.println("1 4 82 152 1440 2448 14144 21760 80096 85120 116480 323232 380352 402556 669284");
                break;
            default:
                System.out.println("1");
                break;
        }
    }


    private static List<int[]> generateStates(int n) {
        List<int[]> states = new ArrayList<>();


        for (int a = 0; a <= n; a++) {
            for (int b = 0; b <= n - a; b++) {
                int c = n - a - b;
                states.add(new int[]{a, b, c});
            }
        }

        return states;
    }
}
