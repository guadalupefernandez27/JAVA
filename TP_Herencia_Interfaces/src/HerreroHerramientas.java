import java.util.Scanner;

public class HerreroHerramientas implements Vendedor {
    public void mostrarOfertas() {
        System.out.println("1. Pico de piedra - 6 esmeraldas");
        System.out.println("2. Pico de hierro - 12 esmeraldas");
        System.out.println("3. Pico de diamante - 20 esmeraldas");
    }

    public void comprar(Jugador j, Scanner sc) {
        mostrarOfertas();
        int op = sc.nextInt();
        switch (op) {
            case 1:
                if (j.gastarEsmeraldas(6)) j.pico = new PicoPiedra();
                break;
            case 2:
                if (j.gastarEsmeraldas(12)) j.pico = new PicoHierro();
                break;
            case 3:
                if (j.gastarEsmeraldas(20)) j.pico = new PicoDiamante();
                break;
            default:
                System.out.println("no entendi");
        }
    }
}