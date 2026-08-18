import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws InterruptedException {
        HiloCliente cliente = new HiloCliente("127.0.0.1");
        cliente.start();
        cliente.conectar();

        int intentos = 0;
        while (cliente.getMiId() == -1 && intentos < 50) {
            Thread.sleep(100);
            intentos++;
        }

        System.out.println("Escribi un mensaje y presiona Enter para enviarlo");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String texto = scanner.nextLine();
            cliente.enviarChat(texto);
        }
    }
}