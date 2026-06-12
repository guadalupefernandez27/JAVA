public class Comida extends Item {
    private double recupera;
    public Comida(String nombre, int cantidad, double recupera) {
        super(nombre, cantidad);
        this.recupera = recupera;
    }
    public double getRecupera() {
        return recupera;
    }
}