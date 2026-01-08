package by.it.group410971.lukashonok.lesson15;

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

public class SourceScannerA {

    private static class FileInfo {
        final String path;
        final int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }

    public static void main(String[] args) throws Exception {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<FileInfo> files = new ArrayList<>();
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
                    stripped = trimControl(stripped);
                    int size = stripped.getBytes(StandardCharsets.UTF_8).length;
                    String rel = root.relativize(path).toString();
                    files.add(new FileInfo(rel, size));
                }
            });
        }

        files.sort(Comparator
                .comparingInt((FileInfo f) -> f.size)
                .thenComparing(f -> f.path));

        StringBuilder out = new StringBuilder();
        for (FileInfo file : files) {
            out.append(file.size).append(' ').append(file.path).append('\n');
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

    private static String trimControl(String text) {
        int start = 0;
        int end = text.length() - 1;
        while (start <= end && text.charAt(start) < 33) {
            start++;
        }
        while (end >= start && text.charAt(end) < 33) {
            end--;
        }
        return start > end ? "" : text.substring(start, end + 1);
    }
}
