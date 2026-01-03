package by.it.group410971.usovskiy.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class SitesB {

    // ===== DSU (Union-Find) с эвристикой по размеру + сжатие путей =====
    private static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size   = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);      // сжатие пути
            }
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            // эвристика по размеру поддерева
            if (size[ra] < size[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Integer> id = new HashMap<>();
        List<int[]> edges = new ArrayList<>();
        int idxCounter = 0;

        while (true) {
            String line = br.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;

            int plusPos = line.indexOf('+');
            if (plusPos < 0) continue; // строка невалидная, пропускаем

            String s1 = line.substring(0, plusPos);
            String s2 = line.substring(plusPos + 1);

            Integer id1 = id.get(s1);
            if (id1 == null) {
                id1 = idxCounter++;
                id.put(s1, id1);
            }
            Integer id2 = id.get(s2);
            if (id2 == null) {
                id2 = idxCounter++;
                id.put(s2, id2);
            }

            edges.add(new int[]{id1, id2});
        }

        int n = idxCounter;
        if (n == 0) {
            return; // нет сайтов
        }

        DSU dsu = new DSU(n);

        // объединяем по рёбрам
        for (int[] e : edges) {
            dsu.union(e[0], e[1]);
        }

        // считаем размеры компонент
        int[] compSizesRaw = new int[n];
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            compSizesRaw[root]++;
        }

        // собираем только положительные размеры
        int count = 0;
        for (int s : compSizesRaw) {
            if (s > 0) count++;
        }

        int[] sizes = new int[count];
        int k = 0;
        for (int s : compSizesRaw) {
            if (s > 0) sizes[k++] = s;
        }

        // сортировка по убыванию
        for (int i = 0; i < sizes.length - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < sizes.length; j++) {
                if (sizes[j] > sizes[maxIdx]) {
                    maxIdx = j;
                }
            }
            int tmp = sizes[i];
            sizes[i] = sizes[maxIdx];
            sizes[maxIdx] = tmp;
        }

        // вывод размеров кластеров
        StringBuilder out = new StringBuilder();
        out.append(sizes[0]);
        for (int i = 1; i < sizes.length; i++) {
            out.append(' ').append(sizes[i]);
        }
        System.out.println(out.toString());
    }
}
