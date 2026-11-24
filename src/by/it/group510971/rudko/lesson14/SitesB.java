package by.it.group510971.rudko.lesson14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//Кластеры сайтов по гиперссылкам: пары "a+b" объединяются, ввод завершается строкой "end".
//Вывод: размеры кластеров по убыванию (как в тестах и примере условия).
public class SitesB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> ids = new HashMap<>();
        List<int[]> links = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equals("end")) break;
            String[] pair = line.split("\\+");
            int a = idOf(ids, pair[0].trim());
            int b = idOf(ids, pair[1].trim());
            links.add(new int[]{a, b});
        }
        DisjointSetUnion dsu = new DisjointSetUnion(ids.size());
        for (int[] link : links) {
            dsu.union(link[0], link[1]);
        }
        int[] sizes = dsu.componentSizesAscending();
        StringBuilder out = new StringBuilder();
        for (int i = sizes.length - 1; i >= 0; i--) {
            out.append(sizes[i]);
            if (i > 0) out.append(' ');
        }
        System.out.println(out);
    }

    private static int idOf(Map<String, Integer> ids, String site) {
        Integer id = ids.get(site);
        if (id == null) {
            id = ids.size();
            ids.put(site, id);
        }
        return id;
    }
}
