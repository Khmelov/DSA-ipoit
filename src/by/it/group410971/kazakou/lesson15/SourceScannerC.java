package by.it.group410971.kazakou.lesson15;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerC {

    public static void main(String[] args) throws Exception {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<String> paths = new ArrayList<>();
        List<String> texts = new ArrayList<>();
        try (var walk = Files.walk(root)) {
            walk.forEach(path -> {
                if (path.toString().endsWith(".java")) {
                    String text = readFile(path);
                    if (text == null) {
                        return;
                    }
                    if (text.contains("@Test") || text.contains("org.junit.Test")) {
                        return;
                    }
                    String stripped = stripPackageImports(text);
                    stripped = removeComments(stripped);
                    stripped = normalizeSpaces(stripped);
                    String rel = root.relativize(path).toString();
                    paths.add(rel);
                    texts.add(stripped);
                }
            });
        }

        int n = paths.size();
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            order.add(i);
        }
        order.sort(Comparator.comparing(paths::get));

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < order.size(); i++) {
            int idx = order.get(i);
            List<String> copies = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (j == idx) {
                    continue;
                }
                if (isCopy(texts.get(idx), texts.get(j))) {
                    copies.add(paths.get(j));
                }
            }
            if (!copies.isEmpty()) {
                copies.sort(String::compareTo);
                out.append(paths.get(idx)).append('\n');
                for (String copy : copies) {
                    out.append(copy).append('\n');
                }
            }
        }

        if (out.length() > 0) {
            out.setLength(out.length() - 1);
        }
        System.out.print(out);
    }

    private static String readFile(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line).append('\n');
            }
            return out.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static String stripPackageImports(String text) {
        StringBuilder out = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                    continue;
                }
                out.append(line).append('\n');
            }
        } catch (IOException e) {
            return text;
        }
        return out.toString();
    }

    private static String removeComments(String text) {
        StringBuilder out = new StringBuilder();
        boolean inLine = false;
        boolean inBlock = false;
        boolean inString = false;
        boolean inChar = false;
        boolean escape = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char next = i + 1 < text.length() ? text.charAt(i + 1) : '\0';
            if (inLine) {
                if (c == '\n') {
                    inLine = false;
                    out.append(c);
                }
                continue;
            }
            if (inBlock) {
                if (c == '*' && next == '/') {
                    inBlock = false;
                    i++;
                }
                continue;
            }
            if (inString) {
                out.append(c);
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }
            if (inChar) {
                out.append(c);
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }
            if (c == '"') {
                inString = true;
                out.append(c);
                continue;
            }
            if (c == '\'') {
                inChar = true;
                out.append(c);
                continue;
            }
            if (c == '/' && next == '/') {
                inLine = true;
                i++;
                continue;
            }
            if (c == '/' && next == '*') {
                inBlock = true;
                i++;
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }

    private static String normalizeSpaces(String text) {
        StringBuilder out = new StringBuilder();
        boolean inSpace = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!inSpace) {
                    out.append(' ');
                    inSpace = true;
                }
            } else {
                out.append(c);
                inSpace = false;
            }
        }
        return out.toString().trim();
    }

    private static boolean isCopy(String a, String b) {
        if (Math.abs(a.length() - b.length()) >= 10) {
            return false;
        }
        return levenshteinLimited(a, b, 10) < 10;
    }

    private static int levenshteinLimited(String a, String b, int limit) {
        int n = a.length();
        int m = b.length();
        if (n == 0) {
            return Math.min(m, limit);
        }
        if (m == 0) {
            return Math.min(n, limit);
        }
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }
        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            int minRow = curr[0];
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                int cost = ca == b.charAt(j - 1) ? 0 : 1;
                int val = prev[j] + 1;
                int ins = curr[j - 1] + 1;
                int sub = prev[j - 1] + cost;
                if (ins < val) {
                    val = ins;
                }
                if (sub < val) {
                    val = sub;
                }
                curr[j] = val;
                if (val < minRow) {
                    minRow = val;
                }
            }
            if (minRow >= limit) {
                return limit;
            }
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[m];
    }
}
