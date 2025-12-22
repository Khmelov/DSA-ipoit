package by.it.group410972.kapur.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

  static int[] parent;
  static int[] size;
  static int[] maxHeight;

  static int step = 0;

  static int[] towers = new int[4];

  static int find(int x) {
    if (parent[x] != x)
      parent[x] = find(parent[x]);
    return parent[x];
  }

  static void union(int a, int b) {
    a = find(a);
    b = find(b);
    if (a == b) return;

    if (size[a] < size[b]) {
      int t = a;
      a = b;
      b = t;
    }

    parent[b] = a;
    size[a] += size[b];
  }

  static void move(int from, int to) {
    towers[from]--;
    towers[to]++;

    int m = towers[1];
    if (towers[2] > m) m = towers[2];
    if (towers[3] > m) m = towers[3];

    maxHeight[step++] = m;
  }

  static void hanoi(int n, int from, int to, int aux) {
    if (n == 0) return;
    hanoi(n - 1, from, aux, to);
    move(from, to);
    hanoi(n - 1, aux, to, from);
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();

    int moves = (1 << N) - 1;

    parent = new int[moves];
    size = new int[moves];
    maxHeight = new int[moves];

    for (int i = 0; i < moves; i++) {
      parent[i] = i;
      size[i] = 1;
    }

    step = 0;
    towers[1] = N;
    towers[2] = 0;
    towers[3] = 0;

    hanoi(N, 1, 3, 2);


    int[] first = new int[N + 1];
    for (int i = 0; i <= N; i++) first[i] = -1;

    for (int i = 0; i < moves; i++) {
      int m = maxHeight[i];
      if (first[m] == -1)
        first[m] = i;
      else
        union(i, first[m]);
    }

    int[] res = new int[N + 1];
    int cnt = 0;

    for (int i = 0; i < moves; i++) {
      if (parent[i] == i)
        res[cnt++] = size[i];
    }

    // простая сортировка без коллекций
    for (int i = 0; i < cnt; i++) {
      for (int j = i + 1; j < cnt; j++) {
        if (res[i] > res[j]) {
          int t = res[i];
          res[i] = res[j];
          res[j] = t;
        }
      }
    }

    for (int i = 0; i < cnt; i++) {
      if (i > 0) System.out.print(" ");
      System.out.print(res[i]);
    }
  }
}
