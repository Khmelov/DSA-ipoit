package by.it.group510971.rudko.lesson14;

import java.util.Arrays;

//Система непересекающихся множеств (DSU) на массивах.
//Две эвристики: объединение по размеру поддерева и сокращение пути (path halving) при поиске.
class DisjointSetUnion {

    private final int[] parent;
    private final int[] size;

    DisjointSetUnion(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];   //сокращение пути: ссылка перескакивает через уровень
            x = parent[x];
        }
        return x;
    }

    //объединяет множества элементов a и b; возвращает false, если они уже были вместе
    boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return false;
        if (size[ra] < size[rb]) {        //меньшее дерево подвешиваем к большему
            int t = ra;
            ra = rb;
            rb = t;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
        return true;
    }

    //размеры всех множеств по возрастанию
    int[] componentSizesAscending() {
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) count++;
        }
        int[] result = new int[count];
        int k = 0;
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) result[k++] = size[i];
        }
        Arrays.sort(result);
        return result;
    }
}
