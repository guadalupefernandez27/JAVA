class HiloReloj extends Thread {
    private boolean carreraActiva = true;
    private int segundos = 0;

    public void detenerReloj() {
        this.carreraActiva = false;
    }

    @Override
    public void run() {
        while (carreraActiva) {
            try {
                Thread.sleep(1000);
                segundos++;
                System.out.println("[RELOJ GLOBAL] Tiempo transcurrido: " + segundos + "s");
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public int getSegundos() { return segundos; }
}