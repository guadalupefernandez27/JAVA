class Pista {
    String nombre;
    int metrosPorVuelta;
    int totalVueltas;

    public Pista(String nombre, int metrosPorVuelta, int totalVueltas) {
        this.nombre = nombre;
        this.metrosPorVuelta = metrosPorVuelta;
        this.totalVueltas = totalVueltas;
    }

    public int getLongitudTotal() { return metrosPorVuelta * totalVueltas; }
}