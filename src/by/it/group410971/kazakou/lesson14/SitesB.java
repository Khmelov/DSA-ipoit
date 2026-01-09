package by.it.group410971.kazakou.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SitesB {

    private static class DSU {
        int[] parent;
        int[] size;
        int count;

        DSU(int capacity) {
            parent = new int[capacity];
            size = new int[capacity];
            count = 0;
        }

        int add() {
            ensureCapacity(count + 1);
            parent[count] = count;
            size[count] = 1;
            return count++;
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

        private void ensureCapacity(int minCapacity) {
            if (minCapacity <= parent.length) {
                return;
            }
            int newCapacity = Math.max(minCapacity, parent.length * 2);
            parent = Arrays.copyOf(parent, newCapacity);
            size = Arrays.copyOf(size, newCapacity);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> ids = new HashMap<>();
        DSU dsu = new DSU(16);
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            if ("end".equals(line)) {
                break;
            }
            String[] parts = line.split("\\+");
            if (parts.length != 2) {
                continue;
            }
            int a = ids.computeIfAbsent(parts[0], k -> dsu.add());
            int b = ids.computeIfAbsent(parts[1], k -> dsu.add());
            dsu.union(a, b);
        }

        int[] sizes = new int[dsu.count];
        int count = 0;
        for (int i = 0; i < dsu.count; i++) {
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
