package by.it.group410972.popova.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.util.*;

public class SourceScannerB {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<FileResult> results = new ArrayList<>();
        scanDirectory(new File(src), src, results);

        results.sort((a, b) -> {
            if (a.size != b.size) return Integer.compare(a.size, b.size);
            return a.path.compareTo(b.path);
        });

        for (FileResult r : results) {
            System.out.println(r.size + " " + r.path);
        }
    }

    static class FileResult {
        int size;
        String path;
        FileResult(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }

    static void scanDirectory(File dir, String src, List<FileResult> results) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                scanDirectory(f, src, results);
            } else if (f.getName().endsWith(".java")) {
                processFile(f, src, results);
            }
        }
    }

    static void processFile(File file, String src, List<FileResult> results) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (MalformedInputException mie) {
            return;
        } catch (IOException e) {
            return;
        }

        String joined = String.join("\n", lines);
        if (joined.contains("@Test") || joined.contains("org.junit.Test")) return;

        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package") || trimmed.startsWith("import")) {
                continue;
            }
            sb.append(line).append("\n");
        }

        String text = sb.toString();
        text = text.replaceAll("//.*", "");
        text = text.replaceAll("/\\*.*?\\*/", "");
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) start++;
        int end = text.length() - 1;
        while (end >= 0 && text.charAt(end) < 33) end--;
        if (end >= start) {
            text = text.substring(start, end + 1);
        } else {
            text = "";
        }

        StringBuilder cleaned = new StringBuilder();
        for (String line : text.split("\n")) {
            if (!line.trim().isEmpty()) {
                cleaned.append(line).append("\n");
            }
        }
        text = cleaned.toString();

        int size = text.getBytes(Charset.defaultCharset()).length;
        String relativePath = file.getAbsolutePath();
        if (relativePath.startsWith(src)) {
            relativePath = relativePath.substring(src.length());
        }
        results.add(new FileResult(size, relativePath));
    }
}
