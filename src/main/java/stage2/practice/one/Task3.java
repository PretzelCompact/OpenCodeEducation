package stage2.practice.one;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.BitSet;

public class Task3 implements utils.Playable{
    @Override
    public void play(BufferedReader br) {
        BitSet bits;
        String input;
        AlphabetArchivator alphabet;

        System.out.println("Введите символы алфавита(без пробелов, 'ABCDEF'):");
        do{
            try{
                input = br.readLine();
            } catch (IOException e){
                System.out.println("Ошибка ввода. Попробуйте снова");
                input = null;
            }
        } while(input == null);
        alphabet = new AlphabetArchivator(input.toCharArray());

        System.out.println("-".repeat(20));
        System.out.println("УПРАВЛЕНИЕ");
        printHelp();
        System.out.println("-".repeat(20));

        while (true){
            try {
                switch (br.readLine()){
                    case "list":
                        System.out.println(alphabet);
                        break;
                    case "compress":
                        System.out.println("Введите строку из символов алфавита:");
                        input = br.readLine();
                        bits = alphabet.compress(input);
                        System.out.println(BitsString.get(bits,alphabet.getNumOfBits() * input.length()));
                        break;
                    case "decompress":
                        System.out.println("Введите строку бит(пробелы не влияют):");
                        input = br.readLine().replace(" ", "");
                        bits = BitsString.convert(input);
                        System.out.println(alphabet.decompress(bits,input.length()));
                        break;
                    case "exit":
                        return;
                    case "help":
                        printHelp();
                        break;
                    default:
                        System.out.println("Неизвестная команда");
                        break;
                }
            } catch (Exception e){
                System.out.println("Произошла ошибка");
            } finally {
                System.out.println("-".repeat(20));
            }
        }
    }

    private static void printHelp(){
        System.out.println("-".repeat(20));
        System.out.println("help -- список команд");
        System.out.println("list -- вывести весь алфавит");
        System.out.println("compress -- закодировать строку");
        System.out.println("decompress -- раскодировать строку");
        System.out.println("exit -- выход");
    }
}
