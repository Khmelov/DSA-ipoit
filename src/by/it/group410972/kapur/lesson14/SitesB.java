package by.it.group410972.kapur.lesson14;

import java.util.*;

public class SitesB {

  static class DSU {
    List<Integer> parent = new ArrayList<>();
    List<Integer> size = new ArrayList<>();

    int makeSet() {
      int id = parent.size();
      parent.add(id);
      size.add(1);
      return id;
    }

    int find(int x) {
      if (parent.get(x) != x) {
        parent.set(x, find(parent.get(x)));
      }
      return parent.get(x);
    }

    void union(int a, int b) {
      a = find(a);
      b = find(b);
      if (a == b) return;

      if (size.get(a) < size.get(b)) {
        int t = a;
        a = b;
        b = t;
      }

      parent.set(b, a);
      size.set(a, size.get(a) + size.get(b));
    }
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    DSU dsu = new DSU();
    Map<String, Integer> id = new HashMap<>();

    while (true) {
      String line = sc.nextLine().trim();
      if (line.equals("end")) break;

      String[] pair = line.split("\\+");
      String a = pair[0];
      String b = pair.length > 1 ? pair[1] : pair[0];

      if (!id.containsKey(a)) id.put(a, dsu.makeSet());
      if (!id.containsKey(b)) id.put(b, dsu.makeSet());

      dsu.union(id.get(a), id.get(b));
    }

    Map<Integer, Integer> clusters = new HashMap<>();
    for (int x : id.values()) {
      int root = dsu.find(x);
      clusters.put(root, clusters.getOrDefault(root, 0) + 1);
    }

    List<Integer> result = new ArrayList<>(clusters.values());
    result.sort(Collections.reverseOrder());

    for (int i = 0; i < result.size(); i++) {
      if (i > 0) System.out.print(" ");
      System.out.print(result.get(i));
    }
  }
}
