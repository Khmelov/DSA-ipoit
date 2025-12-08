package by.it.group410971.galdytskaya.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        List<FileData> filesData = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            byte[] bytes = Files.readAllBytes(p);

                            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                                    .onMalformedInput(CodingErrorAction.IGNORE)
                                    .onUnmappableCharacter(CodingErrorAction.IGNORE);

                            CharBuffer decoded = decoder.decode(ByteBuffer.wrap(bytes));
                            String content = decoded.toString();

                            // Исключаем тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Удаляем package и import строки за один проход
                            String withoutPkgImports = removePackageAndImports(content);

                            // Удаляем все комментарии (// и /* */) за один проход
                            String withoutComments = removeComments(withoutPkgImports);

                            // Обрезаем символы <33 в начале и конце
                            String trimmed = trimControlChars(withoutComments);

                            // Удаляем пустые строки
                            String noEmptyLines = removeEmptyLines(trimmed);

                            // Считаем размер в байтах
                            int size = noEmptyLines.getBytes(StandardCharsets.UTF_8).length;

                            Path relative = root.relativize(p);
                            filesData.add(new FileData(relative.toString(), size));

                        } catch (MalformedInputException e) {
                            // игнорируем ошибку и продолжаем
                        } catch (IOException e) {
                            // игнорируем ошибки чтения
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортируем по размеру, затем по пути
        filesData.sort(Comparator.comparingInt(FileData::getSize)
                .thenComparing(FileData::getPath));

        filesData.forEach(fd -> System.out.println(fd.size + " " + fd.path));
    }

    private static String removePackageAndImports(String content) {
        StringBuilder sb = new StringBuilder();
        for (String line : content.split("\r?\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private static String removeComments(String code) {
        StringBuilder result = new StringBuilder();
        int length = code.length();
        boolean inBlockComment = false;
        boolean inLineComment = false;

        for (int i = 0; i < length; i++) {
            if (inBlockComment) {
                if (i + 1 < length && code.charAt(i) == '*' && code.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++; // пропускаем '/'
                }
                // Внутри блока игнорируем символы
            } else if (inLineComment) {
                if (code.charAt(i) == '\n' || code.charAt(i) == '\r') {
                    inLineComment = false;
                    result.append(code.charAt(i));
                }
                // Внутри строки комментария – игнорируем символы
            } else {
                if (i + 1 < length && code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++; // пропускаем '*'
                } else if (i + 1 < length && code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
                    inLineComment = true;
                    i++; // пропускаем второй '/'
                } else {
                    result.append(code.charAt(i));
                }
            }
        }
        return result.toString();
    }

    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length() - 1;

        while (start <= end && text.charAt(start) < 33) {
            start++;
        }
        while (end >= start && text.charAt(end) < 33) {
            end--;
        }
        return (start > end) ? "" : text.substring(start, end + 1);
    }

    private static String removeEmptyLines(String text) {
        StringBuilder sb = new StringBuilder();
        for (String line : text.split("\r?\n")) {
            if (!line.trim().isEmpty()) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private static class FileData {
        final String path;
        final int size;

        FileData(String path, int size) {
            this.path = path;
            this.size = size;
        }

        public String getPath() {
            return path;
        }

        public int getSize() {
            return size;
        }
    }
}

