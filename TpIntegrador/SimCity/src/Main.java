public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("-------------------------------");
    System.out.print("Nombre Alcalde: ");
    String nombre = sc.nextLine();
    System.out.print("Apellido Alcalde: ");
    String apellido = sc.nextLine();
    Alcalde alcalde = new Alcalde(nombre, apellido);
    System.out.println("-------------------------------");
    System.out.print("Nombre de la Ciudad: ");
    String nombre_ciudad = sc.nextLine();
    Ciudad ciudad = new Ciudad(nombre_ciudad, alcalde);
    String[][] datos = {
            {"Rascacielos", "Maravilla", "0", "0", "10", "900000"},
            {"Torre Eiffel", "Maravilla", "0", "0", "10", "850000"},
            {"Arco del triunfo", "Maravilla", "0", "0", "9", "700000"},
            {"Pirámide", "Maravilla", "0", "0", "9", "600000"},
            {"Obelisco", "Maravilla", "0", "0", "8", "450000"},
            {"Planta de energía eólica", "Planta energética", "0", "0", "10", "500000"},
            {"Planta de energía solar", "Planta energética", "0", "0", "9", "400000"},
            {"Planta de energía hidroeléctrica", "Planta energética", "0", "0", "8", "300000"},
            {"Planta de energía de carbón", "Planta energética", "0", "0", "4", "150000"},
            {"Empresa de agua corriente", "Planta de agua", "0", "0", "10", "750000"},
            {"Obras de cañerias para agua potable", "Planta de agua", "0", "0", "9", "500000"},
            {"Tanques de agua por manzana", "Planta de agua", "0", "10", "9", "350000"},
            {"Pozos de agua", "Planta de agua", "0", "0", "7", "200000"},
            {"Mega estación de policía", "Seguridad", "10", "0", "10", "400000"},
            {"Estación de policía grande", "Seguridad", "9", "0", "9", "300000"},
            {"Estación de policía mediana", "Seguridad", "7", "0", "5", "200000"},
            {"Estación de policía chica", "Seguridad", "5", "0", "2", "100000"},
            {"Mega estación de bomberos", "Incendios", "0", "10", "10", "450000"},
            {"Estación de bomberos grande", "Incendios", "0", "8", "8", "350000"},
            {"Estación de bomberos mediana", "Incendios", "0", "7", "6", "250000"},
            {"Estación de bomberos chica", "Incendios", "0", "6", "5", "150000"},
            {"Rutas", "Caminos", "0", "0", "10", "400000"},
            {"Autopistas", "Caminos", "0", "0", "9", "300000"},
            {"Avenidas", "Caminos", "0", "0", "8", "200000"},
            {"Calles de asfalto", "Caminos", "0", "0", "6", "150000"},
            {"Calles de tierra", "Caminos", "0", "0", "1", "50000"},
            {"Reserva natural", "Ecología", "0", "0", "10", "500000"},
            {"Parque polideportivo", "Ecología", "0", "0", "9", "300000"},
            {"Parque grande", "Ecología", "0", "0", "8", "200000"},
            {"Plaza", "Ecología", "0", "0", "7", "100000"}
    };
    System.out.println("-------------------------------");
    System.out.print("Desea comprar un edificio? (si/no): ");
    String decision = sc.nextLine();
    while (decision.equalsIgnoreCase("si")) {
        System.out.println("-------------------------------");
        System.out.println("\n---- CATALOGO DE EDIFICIOS ----");
        for (int i = 0; i < datos.length; i++) {
            System.out.println(i + " - " + datos[i][0] + " ($" + datos[i][5] + ")");
        }
        System.out.println("-------------------------------");
        System.out.print("\nOpción numerica o 'TORG' para dinero");
        String input = sc.nextLine();

        if (input.equalsIgnoreCase("TORG")) {
            alcalde.saldo_actual += 1000000;
            System.out.println("-------------------------------");
            System.out.println("Truco activado!!! Saldo: $" + alcalde.saldo_actual);
        } else {
            try {
                int op = Integer.parseInt(input);
                String nom = datos[op][0];
                String tipo = datos[op][1];
                int costo = Integer.parseInt(datos[op][5]);
                boolean repetido = false;
                for (Edificio e : ciudad.edificios) {
                    if (e.tipo_edificio.equalsIgnoreCase(tipo)) {
                        repetido = true;
                        break;
                    }
                }
                if (repetido) {
                    System.out.println("-------------------------------");
                    System.out.println("Error!! Ya tenes un edificio de tipo " + tipo);
                } else if (alcalde.saldo_actual >= costo) {

                    Edificio nuevo = new Edificio(nom, tipo, costo,
                            Integer.parseInt(datos[op][2]),
                            Integer.parseInt(datos[op][3]),
                            Integer.parseInt(datos[op][4]));

                    ciudad.edificios.add(nuevo);
                    alcalde.saldo_actual -= costo;
                    System.out.println("-------------------------------");
                    System.out.println(nom + " comprado");
                } else {
                    System.out.println("-------------------------------");
                    System.out.println("No tenes saldo suficiente");
                }
            } catch (Exception e) {
                System.out.println("-------------------------------");
                System.out.println("Opción inválida");
            }
        }
        System.out.println("-------------------------------");
        System.out.print("\n¿Comprar otro? (si/no): ");
        decision = sc.nextLine();
    }
    int menu = 0;
    do {
        System.out.println("-------------------------------");
        System.out.println("\n--- CIUDAD: " + ciudad.nombre_ciudad + " ---");
        System.out.println("1. Datos | 2. Seguridad | 3. Incendio | 4. Felicidad | 5. Caro/Barato | 6. Inversión Total | 7. Aporte Máximo | 8. Salir");
        menu = Utiles.verificar("Seleccione: ");
        int suma = 0;
            switch (menu) {
                case 1:
                    System.out.println("-------------------------------");
                    System.out.println("Alcalde: " + alcalde.nombre + " " + alcalde.apellido);
                    System.out.println("Edificios construidos:");
                    for (Edificio e : ciudad.edificios) System.out.println("- " + e.nombre_edificio + " (" + e.tipo_edificio + ")");
                    break;
                case 2:
                    System.out.println("-------------------------------");
                    for (Edificio e : ciudad.edificios) suma += e.seguridad;
                    System.out.println("Promedio Seguridad: " + (ciudad.edificios.isEmpty() ? 0 : (double)suma/ciudad.edificios.size()));
                    break;
                case 3:
                    System.out.println("-------------------------------");
                    for (Edificio e : ciudad.edificios) suma += e.incendio;
                    System.out.println("Promedio Incendio: " + (ciudad.edificios.isEmpty() ? 0 : (double)suma/ciudad.edificios.size()));
                    break;
                case 4:
                    System.out.println("-------------------------------");
                    for (Edificio e : ciudad.edificios) suma += e.felicidad;
                    System.out.println("Promedio Felicidad: " + (ciudad.edificios.isEmpty() ? 0 : (double)suma/ciudad.edificios.size()));
                    break;
                case 5: // Edificio más caro y más barato
                    if (ciudad.edificios.isEmpty()) {
                        System.out.println("No hay edificios");
                    } else {
                        Edificio caro = ciudad.edificios.get(0);
                        Edificio barato = ciudad.edificios.get(0);
                        for (Edificio e : ciudad.edificios) {
                            if (e.costo_edificio > caro.costo_edificio) caro = e;
                            if (e.costo_edificio < barato.costo_edificio) barato = e;
                        }
                        System.out.println("-------------------------------");
                        System.out.println("Más valioso: " + caro.nombre_edificio + " ($" + caro.costo_edificio + ")");
                        System.out.println("Menos valioso: " + barato.nombre_edificio + " ($" + barato.costo_edificio + ")");
                    }
                    break;
                case 6:
                    int total = 0;
                    for (Edificio e : ciudad.edificios) total += e.costo_edificio;
                    System.out.println("-------------------------------");
                    System.out.println("Total invertido: $" + total);
                    break;

                case 7:
                    if (ciudad.edificios.isEmpty()) {
                        System.out.println("No hay edificios");
                    } else {
                        Edificio mejor = ciudad.edificios.get(0);
                        for (Edificio e : ciudad.edificios) {
                            if (e.felicidad > mejor.felicidad) mejor = e;
                        }
                        System.out.println("-------------------------------");
                        System.out.println("El que mas aporta es: " + mejor.nombre_edificio);
                    }
                    break;
                case 8:
                    System.out.println("-------------------------------");
                    System.out.println("Gracias por jugar :) ");
                    System.out.println("-------------------------------");
                    break;
                default:
                    System.out.println("-------------------------------");
                    System.out.print("Ingrese opción valida");
            }
    } while (menu !=8);

}
