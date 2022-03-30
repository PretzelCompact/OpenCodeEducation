package stage2.practice.three.pathfinding.util;

import stage2.practice.three.pathfinding.Cell;

import java.util.LinkedList;


public class LinkedListCellContainer implements ICellContainer{
    private LinkedList<Cell> list = new LinkedList<>();
    @Override
    public Cell remove() {
        return list.remove();
    }

    @Override
    public Cell removeLast() {
        return list.removeLast();
    }

    @Override
    public void add(Cell cell) {
        list.add(cell);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Cell cell) {
        return list.contains(cell);
    }
}
