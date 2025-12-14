package by.it.group410972.popova.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.util.*;

public class SourceScannerC {

    static class FileText {
        String path;
        String text;
        FileText(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileText> files = new ArrayList<>();
        scanDirectory(new File(src), src, files);

        Map<String, List<String>> copies = new TreeMap<>();
        for (int i = 0; i < files.size(); i++) {
            for (int j = i + 1; j < files.size(); j++) {
                int dist = levenshtein(files.get(i).text, files.get(j).text);
                if (dist < 10) {
                    copies.computeIfAbsent(files.get(i).path, k -> new ArrayList<>()).add(files.get(j).path);
                    copies.computeIfAbsent(files.get(j).path, k -> new ArrayList<>()).add(files.get(i).path);
                }
            }
        }

        for (String path : copies.keySet()) {
            System.out.println(path);
            List<String> list = copies.get(path);
            Collections.sort(list);
            for (String copy : list) {
                System.out.println(copy);
            }
        }
    }

    static void scanDirectory(File dir, String src, List<FileText> results) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                scanDirectory(f, src, results);
            } else if (f.getName().endsWith(".java")) {
                String text = processFile(f);
                if (text != null) {
                    String relativePath = f.getAbsolutePath().substring(src.length());
                    results.add(new FileText(relativePath, text));
                }
            }
        }
    }

    static String processFile(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (MalformedInputException mie) {
            return null;
        } catch (IOException e) {
            return null;
        }

        String joined = String.join("\n", lines);
        if (joined.contains("@Test") || joined.contains("org.junit.Test")) return null;

        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package") || trimmed.startsWith("import")) continue;
            sb.append(line).append("\n");
        }

        String text = sb.toString();
        text = text.replaceAll("//.*", "");
        text = text.replaceAll("/\\*.*?\\*/", "");

        StringBuilder normalized = new StringBuilder();
        boolean inSpace = false;
        for (char c : text.toCharArray()) {
            if (c < 33) {
                if (!inSpace) {
                    normalized.append(' ');
                    inSpace = true;
                }
            } else {
                normalized.append(c);
                inSpace = false;
            }
        }

        return normalized.toString().trim();
    }

    static int levenshtein(String a, String b) {
        int n = a.length();
        int m = b.length();
        if (n == 0) return m;
        if (m == 0) return n;

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[m];
    }
}
