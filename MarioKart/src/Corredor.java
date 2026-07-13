import java.util.Random;
class Corredor extends Thread {
    String nombre;
    double velocidadFinal;
    int estabilidadFinal, driftFinal;
    Pista pista;

    int distanciaRecorrida = 0;
    int tiempoFinal = 0;
    int puestoLlegada;

    static int puestoActual = 1;
    private HiloReloj reloj;

    public Corredor(String nombre, double velBase, int pesoBase, int driftBase,
                    Vehiculo v, Componente rueda, Componente chasis, Pista pista, HiloReloj reloj) {
        this.nombre = nombre;
        this.pista = pista;
        this.reloj = reloj;

        int pesoTotal = pesoBase + v.peso + rueda.pesoMod + chasis.pesoMod;
        this.estabilidadFinal = v.estabilidad + rueda.estMod + chasis.estMod;
        this.driftFinal = driftBase + v.drift + rueda.drifMod + chasis.drifMod;

        this.velocidadFinal = velBase - (pesoTotal * 3);
        if (this.velocidadFinal < 5) this.velocidadFinal = 5;
    }

    @Override
    public void run() {
        Random rand = new Random();
        int totalMetros = pista.getLongitudTotal();

        while (distanciaRecorrida < totalMetros) {
            try {
                Thread.sleep(1000);

                double avance = velocidadFinal + (rand.nextInt(5) - 2);

                if (estabilidadFinal < 10) {
                    int probCaida = (10 - estabilidadFinal) * 5;
                    if (rand.nextInt(100) < probCaida) {
                        avance -= 5;
                        System.out.println("[" + nombre + "] Exceso de inestabilidad: pierde 5 metros.");
                    }
                }

                if (driftFinal > 10) {
                    int probBoost = (driftFinal - 10) * 5;
                    if (rand.nextInt(100) < probBoost) {
                        avance += 10;
                        System.out.println("[" + nombre + "] Traccion efectiva: boost de +10 metros.");
                    }
                }

                if (avance < 0) avance = 0;
                distanciaRecorrida += avance;

                int vueltaActual = (distanciaRecorrida / pista.metrosPorVuelta) + 1;
                if (vueltaActual > pista.totalVueltas) vueltaActual = pista.totalVueltas;
                System.out.printf("Corredor: %s | Vuelta: %d | Distancia: %d metros\n", nombre, vueltaActual, distanciaRecorrida);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.tiempoFinal = reloj.getSegundos();

        synchronized (Corredor.class) {
            this.puestoLlegada = puestoActual;
            puestoActual++;
        }
        System.out.println(">>> [" + nombre + "] HA CRUZADO LA LINEA DE META <<<");
    }
}