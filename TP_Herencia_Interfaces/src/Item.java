public abstract class Item {
    protected String nombre;
    protected int cantidad;

    public Item(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void agregar(int c) {
        cantidad += c;
    }

    public boolean quitar(int c) {
        if (cantidad >= c) {
            cantidad -= c;
            return true;
        }
        return false;
    }
}