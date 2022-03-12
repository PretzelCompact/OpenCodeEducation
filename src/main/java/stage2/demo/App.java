package stage2.demo;

public class App {
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(123);
        Class.forName ("org.h2.Driver");
    }
}
