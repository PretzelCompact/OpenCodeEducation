package stage2.practice.three.pathfinding;

import java.time.LocalDateTime;
import java.util.Random;

public class ObstacleCreators {
    /*
    Класс, содержащий статичные методы, которые
    создают препятствия внутри сетки
     */

    private static void setObstacle(Grid grid, int x, int y){
        grid.setCellState(x, y, Cell.State.BLOCKED);
    }

    public static void lineScan(Grid grid, int maxLineLength){
        /*
        Пройти по горизонатальным рядам сетки через один
        Установить пробелы и стены случайной длины (не больше maxLineLength)
         */

        var rnd = new Random(LocalDateTime.now().hashCode());

        for(int i = 0; i < grid.getWidth(); i+=2){
            int obstacleBlocsLeft = 0;
            int emptyBlocks = 0;
            for(int j = 0; j < grid.getHeight(); j++){
                var state = grid.getCell(i,j).getState();
                if(state == Cell.State.START || state == Cell.State.END)
                    continue;

                if(obstacleBlocsLeft == 0 && emptyBlocks == 0){
                    emptyBlocks = rnd.nextInt(1,maxLineLength/2+1);
                    obstacleBlocsLeft = rnd.nextInt(1,maxLineLength+1);
                } else if(obstacleBlocsLeft > 0){
                    obstacleBlocsLeft--;
                    setObstacle(grid,i,j);
                } else{
                    emptyBlocks--;
                }
            }
        }
    }
}
