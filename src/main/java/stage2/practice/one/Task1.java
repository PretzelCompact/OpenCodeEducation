package stage2.practice.one;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

public class Task1 implements utils.Playable{
    @Override
    public void play(BufferedReader br) {
        String str;
        while(true){
            System.out.println("Введите строку или exit:");
            try{
                str = br.readLine();
                if(str.equals("exit")){
                    break;
                }

                System.out.println(stringCorrect(str) ? "CORRECT" : "WRONG");
                System.out.println("-------------------------------");
            } catch (IOException e){
                System.out.println("Ошибка ввода/вывода");
            }
        }
        System.out.println("-------------\nSee you soon!");
    }

    enum Bracket{
        round('(',')'),
        square('[',']'),
        curly('{','}');

        private final int open;
        private final int close;

        public int getOpen(){
            return open;
        }
        public int getClose(){
            return close;
        }
        public static Bracket get(int sym){
            for(var br : Bracket.values()){
                if(br.getClose() == sym || br.getOpen() == sym)
                    return br;
            }
            return null;
        }

        Bracket(char open, char close){
            this.open = (int)open;
            this.close = (int)close;
        }
    }

    private static boolean stringCorrect(String str){
        var stack = new LinkedList<Bracket>();

        for(int sym : str.chars().toArray()){
            var br = Bracket.get(sym);

            //Если символ не скобка
            if(br == null)
                continue;

            //Открывающая ли скобка
            boolean isOpen = br.getOpen() == sym;

            //Первая скобка закрывающая
            //Case: "}"
            //Case: "{}[])"
            if(stack.isEmpty() && !isOpen)
                return false;

            //Скобка закрывающая, но отсуствует открывающая
            //Или между открывающей и закрывающей открылась другая скобка
            //
            //Case: "{(}"
            //Case: "[({)"
            if(!stack.isEmpty() && !stack.peek().equals(br) && !isOpen)
                return false;

            //Открывающая и закрывающая скобка "схлопываются"
            //
            //Case: "([]"  => "("
            //Case: "[({}" => "[("
            if(!stack.isEmpty() && stack.peek().getClose() == sym){
                stack.pop();
                continue;
            }

            //Если не один из случае сверху
            //
            //Case: "(("
            //Case: "["
            //Case: "[("
            stack.push(br);
        }
        return stack.isEmpty();
    }
}
