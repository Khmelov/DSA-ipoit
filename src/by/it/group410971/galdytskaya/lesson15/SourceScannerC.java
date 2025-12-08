package by.it.group410971.galdytskaya.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        Map<String, String> fileToText = new TreeMap<>();

        try (var walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    String content = Files.readString(p);
                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        return;
                    }
                    String cleaned = cleanSource(content);
                    if (!cleaned.isEmpty()) {
                        String relativePath = root.relativize(p).toString();
                        fileToText.put(relativePath, cleaned);
                    }
                } catch (MalformedInputException mie) {
                    System.err.println("Ошибка кодировки файла, пропуск: " + p);
                } catch (IOException e) {
                    System.err.println("Ошибка чтения файла: " + p + " - " + e.getMessage());
                }
            });
        }

        Map<String, List<String>> copiesMap = findCopies(fileToText, 10);

        Set<String> filesWithCopies = copiesMap.keySet();

        filesWithCopies.stream()
                .sorted()
                .forEach(file -> {
                    System.out.println(file);
                    List<String> copies = copiesMap.get(file);
                    copies.stream().sorted().forEach(System.out::println);
                });

        fileToText.keySet().stream()
                .filter(file -> !filesWithCopies.contains(file))
                .sorted()
                .forEach(System.out::println);
    }

    private static String cleanSource(String text) {
        StringBuilder sb = new StringBuilder();
        String[] lines = text.split("\n");
        for (String line : lines) {
            String l = line.strip();
            if (l.startsWith("package ") || l.startsWith("import ")) {
                continue;
            }
            sb.append(line).append("\n");
        }

        String withoutPkgImp = sb.toString();
        String noComments = removeComments(withoutPkgImp);

        String replaced = noComments.chars()
                .map(c -> c < 33 ? 32 : c)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();

        return replaced.trim();
    }

    private static String removeComments(String src) {
        StringBuilder sb = new StringBuilder();
        int length = src.length();
        int i = 0;
        while (i < length) {
            char c = src.charAt(i);
            if (c == '/') {
                if (i + 1 < length) {
                    char c2 = src.charAt(i + 1);
                    if (c2 == '/') {
                        i += 2;
                        while (i < length && src.charAt(i) != '\n') i++;
                        continue;
                    }
                    if (c2 == '*') {
                        i += 2;
                        while (i + 1 < length && !(src.charAt(i) == '*' && src.charAt(i + 1) == '/')) {
                            i++;
                        }
                        i += 2;
                        continue;
                    }
                }
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    private static Map<String, List<String>> findCopies(Map<String, String> fileToText, int threshold) {
        Map<String, List<String>> result = new TreeMap<>();
        List<String> files = new ArrayList<>(fileToText.keySet());

        for (int i = 0; i < files.size(); i++) {
            String f1 = files.get(i);
            String t1 = fileToText.get(f1);
            List<String> copies = new ArrayList<>();
            for (int j = i + 1; j < files.size(); j++) {
                String f2 = files.get(j);
                String t2 = fileToText.get(f2);

                if (Math.abs(t1.length() - t2.length()) > threshold) {
                    continue;
                }
                int dist = levenshteinDistance(t1, t2, threshold);
                if (dist >= 0 && dist < threshold) {
                    copies.add(f2);
                }
            }
            if (!copies.isEmpty()) {
                result.put(f1, copies);
            }
        }
        return result;
    }

    private static int levenshteinDistance(String s1, String s2, int threshold) {
        int len1 = s1.length();
        int len2 = s2.length();

        if (Math.abs(len1 - len2) > threshold) {
            return -1;
        }

        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        for (int j = 0; j <= len2; j++) prev[j] = j;

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            int minInRow = curr[0];
            char c1 = s1.charAt(i - 1);
            for (int j = 1; j <= len2; j++) {
                int cost = (c1 == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
                if (curr[j] < minInRow) minInRow = curr[j];
            }
            if (minInRow > threshold) return -1;
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        int dist = prev[len2];
        return dist > threshold ? -1 : dist;
    }
}
