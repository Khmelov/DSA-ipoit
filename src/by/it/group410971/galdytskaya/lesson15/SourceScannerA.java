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

public class SourceScannerA {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Paths.get(src);

        List<FileData> processedFiles = new ArrayList<>();

        try {
            Files.walk(root)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            byte[] rawBytes = Files.readAllBytes(p);

                            // декодер для игнорирования ошибок
                            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                                    .onMalformedInput(CodingErrorAction.IGNORE)
                                    .onUnmappableCharacter(CodingErrorAction.IGNORE);

                            CharBuffer decoded = decoder.decode(ByteBuffer.wrap(rawBytes));
                            String content = decoded.toString();

                            // исключение файлов с тестами
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // удаление package и import строки
                            String processed = removePackageAndImports(content);

                            // удаление символов с кодом < 33 в начале и конце
                            processed = trimControlChars(processed);

                            int size = processed.getBytes(StandardCharsets.UTF_8).length;

                            Path relative = root.relativize(p);
                            processedFiles.add(new FileData(relative.toString(), size));

                        } catch (MalformedInputException e) {
                        } catch (IOException e) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // сортировка по размеру и лексикографически по пути
        processedFiles.sort(Comparator.comparingInt(FileData::getSize)
                .thenComparing(FileData::getPath));

        processedFiles.forEach(fd -> System.out.println(fd.size + " " + fd.path));
    }

    private static String removePackageAndImports(String content) {
        StringBuilder sb = new StringBuilder();
        String[] lines = content.split("\r?\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
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
