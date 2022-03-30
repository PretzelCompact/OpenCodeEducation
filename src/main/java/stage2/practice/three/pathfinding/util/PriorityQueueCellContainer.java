package stage2.practice.three.pathfinding.util;

import jdk.jshell.spi.ExecutionControl;
import stage2.practice.three.pathfinding.Cell;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueCellContainer implements ICellContainer{
    private PriorityQueue<Cell> priorityQueue;

    public PriorityQueueCellContainer(Comparator<Cell> comparator){
        priorityQueue = new PriorityQueue<>(comparator);
    }
    @Override
    public Cell remove() {
        return priorityQueue.remove();
    }

    @Override
    public Cell removeLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(Cell cell) {
        priorityQueue.add(cell);
    }

    @Override
    public boolean isEmpty() {
        return priorityQueue.isEmpty();
    }

    @Override
    public boolean contains(Cell cell) {
        return priorityQueue.contains(cell);
    }
}
