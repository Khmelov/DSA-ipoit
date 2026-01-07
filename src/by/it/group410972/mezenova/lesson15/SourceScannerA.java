package by.it.group410972.mezenova.lesson15;


import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    static class FileData {
        String path;
        int size;
        FileData(String path, int size) { this.path = path; this.size = size; }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<FileData> filesList = new ArrayList<>();

        scanDir(new File(src), src, filesList);

        filesList.sort((f1, f2) -> {
            if (f1.size != f2.size) return Integer.compare(f1.size, f2.size);
            return f1.path.compareTo(f2.path);
        });

        for (FileData fd : filesList) {
            System.out.println(fd.size + " " + fd.path);
        }
    }

    static void scanDir(File dir, String basePath, List<FileData> filesList) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                scanDir(f, basePath, filesList);
            } else if (f.isFile() && f.getName().endsWith(".java")) {
                try {
                    byte[] bytes = Files.readAllBytes(f.toPath());
                    String text;
                    text = new String(bytes, StandardCharsets.UTF_8);

                    // Игнорируем тестовые файлы
                    if (text.contains("@Test") || text.contains("org.junit.Test")) continue;

                    // Удаляем package и import строки
                    StringBuilder sb = new StringBuilder();
                    for (String line : text.split("\n")) {
                        String trimmed = line.trim();
                        if (trimmed.startsWith("package") || trimmed.startsWith("import")) continue;
                        sb.append(line).append("\n");
                    }
                    String cleaned = sb.toString();

                    // Обрезаем символы с кодом <33 в начале и конце
                    int start = 0, end = cleaned.length();
                    while (start < end && cleaned.charAt(start) < 33) start++;
                    while (end > start && cleaned.charAt(end - 1) < 33) end--;
                    String finalText = cleaned.substring(start, end);

                    int size = finalText.getBytes(StandardCharsets.UTF_8).length;
                    String relPath = f.getAbsolutePath().substring(basePath.length());
                    filesList.add(new FileData(relPath, size));

                } catch (IOException ignored) {
                }
            }
        }
    }
}