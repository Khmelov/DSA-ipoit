package by.it.group510971.rudko.lesson15;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

//Поиск копий: тексты нормализуются, затем пары с расстоянием Левенштейна меньше 10 считаются копиями.
//Вывод: для каждого файла с копиями - его путь, в следующих строках пути копий. Файлы по пути.
public class SourceScannerC {

    private static final int MAX_EDITS = 10;   //копия - если правок меньше 10

    public static void main(String[] args) throws IOException {
        Path root = SourceText.srcRoot();
        TreeMap<String, String> texts = new TreeMap<>();   //путь -> нормализованный текст
        for (Path file : SourceText.javaFiles(root)) {
            String text = SourceText.read(file);
            if (SourceText.isTest(text)) continue;
            String processed = SourceText.removePackageAndImports(text);
            processed = SourceText.removeComments(processed);
            processed = SourceText.trimControl(SourceText.collapseControl(processed));
            texts.put(root.relativize(file).toString(), processed);
        }
        List<String> paths = new ArrayList<>(texts.keySet());
        List<String> bodies = new ArrayList<>(texts.values());
        int n = paths.size();
        List<List<Integer>> copies = new ArrayList<>();
        for (int i = 0; i < n; i++) copies.add(new ArrayList<>());
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(bodies.get(i).length() - bodies.get(j).length()) >= MAX_EDITS) continue;
                if (Levenshtein.withinLimit(bodies.get(i), bodies.get(j), MAX_EDITS - 1)) {
                    copies.get(i).add(j);
                    copies.get(j).add(i);
                }
            }
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (copies.get(i).isEmpty()) continue;
            out.append(paths.get(i)).append('\n');
            for (int j : copies.get(i)) {
                out.append(paths.get(j)).append('\n');
            }
        }
        System.out.print(out);
    }
}
