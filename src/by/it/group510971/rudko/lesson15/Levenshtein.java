package by.it.group510971.rudko.lesson15;

//Расстояние Левенштейна с ограничением: вычисляется только лента шириной 2k+1 вокруг диагонали
//(Ukkonen), поэтому время O(n*k) вместо O(n*m); если минимум строки превысил k, досрочно останавливаемся.
class Levenshtein {

    //true, если расстояние между a и b не больше k
    static boolean withinLimit(String a, String b, int k) {
        int n = a.length();
        int m = b.length();
        if (Math.abs(n - m) > k) return false;
        final int inf = k + 1;
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        for (int j = 0; j <= m; j++) {
            prev[j] = (j <= k) ? j : inf;
        }
        for (int i = 1; i <= n; i++) {
            int from = Math.max(1, i - k);
            int to = Math.min(m, i + k);
            cur[0] = (i <= k) ? i : inf;
            int rowMin = cur[0];
            if (from > 1) cur[from - 1] = inf;
            for (int j = from; j <= to; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                int best = prev[j - 1] + cost;
                best = Math.min(best, prev[j] + 1);
                best = Math.min(best, cur[j - 1] + 1);
                cur[j] = Math.min(best, inf);
                rowMin = Math.min(rowMin, cur[j]);
            }
            if (to < m) cur[to + 1] = inf;
            if (rowMin > k) return false;
            int[] t = prev;
            prev = cur;
            cur = t;
        }
        return prev[m] <= k;
    }
}
