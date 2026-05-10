public class Edificio {
    String nombre_edificio;
    String tipo_edificio;
    int costo_edificio;
    int seguridad;
    int incendio;
    int felicidad;

    public Edificio (String nombre_edificio, String tipo_edificio, int costo_edificio, int seguridad, int incendio, int felicidad){
        this.nombre_edificio = nombre_edificio;
        this.tipo_edificio = tipo_edificio;
        this.costo_edificio = costo_edificio;
        this.seguridad = seguridad;
        this.incendio = incendio;
        this.felicidad = felicidad;
    }
}