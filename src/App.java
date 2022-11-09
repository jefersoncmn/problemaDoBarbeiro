public class App {
    public static void main(String[] args) throws Exception {
        Barbeiro barbeiro1 = new Barbeiro("Claudiao",2,5);
        Thread threadBarbeiro1 = new Thread(barbeiro1);
        threadBarbeiro1.start();

    }
}
