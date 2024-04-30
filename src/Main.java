import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("LGP para el ISHOP-U");
        LGP objeto=new LGP("UniformS1.csv");
        objeto.Ejecutar();
        objeto.printOutput();

    }
}