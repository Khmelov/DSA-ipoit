package by.it.group410972.Pushkarev.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {
    static class FileData {
        long size;
        String relativePath;

        FileData(long size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        File srcDir = new File(src);

        List<FileData> fileList = new ArrayList<>();
        scanDir(srcDir, srcDir, fileList);

        fileList.sort(Comparator.comparingLong((FileData f) -> f.size)
                .thenComparing(f -> f.relativePath));

        for (FileData f : fileList) {
            System.out.println(f.size + " " + f.relativePath);
        }
    }

    private static void scanDir(File dir, File base, List<FileData> fileList) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                scanDir(f, base, fileList);
            } else if (f.isFile() && f.getName().endsWith(".java")) {
                try {
                    String content = readFileSafe(f.toPath());
                    if (content.contains("@Test") || content.contains("org.junit.Test")) continue;

                    String[] lines = content.split("\n");
                    StringBuilder sb = new StringBuilder();
                    for (String line : lines) {
                        String trimmed = line.trim();
                        if (!trimmed.startsWith("package") && !trimmed.startsWith("import")) {
                            sb.append(line).append("\n");
                        }
                    }

                    String text = sb.toString();
                    int start = 0, end = text.length() - 1;
                    while (start <= end && text.charAt(start) < 33) start++;
                    while (end >= start && text.charAt(end) < 33) end--;
                    if (start <= end) {
                        text = text.substring(start, end + 1);
                    } else {
                        text = "";
                    }

                    long size = text.getBytes(Charset.defaultCharset()).length;
                    String relativePath = base.toPath().relativize(f.toPath()).toString();
                    fileList.add(new FileData(size, relativePath));

                } catch (MalformedInputException mie) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String readFileSafe(Path path) throws IOException {
        try {
            return Files.readString(path);
        } catch (MalformedInputException e) {
            return "";
        }
    }
}
