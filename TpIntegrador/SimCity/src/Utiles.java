import java.util.Scanner;

public class Utiles {
    public static int verificar(String mensaje) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("-------------------------------");
                System.out.println("Error!! Ingrese un numero valido");
            }
        }
    }
}
