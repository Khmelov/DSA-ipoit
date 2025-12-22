package by.it.group410972.kapur.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC {

  private static final int LIMIT = 10;

  private static class FileData {
    final String path;
    final String text;

    FileData(String path, String text) {
      this.path = path;
      this.text = text;
    }
  }

  public static void main(String[] args) {
    String src = System.getProperty("user.dir")
        + File.separator + "src" + File.separator;

    Path root = Path.of(src);
    List<FileData> files = new ArrayList<>();

    try (Stream<Path> walk = Files.walk(root)) {
      walk.filter(p -> p.toString().endsWith(".java"))
          .forEach(p -> readFile(root, p, files));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Map<String, List<String>> copies = new TreeMap<>();

    for (int i = 0; i < files.size(); i++) {
      FileData a = files.get(i);
      for (int j = i + 1; j < files.size(); j++) {
        FileData b = files.get(j);

        if (isCopy(a.text, b.text)) {
          copies.computeIfAbsent(a.path, k -> new ArrayList<>()).add(b.path);
        }
      }
    }

    for (FileData file : files) {
      System.out.println(file.path);

      List<String> dupes = copies.get(file.path);
      if (dupes != null) {
        dupes.stream()
            .sorted()
            .forEach(p -> System.out.println(p));
      }
    }

  }

  private static void readFile(Path root, Path file, List<FileData> files) {
    String text;
    try {
      text = Files.readString(file);
    } catch (MalformedInputException e) {
      return;
    } catch (IOException e) {
      return;
    }

    if (text.contains("@Test") || text.contains("org.junit.Test")) {
      return;
    }

    text = removePackageAndImports(text);
    text = removeComments(text);
    text = normalize(text);

    files.add(new FileData(root.relativize(file).toString(), text));
  }

  // 1. Удаление package и import
  private static String removePackageAndImports(String s) {
    StringBuilder out = new StringBuilder();
    Scanner sc = new Scanner(s);
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      String t = line.trim();
      if (t.startsWith("package ") || t.startsWith("import ")) continue;
      out.append(line).append('\n');
    }
    return out.toString();
  }

  // 2. Удаление комментариев O(n)
  private static String removeComments(String s) {
    StringBuilder out = new StringBuilder();
    boolean block = false, line = false;

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      char n = i + 1 < s.length() ? s.charAt(i + 1) : 0;

      if (block) {
        if (c == '*' && n == '/') {
          block = false;
          i++;
        }
        continue;
      }

      if (line) {
        if (c == '\n') {
          line = false;
          out.append(c);
        }
        continue;
      }

      if (c == '/' && n == '*') {
        block = true;
        i++;
        continue;
      }

      if (c == '/' && n == '/') {
        line = true;
        i++;
        continue;
      }

      out.append(c);
    }
    return out.toString();
  }

  // 3–4. Нормализация в строку + trim
  private static String normalize(String s) {
    StringBuilder out = new StringBuilder();
    boolean space = false;

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c < 33) {
        if (!space) {
          out.append(' ');
          space = true;
        }
      } else {
        out.append(c);
        space = false;
      }
    }
    return out.toString().trim();
  }

  // 5. Проверка копии (Левенштейн < 10)
  private static boolean isCopy(String a, String b) {
    int la = a.length();
    int lb = b.length();

    if (Math.abs(la - lb) >= LIMIT) return false;

    int[] prev = new int[lb + 1];
    int[] curr = new int[lb + 1];

    for (int j = 0; j <= lb; j++) prev[j] = j;

    for (int i = 1; i <= la; i++) {
      curr[0] = i;
      int min = curr[0];

      for (int j = 1; j <= lb; j++) {
        int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
        curr[j] = Math.min(
            Math.min(curr[j - 1] + 1, prev[j] + 1),
            prev[j - 1] + cost
        );
        min = Math.min(min, curr[j]);
      }

      if (min >= LIMIT) return false;

      int[] tmp = prev;
      prev = curr;
      curr = tmp;
    }

    return prev[lb] < LIMIT;
  }
}

