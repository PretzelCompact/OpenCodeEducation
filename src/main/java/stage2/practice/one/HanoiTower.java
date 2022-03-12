package stage2.practice.one;

import java.util.LinkedList;

public class HanoiTower{
    public static final int NUMBER_OF_LINES = 3;

    private LinkedList<Integer>[] lines;
    private int height;

    public int getHeight(){
        return height;
    }

    public HanoiTower(int startLine, int height){
        this.height = height;
        lines = new LinkedList[NUMBER_OF_LINES];
        for(int i = 0; i < NUMBER_OF_LINES; i++){
            lines[i] = new LinkedList<>();
            if(i == startLine){
                for(int n = height; n >= 1; n--){
                    lines[i].push(n);
                }
            }
        }
    }

    public void moveDisk(int from, int to){
        if(from < 0 || from > NUMBER_OF_LINES)
            throw new RuntimeException("Line " + from + " doesn't exist in Hanoi Tower");
        if(to < 0 || to > NUMBER_OF_LINES)
            throw new RuntimeException("Line " + to + " doesn't exist in Hanoi Tower");

        if(from == to)
            return;

        if(!lines[to].isEmpty() && lines[from].peek() > lines[to].peek())
            throw new RuntimeException("It's impossible to move disk from " + from + " to " + to);

        lines[to].push(lines[from].pop());
    }

    private String printDisk(int line, int level){
        //Корректно вывести диск башни
        //Учитывается положение и размер

        int sumLength = height*2+1;

        int index = lines[line].size() - level - 1;
        if(index < 0)
            return " ".repeat(sumLength+2);

        int size = lines[line].get(index);
        int margin = sumLength/2-size + (sumLength%2==0 ? 1 : 0);
        var str = " ".repeat(margin) +
                '|' +
                "x".repeat(sumLength-margin*2) +
                '|' +
                " ".repeat(margin);
        return str;
    }

    public static int getLineFromName(char name){
        int result = (int)name - (int)'A';
        if(result < 0 || result >= NUMBER_OF_LINES)
            throw new RuntimeException("Line " + name + " doesn't exist");
        return result;
    }

    private String printLineName(int lineNumber){
        int sumLength = height*2+1;
        char name = (char)((int)'A' + lineNumber);
        return " ".repeat(sumLength/2+1) + name + " ".repeat(sumLength/2+1);
    }

    @Override
    public String toString(){
        var sb = new StringBuilder();
        String delimiter = "\t#\t";

        for(int i = height-1; i >= 0; i--){
            for(int j = 0; j < NUMBER_OF_LINES; j++){
                sb.append(printDisk(j,i) + delimiter);
            }
            sb.append('\n');
        }
        for(int i = 0; i < NUMBER_OF_LINES; i++){
            sb.append(printLineName(i) + delimiter);
        }
        return sb.toString();
    }
}