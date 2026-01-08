package by.it.group410971.lukashonok.lesson14;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;

public class PointsA {

    private static class FastScanner {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr;
        private int len;

        private int read() throws IOException {
            if (ptr >= len) {
                len = System.in.read(buffer);
                ptr = 0;
                if (len <= 0) {
                    return -1;
                }
            }
            return buffer[ptr++];
        }

        long nextLong() throws IOException {
            int c;
            do {
                c = read();
            } while (c <= 32 && c != -1);
            if (c == -1) {
                return Long.MIN_VALUE;
            }
            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }
            long value = 0;
            while (c > 32 && c != -1) {
                value = value * 10 + (c - '0');
                c = read();
            }
            return value * sign;
        }
    }

    private static class DSU {
        private final int[] parent;
        private final int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int a, int b) {
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

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        long dValue = fs.nextLong();
        if (dValue == Long.MIN_VALUE) {
            return;
        }
        int n = (int) fs.nextLong();
        int[] xs = new int[n];
        int[] ys = new int[n];
        int[] zs = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = (int) fs.nextLong();
            ys[i] = (int) fs.nextLong();
            zs[i] = (int) fs.nextLong();
        }

        long d2 = dValue * dValue;
        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = xs[i] - xs[j];
                long dy = ys[i] - ys[j];
                long dz = zs[i] - zs[j];
                long dist2 = dx * dx + dy * dy + dz * dz;
                if (dist2 < d2) {
                    dsu.union(i, j);
                }
            }
        }

        int[] sizes = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                sizes[count++] = dsu.size[i];
            }
        }
        Arrays.sort(sizes, 0, count);
        StringBuilder out = new StringBuilder();
        for (int i = count - 1; i >= 0; i--) {
            if (out.length() > 0) {
                out.append(' ');
            }
            out.append(sizes[i]);
        }
        System.out.print(out);
    }
}
