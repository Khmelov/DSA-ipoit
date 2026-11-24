package by.it.group510971.rudko.lesson14;

import java.util.Scanner;

//Кластеры точек в 3D: точки с расстоянием меньше D объединяются в один кластер (DSU).
//Ввод: "D N", затем N строк "X Y Z". Вывод: размеры кластеров по убыванию (как в тестах).
public class PointsA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long d = scanner.nextLong();
        int n = scanner.nextInt();
        long[][] points = new long[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                points[i][j] = scanner.nextLong();
            }
        }
        DisjointSetUnion dsu = new DisjointSetUnion(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = points[i][0] - points[j][0];
                long dy = points[i][1] - points[j][1];
                long dz = points[i][2] - points[j][2];
                //расстояние строго меньше D  <=>  квадрат расстояния строго меньше D^2
                if (dx * dx + dy * dy + dz * dz < d * d) {
                    dsu.union(i, j);
                }
            }
        }
        int[] sizes = dsu.componentSizesAscending();
        StringBuilder out = new StringBuilder();
        for (int i = sizes.length - 1; i >= 0; i--) {
            out.append(sizes[i]);
            if (i > 0) out.append(' ');
        }
        System.out.println(out);
    }
}
