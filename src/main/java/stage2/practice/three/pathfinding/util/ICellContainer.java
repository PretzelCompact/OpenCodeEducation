package stage2.practice.three.pathfinding.util;

import stage2.practice.three.pathfinding.Cell;

public interface ICellContainer {
    Cell remove();
    Cell removeLast();
    void add(Cell cell);
    boolean isEmpty();
    boolean contains(Cell cell);
}
