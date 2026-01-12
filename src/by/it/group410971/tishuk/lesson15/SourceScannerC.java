package by.it.group410971.tishuk.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileData> files = new ArrayList<>();

        // --- сбор java-файлов ---
        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".java")) {
                        try {
                            String content = readFileSafe(file);
                            if (!isTestFile(content)) {
                                String processed = preprocess(content);
                                if (!processed.isEmpty()) {
                                    String relPath = file.toAbsolutePath().toString().substring(src.length());
                                    files.add(new FileData(relPath, processed));
                                }
                            }
                        } catch (Exception ignore) { }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // --- сортировка по размеру и имени ---
        files.sort((f1, f2) -> {
            int cmp = Integer.compare(f1.text.length(), f2.text.length());
            if (cmp == 0) return f1.path.compareTo(f2.path);
            return cmp;
        });

        // --- вывод ---
        for (FileData file : files) {
            System.out.println(file.text.length() + " " + file.path);
        }
    }

    // ---------- чтение файла с fallback по кодировкам ----------
    private static String readFileSafe(Path file) throws IOException {
        Charset[] charsets = {
                StandardCharsets.UTF_8,
                StandardCharsets.ISO_8859_1,
                Charset.forName("Windows-1251"),
                Charset.forName("CP1252")
        };
        for (Charset cs : charsets) {
            try {
                return Files.readString(file, cs);
            } catch (MalformedInputException ignored) { }
        }
        // fallback байтов
        byte[] bytes = Files.readAllBytes(file);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // ---------- определение тестовых файлов ----------
    private static boolean isTestFile(String content) {
        String lower = content.toLowerCase();
        return lower.contains("@test") || lower.contains("org.junit.test");
    }

    // ---------- удаление package, import и невидимых символов ----------
    private static String preprocess(String text) {
        StringBuilder sb = new StringBuilder();
        boolean inCommentLine = false;
        boolean inCommentBlock = false;
        boolean inString = false;
        char prev = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char next = (i + 1 < text.length()) ? text.charAt(i + 1) : 0;

            if (!inCommentBlock && !inCommentLine && !inString) {
                if (c == '/' && next == '/') { inCommentLine = true; i++; continue; }
                if (c == '/' && next == '*') { inCommentBlock = true; i++; continue; }
                if (c == '"' || c == '\'') { inString = true; prev = c; sb.append(c); continue; }
            } else if (inString) {
                sb.append(c);
                if (c == prev && text.charAt(i - 1) != '\\') inString = false;
                continue;
            } else if (inCommentLine) {
                if (c == '\n') inCommentLine = false;
                continue;
            } else if (inCommentBlock) {
                if (c == '*' && next == '/') { inCommentBlock = false; i++; }
                continue;
            }

            sb.append(c);
        }

        StringBuilder result = new StringBuilder();
        String[] lines = sb.toString().split("\\R");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("package") || line.startsWith("import")) continue;

            // удаляем символы <33 только в начале и конце строки
            int start = 0, end = line.length() - 1;
            while (start <= end && line.charAt(start) < 33) start++;
            while (end >= start && line.charAt(end) < 33) end--;
            if (start <= end) result.append(line, start, end + 1).append(' ');
        }

        return result.toString();
    }

    // ---------- вспомогательная структура ----------
    private static class FileData {
        String path;
        String text;
        FileData(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }
}
