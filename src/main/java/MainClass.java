import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainClass {
    public static void main(String[] args){
        utils.Playable secondStage = new stage2.Runner();
        var br = new BufferedReader(new InputStreamReader(System.in));
        secondStage.play(br);
    }
}
