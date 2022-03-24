package stage2.practice.one;

import utils.Playable;

import java.io.BufferedReader;
import java.io.IOException;

public class RunnerPart1 implements utils.Playable{
    @Override
    public void play(BufferedReader br){
        String input;
        utils.Playable next;
        do{
            System.out.println("Введите номер задания(1-3) или exit:");
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
                    next = new stage2.practice.one.Task1();
                    break;
                case "2":
                    next = new stage2.practice.one.Task2();
                    break;
                case "3":
                    next = new stage2.practice.one.Task3();
                    break;
                default:
                    System.out.println("Ошибка ввода");
                    continue;
            }
            break;
        } while(true);
    }
}
