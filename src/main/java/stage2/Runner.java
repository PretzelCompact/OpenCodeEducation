package stage2;

import stage2.practice.one.RunnerPart1;
import stage2.practice.two.RunnerPart2;
import utils.Playable;

import java.io.BufferedReader;
import java.io.IOException;

public class Runner implements utils.Playable {
    @Override
    public void play(BufferedReader br) {
        String input;
        Playable next = null;

        System.out.println("Stage2");
        while (true){
            System.out.println("Введите номер части(1-2) или exit");
            try{
                input = br.readLine();
            } catch (IOException e){
                System.out.println("Ошибка ввода");
                continue;
            }
            switch (input){
                case "exit":
                    return;
                case "1":
                    next = new RunnerPart1();
                    break;
                case "2":
                    next = new RunnerPart2();
                    break;
                default:
                    break;
            }
            if(next!=null)
                next.play(br);
        }
    }
}
