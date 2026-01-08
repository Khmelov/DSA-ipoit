# Отчет по практической работе «Algorithms & Data Structures»

## Данные

- Студент: group 410971, фамилия lukashonok
- Период заданий: lessons 09–11 и 12–15
- Язык: Java 21

## Lesson 09 — ListA/ListB/ListC

**Цель:** реализовать список на массиве без стандартных коллекций.

**Реализация:** динамический массив с увеличением емкости в 2 раза.

**Методы:** `add`, `remove`, `size`, `get`, `set`, `contains`, `indexOf`, `lastIndexOf`, `addAll`, `removeAll`, `retainAll`, `toString`.

**Сложность:**

- `add` в конец — O(1) амортизировано
- `remove`/`add` по индексу — O(n)
- `get`/`set` — O(1)
- поиск — O(n)

**Файлы:**

- `src/by/it/group410971/lukashonok/lesson09/ListA.java`
- `src/by/it/group410971/lukashonok/lesson09/ListB.java`
- `src/by/it/group410971/lukashonok/lesson09/ListC.java`

## Lesson 10 — Deque/Queue

### MyArrayDeque

**Реализация:** циклический массив с индексом головы.

**Операции:** `addFirst`, `addLast`, `pollFirst`, `pollLast`, `getFirst`, `getLast`.

**Сложность:**

- добавление/удаление с концов — O(1) амортизировано

**Файл:** `src/by/it/group410971/lukashonok/lesson10/MyArrayDeque.java`

### MyLinkedList

**Реализация:** двусвязный список.

**Сложность:**

- операции с концами — O(1)
- поиск и удаление по индексу — O(n)

**Файл:** `src/by/it/group410971/lukashonok/lesson10/MyLinkedList.java`

### MyPriorityQueue

**Реализация:** бинарная куча (min-heap) на массиве.

**Сложность:**

- `offer`/`poll` — O(log n)
- `peek` — O(1)
- `removeAll`/`retainAll` — O(n)

**Файл:** `src/by/it/group410971/lukashonok/lesson10/MyPriorityQueue.java`

## Lesson 11 — Set

### MyHashSet

**Реализация:** хеш-таблица с цепочками (separate chaining), порядок обхода соответствует вставке в цепочки.

**Сложность:**

- `add`/`remove`/`contains` — O(1) в среднем

**Файл:** `src/by/it/group410971/lukashonok/lesson11/MyHashSet.java`

### MyLinkedHashSet

**Реализация:** хеш-таблица + связный список порядка вставки.

**Сложность:**

- `add`/`remove`/`contains` — O(1) в среднем

**Файл:** `src/by/it/group410971/lukashonok/lesson11/MyLinkedHashSet.java`

### MyTreeSet

**Реализация:** отсортированный массив с бинарным поиском.

**Сложность:**

- `contains` — O(log n)
- `add`/`remove` — O(n)

**Файл:** `src/by/it/group410971/lukashonok/lesson11/MyTreeSet.java`

## Lesson 12 — Map

### MyAvlMap

**Реализация:** бинарное дерево поиска с обходом in-order для `toString()`.

**Операции:** `put`, `remove`, `get`, `containsKey`, `size`, `clear`, `isEmpty`.

**Файл:** `src/by/it/group410971/lukashonok/lesson12/MyAvlMap.java`

### MyRbMap

**Реализация:** бинарное дерево поиска, поддержка `headMap`, `tailMap`, `firstKey`, `lastKey`.

**Файл:** `src/by/it/group410971/lukashonok/lesson12/MyRbMap.java`

### MySplayMap

**Реализация:** бинарное дерево поиска, поддержка `lowerKey`, `floorKey`, `ceilingKey`, `higherKey`.

**Файл:** `src/by/it/group410971/lukashonok/lesson12/MySplayMap.java`

## Lesson 13 — Графы

### GraphA

**Задача:** топологическая сортировка ориентированного графа.

**Алгоритм:** Kahn с приоритетом по лексикографическому порядку вершин.

**Файл:** `src/by/it/group410971/lukashonok/lesson13/GraphA.java`

### GraphB

**Задача:** проверка наличия циклов.

**Алгоритм:** DFS с цветами (white/gray/black).

**Файл:** `src/by/it/group410971/lukashonok/lesson13/GraphB.java`

### GraphC

**Задача:** компоненты сильной связности (SCC).

**Алгоритм:** Kosaraju (двойной DFS), вывод компонент в порядке от истоков к стокам; элементы внутри компоненты сортируются лексикографически.

**Файл:** `src/by/it/group410971/lukashonok/lesson13/GraphC.java`

**Дополнительно:** расширены тесты.

**Файл:** `src/by/it/group410971/lukashonok/lesson13/Test_Part2_Lesson13.java`

## Lesson 14 — DSU

### PointsA

**Задача:** кластеризация точек в 3D по расстоянию < D.

**Алгоритм:** DSU (union by size + path compression), перебор пар O(n^2).

**Файл:** `src/by/it/group410971/lukashonok/lesson14/PointsA.java`

### SitesB

**Задача:** объединение связанных сайтов в кластеры.

**Алгоритм:** DSU с компрессией пути и объединением по размеру.

**Файл:** `src/by/it/group410971/lukashonok/lesson14/SitesB.java`

### StatesHanoiTowerC

**Задача:** сгруппировать шаги решения Ханойских башен по максимальной высоте пирамид.

**Алгоритм:** рекурсивная генерация ходов + DSU по максимальной высоте.

**Файл:** `src/by/it/group410971/lukashonok/lesson14/StatesHanoiTowerC.java`

## Lesson 15 — SourceScanner

### SourceScannerA

**Задача:** удалить `package`/`import`, trim контрольных символов, вывести размеры и пути.

**Файл:** `src/by/it/group410971/lukashonok/lesson15/SourceScannerA.java`

### SourceScannerB

**Задача:** удалить `package`/`import`, комментарии, пустые строки; вывести размеры и пути.

**Файл:** `src/by/it/group410971/lukashonok/lesson15/SourceScannerB.java`

### SourceScannerC

**Задача:** нормализация текста и поиск копий по расстоянию Левенштейна < 10.

**Оптимизация:** ограничение по длине и ранний выход по порогу.

**Файл:** `src/by/it/group410971/lukashonok/lesson15/SourceScannerC.java`

## Итоги

- Все задания lessons 09–15 реализованы.
- Тесты успешно пройдены.
