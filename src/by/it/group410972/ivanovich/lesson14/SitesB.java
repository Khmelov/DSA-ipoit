package by.it.group410972.ivanovich.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private Map<String, String> parent;
        private Map<String, Integer> rank;
        private Map<String, Integer> size;

        DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
            size = new HashMap<>();
        }

        String find(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
                return x;
            }

            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) return;

            if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
                size.put(rootY, size.get(rootY) + size.get(rootX));
            } else if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            } else {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }

        int getSize(String x) {
            return size.get(find(x));
        }

        Set<String> getRoots() {
            Set<String> roots = new HashSet<>();
            for (String site : parent.keySet()) {
                roots.add(find(site));
            }
            return roots;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }

            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();
                dsu.union(site1, site2);
            }
        }

        Set<String> roots = dsu.getRoots();
        List<Integer> clusterSizes = new ArrayList<>();

        for (String root : roots) {
            clusterSizes.add(dsu.getSize(root));
        }

        // Сортировка по убыванию (как в тесте)
        Collections.sort(clusterSizes, Collections.reverseOrder());

        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();
    }
}