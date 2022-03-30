package stage2.practice.three.pathfinding;

import stage2.practice.three.pathfinding.util.ICellContainer;
import stage2.practice.three.pathfinding.util.LinkedListCellContainer;
import stage2.practice.three.pathfinding.util.PriorityQueueCellContainer;

import java.util.*;
import java.util.function.Function;

public class Pathfinders {

    private static void buildPathThroughPrevs(HashMap<Cell, Cell> prevMap, Cell startCell, Cell finishCell, Cell curCell){
        /*
        Обновить состояния клеток по найденому пути
         */

        while(curCell != startCell){
            curCell.setState(Cell.State.PATH);
            curCell = prevMap.get(curCell);
        }
        startCell.setState(Cell.State.START);
        finishCell.setState(Cell.State.END);
    }
    private static void addNeighboursToCellContainer(Grid grid, Cell curCell, HashSet<Cell> checkedCells, ICellContainer container, HashMap<Cell, Cell> prevMap){
        Arrays.stream(grid.getNeighbours(curCell))
                .forEach(c ->{
                    if(!checkedCells.contains(c) && !container.contains(c)){
                        if(c.getState() == Cell.State.BLOCKED) {
                            checkedCells.add(c);
                        }
                        else{
                            container.add(c);
                            prevMap.merge(c,curCell, (oldV, newV) -> newV);
                        }
                    }
                });
    }

    private static boolean findPathWithCellContainer(Grid grid, ICellContainer container, Cell startCell, Cell finishCell, Function<ICellContainer, Cell> func){
        container.add(startCell);

        var checkedCells = new HashSet<Cell>();
        var prevMap = new HashMap<Cell, Cell>();

        while(!container.isEmpty()){
            var curCell = func.apply(container);
            if(checkedCells.contains(curCell) )
                continue;

            if(curCell.equals(finishCell)){
                buildPathThroughPrevs(prevMap,startCell, finishCell, curCell);
                return true;
            }

            addNeighboursToCellContainer(grid, curCell, checkedCells, container, prevMap);
            checkedCells.add(curCell);
        }
        return false;
    }

    public static boolean BFS(Grid grid, Cell startCell, Cell finishCell){
        var container = new LinkedListCellContainer();
        return findPathWithCellContainer(grid, container, startCell, finishCell, cont->cont.remove());
    }

    public static boolean DFS(Grid grid, Cell startCell, Cell finishCell){
        var container = new LinkedListCellContainer();
        return findPathWithCellContainer(grid, container, startCell, finishCell, cont->cont.removeLast());
    }

    public static boolean AStar(Grid grid, Cell startCell, Cell finishCell){

        Function<Cell, Integer> getCellCost = cell -> {
            return grid.getDistance(cell, startCell) + grid.getDistance(cell, finishCell);
        };

        var container = new PriorityQueueCellContainer(new Comparator<Cell>() {
            @Override
            public int compare(Cell c1, Cell c2) {
                var cost1 = getCellCost.apply(c1);
                var cost2 = getCellCost.apply(c2);
                return cost1.compareTo(cost2);
            }
        });

        return findPathWithCellContainer(grid,container,startCell,finishCell, cont->cont.remove());
    }
}
