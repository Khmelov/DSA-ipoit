package by.it.group410972.ivanovich.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileData> files = new ArrayList<>();

        try {
            Files.walk(srcPath)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> handleFile(p, srcPath, files));

            // Сортировка
            files.sort((f1, f2) -> {
                if (f1.size != f2.size) {
                    return Integer.compare(f1.size, f2.size);
                }
                return f1.path.compareTo(f2.path);
            });

            // Вывод
            for (FileData file : files) {
                System.out.println(file.size + " " + file.path);
            }

        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static void handleFile(Path file, Path srcPath, List<FileData> files) {
        try {
            String content = readFileSafely(file);

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processed = cleanJavaCode(content);
            int size = processed.getBytes(StandardCharsets.UTF_8).length;
            String relativePath = srcPath.relativize(file).toString();

            files.add(new FileData(relativePath, size));

        } catch (IOException e) {
            // Пропускаем файлы с ошибками
        }
    }

    private static String readFileSafely(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // Пробуем другие кодировки
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, Charset.forName("Windows-1251"));
        }
    }

    private static String cleanJavaCode(String code) {
        // Удаляем package и import
        code = removePackageAndImports(code);

        // Удаляем комментарии
        code = removeComments(code);

        // Удаляем управляющие символы с краев
        code = code.trim();
        while (code.length() > 0 && code.charAt(0) < 33) {
            code = code.substring(1);
        }
        while (code.length() > 0 && code.charAt(code.length() - 1) < 33) {
            code = code.substring(0, code.length() - 1);
        }

        // Удаляем пустые строки
        code = removeBlankLines(code);

        return code;
    }

    private static String removePackageAndImports(String code) {
        StringBuilder result = new StringBuilder();
        String[] lines = code.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private static String removeComments(String code) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        char prevChar = 0;

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);

            if (inBlockComment) {
                if (c == '*' && i + 1 < code.length() && code.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    result.append(c);
                }
                continue;
            }

            if (!inString && c == '/' && i + 1 < code.length()) {
                char next = code.charAt(i + 1);
                if (next == '*') {
                    inBlockComment = true;
                    i++;
                    continue;
                } else if (next == '/') {
                    inLineComment = true;
                    i++;
                    continue;
                }
            }

            if (c == '"' && prevChar != '\\') {
                inString = !inString;
            }

            result.append(c);
            prevChar = c;
        }

        return result.toString();
    }

    private static String removeBlankLines(String text) {
        StringBuilder result = new StringBuilder();
        String[] lines = text.split("\n");

        for (String line : lines) {
            // Проверяем, не пустая ли строка
            boolean isEmpty = true;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) > 32) {
                    isEmpty = false;
                    break;
                }
            }

            if (!isEmpty) {
                result.append(line).append("\n");
            }
        }

        // Удаляем последний перевод строки
        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    static class FileData {
        String path;
        int size;

        FileData(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }
}