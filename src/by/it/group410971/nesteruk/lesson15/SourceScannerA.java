package by.it.group410971.nesteruk.lesson15;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerA {

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
            String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            String processedContent = processContent(content);
            byte[] bytes = processedContent.getBytes(StandardCharsets.UTF_8);

            String relativePath = file.toString().substring(src.length());
            fileInfos.add(new FileInfo(relativePath, bytes.length));

        } catch (MalformedInputException e) {
            // Игнорируем файлы с проблемами кодировки
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package") &&
                    !trimmedLine.startsWith("import") &&
                    !trimmedLine.startsWith("//")) {
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

        return processed.substring(start, end + 1);
    }
}