package by.it.group510971.rudko.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

//Как A, но дополнительно удаляются комментарии и пустые строки.
//Вывод: "размер путь" по возрастанию размера, при равенстве - по пути.
public class SourceScannerB {

    record Entry(int size, String path) {
    }

    public static void main(String[] args) throws IOException {
        Path root = SourceText.srcRoot();
        List<Entry> entries = new ArrayList<>();
        for (Path file : SourceText.javaFiles(root)) {
            String text = SourceText.read(file);
            if (SourceText.isTest(text)) continue;
            String processed = SourceText.removePackageAndImports(text);
            processed = SourceText.removeComments(processed);
            processed = SourceText.trimControl(SourceText.removeEmptyLines(processed));
            int size = processed.getBytes(StandardCharsets.UTF_8).length;
            entries.add(new Entry(size, root.relativize(file).toString()));
        }
        entries.sort((a, b) -> a.size() != b.size()
                ? Integer.compare(a.size(), b.size())
                : a.path().compareTo(b.path()));
        StringBuilder out = new StringBuilder();
        for (Entry e : entries) {
            out.append(e.size()).append(' ').append(e.path()).append('\n');
        }
        System.out.print(out);
    }
}
