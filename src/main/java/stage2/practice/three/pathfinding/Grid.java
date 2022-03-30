package stage2.practice.three.pathfinding;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Grid {
    private Cell[][] cells;
    private int width;
    private int height;

    public Grid(int width, int height){
        if(width <= 0 || height <= 0)
            throw new RuntimeException("Невозможно создать сетку размера: (" + width + "," + height
                    + "). Значения должны быть больше нуля");
        this.width = width;
        this.height = height;

        cells = new Cell[width][height];
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                cells[i][j] = new Cell(i,j);
    }

    public Cell getStartCell(){
        for(var row : cells)
            for(var c : row)
                if(c.getState() == Cell.State.START)
                    return c;
        return null;
    }
    public Cell getFinishCell() {
        for(var row : cells)
            for(var c : row)
                if(c.getState() == Cell.State.END)
                    return c;
        return null;
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public int getDistance(Cell cell1, Cell cell2){
        /*
        Расстяние между двумя клетками.
        Т.к. по диагонали ходить нельзя, то можно вычислить длину "уголка"
         */

        int dx = Math.abs(cell1.getX()-cell2.getX());
        int dy = Math.abs(cell1.getY()-cell2.getY());

        return dx+dy;
    }

    public Cell[] getNeighbours(Cell cell){
        var neighbours = new ArrayList<Cell>();
        int x = cell.getX();
        int y = cell.getY();

        if(x+1 < width)
            neighbours.add(getCell(x+1,y));
        if(x-1 >= 0)
            neighbours.add(getCell(x-1,y));
        if(y+1 < getHeight())
            neighbours.add(getCell(x,y+1));
        if(y-1 >= 0)
            neighbours.add(getCell(x,y-1));

        return neighbours.toArray(new Cell[0]);
    }

    public void restore() {
        /*
        Очистить сетку от маркеров поиска пути
        (Кроме START и END)
         */

        Arrays.stream(cells)
                .forEach(row -> Arrays.stream(row)
                        .forEach(c -> {
                            if (c.getState() == Cell.State.PATH)
                                c.setState(Cell.State.EMPTY);
                        }));
    }
    public void clear(){
        /*
        Полностью опустошить сетку
         */

        Arrays.stream(cells)
                .forEach(row -> Arrays.stream(row)
                        .forEach(c->c.setState(Cell.State.EMPTY)));
    }
    public void initialize(){
        /*
        Делает из сетку полностью пустую за исключением
        двух клеток (START и END), которые устанавливаются рандомно
         */

        clear();

        var rnd = new Random(LocalDateTime.now().hashCode());
        var startCell = getCell(rnd.nextInt(width),rnd.nextInt(height));
        var finishCell = startCell;

        while(startCell == finishCell){
            finishCell = getCell(rnd.nextInt(width),rnd.nextInt(height));
        }

        startCell.setState(Cell.State.START);
        finishCell.setState(Cell.State.END);
    }

    public Cell getCell(int x, int y){
        return cells[x][y];
    }
    public void setCellState(int x, int y, Cell.State state){
        getCell(x,y).setState(state);
    }

    @Override
    public String toString(){
        var sb = new StringBuilder();

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height;  j++){
                sb.append(cells[i][j].getState().getStr());
            }
            sb.append('\n');
        }

        return sb.toString().substring(0,sb.length()-1);
    }
}
