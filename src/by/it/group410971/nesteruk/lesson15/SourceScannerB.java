package by.it.group410971.nesteruk.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerB {

    static class FileInfo {
        String path;
        long size;

        FileInfo(String path, long size) {
            this.path = path;
            this.size = size;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java")) {
                        processFile(file, src, fileInfos);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileInfos.sort((a, b) -> {
            int sizeCompare = Long.compare(a.size, b.size);
            return sizeCompare != 0 ? sizeCompare : a.path.compareTo(b.path);
        });

        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.path);
        }
    }

    private static void processFile(Path file, String src, List<FileInfo> fileInfos) {
        try {
            // Пробуем разные кодировки для обработки MalformedInputException
            String content = readFileWithFallback(file);

            if (content == null || content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processContent(content);
            byte[] bytes = processedContent.getBytes(StandardCharsets.UTF_8);

            String relativePath = file.toString().substring(src.length());
            fileInfos.add(new FileInfo(relativePath, bytes.length));

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
                    result.append(c);
                }
                continue;
            }

            if (inString) {
                result.append(c);
                if (c == stringChar && content.charAt(i - 1) != '\\') {
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

        // Удаляем package и импорты, сохраняя остальное
        String[] lines = withoutComments.split("\n");
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") &&
                    !trimmedLine.startsWith("import") &&
                    !trimmedLine.isEmpty()) {
                result.append(trimmedLine).append("\n");
            }
        }

        String processed = result.toString();

        // Удаляем символы с кодом <33 в начале и конце
        int start = 0;
        while (start < processed.length() && processed.charAt(start) < 33) {
            start++;
        }

        int end = processed.length() - 1;
        while (end >= start && processed.charAt(end) < 33) {
            end--;
        }

        if (start > end) {
            return "";
        }

        return processed.substring(start, end + 1);
    }
}