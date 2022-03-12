package stage2.practice.one;

import java.util.concurrent.ConcurrentLinkedQueue;

public class HanoiTowerReconfigurer implements Runnable{
    private HanoiTower tower;
    private int from;
    private int to;
    private int height;
    private ConcurrentLinkedQueue<String> towerStates;

    //Завершена ли перестановка башни
    private boolean isDone = false;
    public boolean done(){
        return isDone;
    }

    public HanoiTowerReconfigurer(ConcurrentLinkedQueue<String> towerStates, HanoiTower tower, int from, int to, int height){
        this.tower = tower;
        this.from = from;
        this.to = to;
        this.height = height;
        this.towerStates = towerStates;
    }

    private void moveTower(int from, int to, int height){
        if(height == 1){
            moveDisk(from,to);
            return;
        }

        int other = 3 - from - to;
        moveTower(from, other, height-1);
        moveDisk(from,to);
        moveTower(other, to, height-1);
    }
    private void moveDisk(int from, int to){
        //Передвинуть диск башни
        tower.moveDisk(from,to);

        //Записать состояние башни в очередь
        towerStates.add(tower.toString());
    }

    @Override
    public void run(){
        towerStates.add(tower.toString());
        moveTower(from,to,height);
        isDone = true;
    }
}
