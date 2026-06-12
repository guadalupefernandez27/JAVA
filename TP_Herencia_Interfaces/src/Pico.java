import java.util.List;

public abstract class Pico {
    protected String nombre;
    protected int durabilidad;
    protected int durabilidadMax;
    protected List<String> rompe;
    protected int irrompibilidad;
    protected int fortuna;
    protected int reparacion;

    public Pico(String nombre, int durabilidad, List<String> rompe) {
        this.nombre = nombre;
        this.durabilidad = durabilidad;
        this.durabilidadMax = durabilidad;
        this.rompe = rompe;
        this.irrompibilidad = 0;
        this.fortuna = 0;
        this.reparacion = 0;
    }

    public boolean puedeRomper(String mineral) {
        return rompe.contains(mineral);
    }

    public boolean usar() {
        durabilidad--;
        return durabilidad > 0;
    }

    public void aplicarIrrompibilidad(int nivel) {
        irrompibilidad = nivel;
        int extra = (int) (durabilidadMax * 0.05 * nivel);
        durabilidad += extra;
        durabilidadMax += extra;
    }

    public void aplicarFortuna(int nivel) {
        fortuna = nivel;
    }

    public void aplicarReparacion(int orbes) {
        reparacion = orbes;
        durabilidad += orbes * 2;
        if (durabilidad > durabilidadMax) durabilidad = durabilidadMax;
    }

    public int getDurabilidad() {
        return durabilidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFortuna() {
        return fortuna;
    }
}