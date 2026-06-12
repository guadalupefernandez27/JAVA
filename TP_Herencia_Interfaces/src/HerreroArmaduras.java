import java.util.Scanner;

public class HerreroArmaduras implements Vendedor {
    public void mostrarOfertas() {
        System.out.println("1. 4 hierro x 1 esmeralda");
        System.out.println("2. 6 carbon x 1 esmeralda");
        System.out.println("3. 2 oro x 1 esmeralda");
        System.out.println("4. 1 diamante x 1 esmeralda");
        System.out.println("5. Armadura de oro - 12 esmeraldas");
        System.out.println("6. Armadura de hierro - 20 esmeraldas");
        System.out.println("7. Armadura de diamante - 30 esmeraldas");
        System.out.println("8. Armadura de netherite - 50 esmeraldas");
    }

    public void comprar(Jugador j, Scanner sc) {
        mostrarOfertas();
        int op = sc.nextInt();
        switch (op) {
            case 1:
                if (j.gastarEsmeraldas(1)) j.agregarItem("hierro", 4);
                break;
            case 2:
                if (j.gastarEsmeraldas(1)) j.agregarItem("carbon", 6);
                break;
            case 3:
                if (j.gastarEsmeraldas(1)) j.agregarItem("oro", 2);
                break;
            case 4:
                if (j.gastarEsmeraldas(1)) j.agregarItem("diamante", 1);
                break;
            case 5:
                if (j.gastarEsmeraldas(12)) { j.reduccionMob = 0.05; System.out.println("comprada armadura de oro"); }
                break;
            case 6:
                if (j.gastarEsmeraldas(20)) { j.reduccionMob = 0.20; System.out.println("comprada armadura de hierro"); }
                break;
            case 7:
                if (j.gastarEsmeraldas(30)) { j.reduccionMob = 0.50; System.out.println("comprada armadura de diamante"); }
                break;
            case 8:
                if (j.gastarEsmeraldas(50)) { j.reduccionMob = 1.0; System.out.println("comprada armadura de netherite"); }
                break;
            default:
                System.out.println("no entendi");
        }
    }
}