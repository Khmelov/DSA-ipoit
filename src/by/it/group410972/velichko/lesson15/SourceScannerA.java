package by.it.group410972.kulesh.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

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
                                String processed = processContentA(content);
                                int size = processed.getBytes(StandardCharsets.UTF_8).length;
                                String relativePath = srcPath.relativize(p).toString();
                                fileInfos.add(new FileInfo(relativePath, size));
                            }
                        } catch (IOException e) {
                        }
                    });
        }

        fileInfos.sort((a, b) -> {
            if (a.size != b.size) {
                return Integer.compare(b.size, a.size);
            }
            return a.path.compareTo(b.path);
        });

        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.path);
        }
    }

    private static String readFileWithFallback(Path path) throws IOException {
        List<Charset> charsets = Arrays.asList(
                StandardCharsets.UTF_8,
                Charset.forName("Windows-1251"),
                StandardCharsets.ISO_8859_1
        );

        for (Charset charset : charsets) {
            try {
                return Files.readString(path, charset);
            } catch (IOException e) {
            }
        }

        return "";
    }

    private static String processContentA(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                result.append(line).append("\n");
            }
        }

        String processed = result.toString();
        processed = processed.replaceAll("^[\\x00-\\x20]+", "");
        processed = processed.replaceAll("[\\x00-\\x20]+$", "");

        return processed;
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
