package by.it.group410972.popova.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        String find(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        void union(String a, String b) {
            a = find(a);
            b = find(b);
            if (a.equals(b)) return;
            if (size.get(a) < size.get(b)) {
                String tmp = a;
                a = b;
                b = tmp;
            }
            parent.put(b, a);
            size.put(a, size.get(a) + size.get(b));
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DSU dsu = new DSU();

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            String[] sites = line.split("\\+");
            String s1 = sites[0].trim();
            String s2 = sites[1].trim();
            dsu.union(s1, s2);
        }

        Map<String, Integer> clusterSizes = new HashMap<>();
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            clusterSizes.put(root, dsu.size.get(root));
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes);

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i < sizes.size() - 1) System.out.print(" ");
        }
    }
}
