package by.it.group410971.nesteruk.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerC {

    static class FileData {
        String path;
        String content;
        int length;

        FileData(String path, String content) {
            this.path = path;
            this.content = content;
            this.length = content.length();
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileData> files = new ArrayList<>();

        // Сбор всех файлов
        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java")) {
                        processFile(file, src, files);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортируем файлы по длине для оптимизации сравнений
        files.sort((a, b) -> Integer.compare(a.length, b.length));

        // Поиск копий с оптимизациями
        Map<String, List<String>> copies = new TreeMap<>();
        int fileCount = files.size();

        for (int i = 0; i < fileCount; i++) {
            FileData file1 = files.get(i);
            for (int j = i + 1; j < fileCount; j++) {
                FileData file2 = files.get(j);

                // Быстрая проверка по разнице длин
                int lengthDiff = Math.abs(file1.length - file2.length);
                if (lengthDiff >= 10) {
                    continue;
                }

                // Оптимизированное расстояние Левенштейна
                int distance = optimizedLevenshtein(file1.content, file2.content, 10);
                if (distance < 10) {
                    copies.computeIfAbsent(file1.path, k -> new ArrayList<>()).add(file2.path);
                    copies.computeIfAbsent(file2.path, k -> new ArrayList<>()).add(file1.path);
                }
            }
        }

        // Вывод результатов
        for (Map.Entry<String, List<String>> entry : copies.entrySet()) {
            List<String> copyList = entry.getValue();
            if (!copyList.isEmpty()) {
                System.out.println(entry.getKey());
                Collections.sort(copyList);
                for (String copy : copyList) {
                    System.out.println(copy);
                }
            }
        }
    }

    private static void processFile(Path file, String src, List<FileData> files) {
        try {
            String content = readFileWithFallback(file);
            if (content == null || content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processContent(content);
            String relativePath = file.toString().substring(src.length());
            files.add(new FileData(relativePath, processedContent));

        } catch (IOException e) {
            // Игнорируем файлы с проблемами чтения
        }
    }

    private static String readFileWithFallback(Path file) throws IOException {
        Charset[] charsets = {
                StandardCharsets.UTF_8,
                StandardCharsets.ISO_8859_1,
                Charset.forName("Windows-1251"),
                StandardCharsets.US_ASCII
        };

        for (Charset charset : charsets) {
            try {
                return new String(Files.readAllBytes(file), charset);
            } catch (MalformedInputException e) {
                // Пробуем следующую кодировку
            }
        }
        return null;
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        char stringChar = '"';

        // Удаление комментариев
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inBlockComment) {
                if (c == '*' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    result.append(' ');
                }
                continue;
            }

            if (inString) {
                result.append(c);
                if (c == stringChar && (i == 0 || content.charAt(i - 1) != '\\')) {
                    inString = false;
                }
                continue;
            }

            if (c == '"' || c == '\'') {
                inString = true;
                stringChar = c;
                result.append(c);
            } else if (c == '/' && i + 1 < content.length()) {
                char next = content.charAt(i + 1);
                if (next == '*') {
                    inBlockComment = true;
                    i++;
                } else if (next == '/') {
                    inLineComment = true;
                    i++;
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }

        String withoutComments = result.toString();
        result.setLength(0);

        // Удаление package и импортов
        String[] lines = withoutComments.split("\n");
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") && !trimmedLine.startsWith("import")) {
                result.append(trimmedLine).append(' ');
            }
        }

        // Замена последовательностей символов с кодом <33 на пробел
        String processed = result.toString();
        result.setLength(0);

        boolean lastWasSpace = false;
        for (int i = 0; i < processed.length(); i++) {
            char c = processed.charAt(i);
            if (c < 33) {
                if (!lastWasSpace) {
                    result.append(' ');
                    lastWasSpace = true;
                }
            } else {
                result.append(c);
                lastWasSpace = false;
            }
        }

        return result.toString().trim();
    }

    // Оптимизированная версия с ограничением максимального расстояния
    private static int optimizedLevenshtein(String s1, String s2, int maxDistance) {
        if (s1.isEmpty()) return s2.length();
        if (s2.isEmpty()) return s1.length();

        int len1 = s1.length();
        int len2 = s2.length();

        // Swap чтобы s1 была короче
        if (len1 > len2) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
            int tempLen = len1;
            len1 = len2;
            len2 = tempLen;
        }

        // Используем два массива для экономии памяти
        int[] current = new int[len1 + 1];
        int[] previous = new int[len1 + 1];

        for (int i = 0; i <= len1; i++) {
            previous[i] = i;
        }

        for (int j = 1; j <= len2; j++) {
            current[0] = j;
            int minInRow = j;

            for (int i = 1; i <= len1; i++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                current[i] = Math.min(Math.min(
                                current[i - 1] + 1,
                                previous[i] + 1),
                        previous[i - 1] + cost);
                minInRow = Math.min(minInRow, current[i]);
            }

            // Ранний выход если минимальное расстояние в строке превышает maxDistance
            if (minInRow > maxDistance) {
                return maxDistance + 1;
            }

            // Swap arrays
            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[len1];
    }
}