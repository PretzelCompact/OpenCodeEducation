import stage2.Runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClass {
    public static void main(String[] args) throws IOException {
        utils.Playable secondStage = new Runner();
        var br = new BufferedReader(new InputStreamReader(System.in));
        secondStage.play(br);
        br.close();
    }
}
