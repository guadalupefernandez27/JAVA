import java.util.Scanner;
import java.util.ArrayList;
public class Ciudad {
    String nombre_ciudad;
    Alcalde alcalde;
    ArrayList<Edificio> edificios;

    public Ciudad(String nombre_ciudad, Alcalde alcalde){
        this.nombre_ciudad = nombre_ciudad;
        this.alcalde = alcalde;
        this.edificios = new ArrayList<>();
    }
}