package by.it.group410972.zayats.lesson15;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileInfo> fileInfos = new ArrayList<>();

        try (var walk = Files.walk(srcPath)) {
            walk.filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = readFileWithFallback(p);
                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                String processed = processContentB(content);
                                int size = processed.getBytes(StandardCharsets.UTF_8).length;
                                String relativePath = srcPath.relativize(p).toString();
                                fileInfos.add(new FileInfo(relativePath, size));
                            }
                        } catch (IOException e) {
                            // Игнорируем файлы с ошибками чтения
                        }
                    });
        }

        // Сортировка: по размеру (по убыванию), при равных - по пути (лексикографически)
        fileInfos.sort((a, b) -> {
            if (a.size != b.size) {
                return Integer.compare(b.size, a.size); // по убыванию
            }
            return a.path.compareTo(b.path);
        });

        // Вывод результатов
        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.path);
        }
    }

    private static String readFileWithFallback(Path path) throws IOException {
        // Пытаемся прочитать с разными кодировками
        List<Charset> charsets = Arrays.asList(
                StandardCharsets.UTF_8,
                Charset.forName("Windows-1251"),
                StandardCharsets.ISO_8859_1
        );

        for (Charset charset : charsets) {
            try {
                return Files.readString(path, charset);
            } catch (IOException e) {
                // Пробуем следующую кодировку
            }
        }

        // Если все кодировки не подошли, возвращаем пустую строку
        return "";
    }

    private static String processContentB(String content) {
        // Удаляем комментарии
        content = removeComments(content);

        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            // Пропускаем package и import
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                // Удаляем пустые строки
                if (!trimmed.isEmpty()) {
                    result.append(line).append("\n");
                }
            }
        }

        // Удаляем символы с кодом <33 в начале и конце
        String processed = result.toString();
        processed = processed.replaceAll("^[\\x00-\\x20]+", "");
        processed = processed.replaceAll("[\\x00-\\x20]+$", "");

        return processed;
    }

    static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (inBlockComment) {
                if (c == '*' && i + 1 < content.length() && content.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++; // Пропускаем '/'
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

            if (inString) {
                result.append(c);
                if (c == '\\' && i + 1 < content.length()) {
                    result.append(content.charAt(i + 1));
                    i++;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                result.append(c);
                if (c == '\\' && i + 1 < content.length()) {
                    result.append(content.charAt(i + 1));
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }

            // Проверка на начало комментария или строки/символа
            if (c == '/' && i + 1 < content.length()) {
                char next = content.charAt(i + 1);
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

            if (c == '"') {
                inString = true;
                result.append(c);
                continue;
            }

            if (c == '\'') {
                inChar = true;
                result.append(c);
                continue;
            }

            result.append(c);
        }

        return result.toString();
    }

    static class FileInfo {
        String path;
        int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }
}