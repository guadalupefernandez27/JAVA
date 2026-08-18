public class Servidor {
    public static void main(String[] args) {
        HiloServidor servidor = new HiloServidor();
        servidor.start();
        System.out.println("Servidor iniciado");
    }
}