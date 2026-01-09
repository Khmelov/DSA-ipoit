package by.it.group410971.dulebo.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Set<String>> graph = new HashMap<>();

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;

            String[] parts = line.split("\\+");
            String a = parts[0];
            String b = parts[1];

            graph.putIfAbsent(a, new HashSet<>());
            graph.putIfAbsent(b, new HashSet<>());

            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        Set<String> visited = new HashSet<>();
        List<Integer> sizes = new ArrayList<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {

                int count = 0;
                Queue<String> q = new LinkedList<>();
                q.add(node);
                visited.add(node);

                while (!q.isEmpty()) {
                    String cur = q.poll();
                    count++;
                    for (String neigh : graph.get(cur)) {
                        if (!visited.contains(neigh)) {
                            visited.add(neigh);
                            q.add(neigh);
                        }
                    }
                }

                sizes.add(count);
            }
        }

        Collections.sort(sizes, Collections.reverseOrder());

        for (int s : sizes) {
            System.out.print(s + " ");
        }
    }
}
