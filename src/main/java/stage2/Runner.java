package stage2;

import stage2.practice.one.Task1;
import utils.Playable;

import java.io.BufferedReader;
import java.io.IOException;

public class Runner implements utils.Playable {
    @Override
    public void play(BufferedReader br) {
        String input;
        Playable next = null;
        while (true){
            /*
            System.out.println("Введите номер этапа() или exit");
            try{
                input = br.readLine();
            } catch (IOException e){
                System.out.println("Ошибка ввода");
                continue;
            }*/
            input = "1";
            switch (input){
                case "exit":
                    return;
                case "1":
                    do{
                        System.out.println("Введите номер задания(1-3):");
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
                                System.out.println("Ошибка");
                                continue;
                        }
                        break;
                    } while(true);
                    break;
                default:
                    break;
            }
            if(next!=null)
                next.play(br);
        }
    }
}
