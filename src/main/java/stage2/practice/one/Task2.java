package stage2.practice.one;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Task2 implements utils.Playable{
    @Override
    public void play(BufferedReader br) {

        int height, startLine, finishLine;
        String input;
        long frameDelay;

        do{
            System.out.println("Введите высоту башни:");
            try{
                height = Integer.parseInt(br.readLine());
            } catch (IOException e){
                System.out.println("Ошибка ввода");
                height = -1;
                continue;
            }
            if(height <= 0){
                System.out.println("Высота башни должна быть больше нуля");
            }
        } while(height <= 0);

        do{
            System.out.println("Введите название начального столбца(A,B,C):");
            try{
                input = br.readLine();
            } catch (IOException e){
                System.out.println("Ошибка ввода");
                startLine = -1;
                continue;
            }
            switch (input){
                case "A":
                case "B":
                case "C":
                    startLine = HanoiTower.getLineFromName(input.charAt(0));
                    break;
                default:
                    startLine = -1;
                    continue;
            }
        } while(startLine == -1);

        do{
            System.out.println("Введите название конечного столбца(A,B,C):");
            try{
                input = br.readLine();
            } catch (IOException e){
                System.out.println("Ошибка ввода");
                finishLine = -1;
                continue;
            }
            switch (input){
                case "A":
                case "B":
                case "C":
                    finishLine = HanoiTower.getLineFromName(input.charAt(0));
                    break;
                default:
                    finishLine = -1;
                    continue;
            }
        } while(finishLine == -1);

        do{
            System.out.println("Введите задержку между соседними кадрами в милисекудах(целое число):");
            try{
                frameDelay = Long.parseLong(br.readLine());
            } catch (IOException e){
                System.out.println("Ошибка ввода");
                frameDelay = -1;
                continue;
            }
        } while(frameDelay < 0);

        var tower = new HanoiTower(startLine,height);
        var queue = new ConcurrentLinkedQueue<String>();
        var configurer = new HanoiTowerReconfigurer(queue,tower,startLine,finishLine,tower.getHeight());

        int it = 1;
        new Thread(configurer).start();
        while (!configurer.done() || configurer.done() && !queue.isEmpty()){
            try{
                Thread.sleep(frameDelay);
            } catch (InterruptedException e){
                System.out.println("Выполнение потока было прервано");
                return;
            }
            while (queue.isEmpty());
            System.out.println("Число манулов: " + it++);
            System.out.println(queue.poll());
            System.out.println("-".repeat((tower.getHeight()*2+7)*HanoiTower.NUMBER_OF_LINES));
        }
    }
}
