package by.it.group410971.dulebo.lesson14;

import java.util.*;

public class PointsA {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            if (size[ra] < size[rb]) {
                int t = ra; ra = rb; rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    static class Point {
        double x, y, z;

        Point(double x, double y, double z) {
            this.x = x; this.y = y; this.z = z;
        }

        double dist(Point o) {
            double dx = x - o.x;
            double dy = y - o.y;
            double dz = z - o.z;
            return Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();
        int N = sc.nextInt();

        Point[] pts = new Point[N];
        for (int i = 0; i < N; i++) {
            pts[i] = new Point(sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                if (pts[i].dist(pts[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer,Integer> clusters = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int r = dsu.find(i);
            clusters.put(r, dsu.size[r]);
        }

        List<Integer> ans = new ArrayList<>(clusters.values());
        ans.sort(Collections.reverseOrder());

        for (int i = 0; i < ans.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(ans.get(i));
        }
        System.out.println();
    }
}
