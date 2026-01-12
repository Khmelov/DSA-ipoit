package by.it.group410972.Pushkarev.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitesB {
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
                parent[x] = find(parent[x]); // path compression
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;

            // union by size
            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }
            parent[b] = a;
            size[a] += size[b];
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Integer> id = new HashMap<>();
        List<int[]> edges = new ArrayList<>();

        String line;
        int idx = 0;

        while (!(line = br.readLine()).equals("end")) {
            String[] parts = line.split("\\+");
            String a = parts[0];
            String b = parts[1];

            if (!id.containsKey(a))
                id.put(a, idx++);
            if (!id.containsKey(b))
                id.put(b, idx++);

            edges.add(new int[]{id.get(a), id.get(b)});
        }

        DSU dsu = new DSU(idx);

        for (int[] e : edges) {
            dsu.union(e[0], e[1]);
        }

        Map<Integer, Integer> components = new HashMap<>();
        for (int i = 0; i < idx; i++) {
            int root = dsu.find(i);
            components.put(root, components.getOrDefault(root, 0) + 1);
        }

        List<Integer> result = new ArrayList<>(components.values());
        result.sort(Collections.reverseOrder());

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }
}
