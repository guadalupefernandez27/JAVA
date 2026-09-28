package com.fastcash.core;

import com.fastcash.core.model.Modulo;
import com.fastcash.core.model.Nivel;
import com.fastcash.core.model.RecursoDef;

import java.util.List;

/**
 * Catalogo de los 4 modulos y sus 7 mapas cada uno (28 mapas en total).
 * Es el UNICO lugar donde hay que tocar texto/nombres: tanto server como
 * client leen de aca.
 *
 * Contenido real, transcripto de los ideas.txt de cada carpeta del Drive del
 * equipo (Modulo1..Modulo4). Los nombres de archivo (archivoFondo, items,
 * personajes) son EXACTAMENTE los que ya estan en Drive: la idea es que
 * alcance con copiar cada subcarpeta de mapa tal cual a
 * client/assets/modulos/<idModulo>/<idMapa>/ sin renombrar nada.
 * Ver client/assets/modulos/README.md para el detalle carpeta por carpeta,
 * incluidas las dudas (archivos duplicados, una inconsistencia en Modulo4)
 * que quedaron marcadas ahi para que las revisen.
 *
 * Economia: modulos 1 a 3 usan 2 recursos por mapa con el mismo valor/velocidad
 * en los 21 mapas (100/8 el comun, 500/10 el raro) — mantiene el mismo "feel"
 * que el juego original. Modulo 4 (barrios-caba) comparte una sola lista de
 * 7 billetes argentinos reales (10 a 20000 pesos) para sus 7 mapas, con su
 * propio puntajeVictoria mucho mas alto: son numeros de partida razonables,
 * faciles de ajustar aca mismo si al probarlo no se siente bien balanceado.
 */
public final class GameModules {

    private GameModules() {}

    private static final int VALOR_COMUN = 100;
    private static final float VELOCIDAD_COMUN = 8f;
    private static final int VALOR_RARO = 500;
    private static final float VELOCIDAD_RARO = 10f;
    private static final int PUNTAJE_VICTORIA_HISTORICO = 7000; // 1000 por mapa x 7 mapas

    public static final List<Modulo> MODULOS = List.of(
        moduloHistoriaMundial(),
        moduloHistoriaArgentina(),
        moduloTerritorioArgentino(),
        moduloBarriosCaba()
    );

    /** Busca un modulo por su id. Devuelve null si no existe (id invalido). */
    public static Modulo porId(String id) {
        if (id == null) return null;
        for (Modulo m : MODULOS) {
            if (m.id.equals(id)) return m;
        }
        return null;
    }

    // ---------------------------------------------------------------------
    // Modulo 1: Historia Mundial
    // Personajes: en Drive, las 7 carpetas de este modulo tienen "hombre.png"
    // y "mujer.png" (reemplazaron a los viejos pjm_X/pjw_X el 23/09).
    // ---------------------------------------------------------------------
    private static Modulo moduloHistoriaMundial() {
        List<Nivel> niveles = List.of(
            new Nivel("01_edad_piedra", "Edad de Piedra (Prehistoria)",
                "Cavernas rocosas con pinturas rupestres, selva de helechos gigantes y campamento nomada al aire libre.",
                "fondo_piedra.jpg", "hombre.png", "mujer.png",
                recursos("item1_piedra.jpg", "item2_piedra.jpg")),
            new Nivel("02_edad_clasica", "Antiguedad Clasica (Imperios Antiguos)",
                "Gran Piramide de Guiza, calles del Foro Romano y acueductos bajo el sol mediterraneo.",
                "fondo_clasica.jpg", "hombre.png", "mujer.png",
                recursos("item1_clasica.jpg", "item2_clasica.jpg")),
            new Nivel("03_edad_media", "Edad Media (Mundo Feudal)",
                "Aldea medieval de madera, murallas de castillo e interiores de mazmorras iluminadas por antorchas.",
                "fondo_media.jpg", "hombre.png", "mujer.png",
                recursos("item1_media.jpg", "item2_media.jpg")),
            new Nivel("04_edad_pirateria", "Era de los Descubrimientos y Pirateria (Siglos XVI-XVIII)",
                "Puerto colonial, cubierta de un barco galeon en alta mar y caleta pirata con cuevas marinas.",
                "fondo_pirateria.jpg", "hombre.png", "mujer.png",
                recursos("item1_pirateria.jpg", "item2_pirateria.jpg")),
            new Nivel("05_edad_revolucionindustrial", "Revolucion Industrial (Siglo XIX)",
                "Fabricas de ladrillo rojo con chimeneas humeantes, estacion de trenes a vapor y calles adoquinadas con faroles de gas.",
                "fondo_revolucion.jpg", "hombre.png", "mujer.png",
                recursos("item1_industrial.jpg", "item2_industrial.jpg")),
            new Nivel("06_edad_metropolis", "Metropolis Moderna (Siglo XX-XXI)",
                "Rascacielos acristalados, avenidas con trafico urbano y distrito financiero iluminado.",
                "fondo_metropolis.jpg", "hombre.png", "mujer.png",
                recursos("item1_metropolis.jpg", "item2_metropolis.jpg")),
            new Nivel("07_edad_futura", "Era Ciber-Futurista (Futuro Cercano)",
                "Centro de datos con servidores masivos, callejones nocturnos con neon e interfaz holografica de trading.",
                "fondo_futuros.jpg", "hombre.png", "mujer.png",
                recursos("item1_futura.jpg", "item2_futura.jpg"))
        );
        return new Modulo("historia-mundial", "Historia Mundial", niveles, null, PUNTAJE_VICTORIA_HISTORICO);
    }

    // ---------------------------------------------------------------------
    // Modulo 2: Historia Argentina
    // Personajes: pjm_X / pjw_X (sin reemplazo mas nuevo en este modulo).
    // ---------------------------------------------------------------------
    private static Modulo moduloHistoriaArgentina() {
        List<Nivel> niveles = List.of(
            new Nivel("01_epoca_precolombina", "Epoca Precolombina (Pueblos Originarios)",
                "Pucara de Tilcara / Quebrada de Humahuaca con terrazas de cultivo, sierras del interior y campamento de tolderias.",
                "fondo_humahua.jpg", "pjm_edad_precolombina.jpg", "pjw_edad_precolombina.jpg",
                recursos("item1_precolombina.jpg", "item2_precolombina.jpg")),
            new Nivel("02_epoca_colonial", "Epoca Colonial y Virreinato del Rio de la Plata (Siglo XVIII-1810)",
                "Finca virreinal con aljibes, la Recova y el Cabildo de Buenos Aires bajo la lluvia de Mayo, y orillas del Rio de la Plata con carretas.",
                "fondo_cabildo2d.jpg", "pjm_epoca_colonial.jpg", "pjw_epoca_colonial.jpg",
                recursos("item1_colonial.jpg", "item2_colonial.jpg")),
            new Nivel("03_epoca_indepencia", "Independencia y Campañas de la Patria (1816-1820s)",
                "La Casa de Tucuman con sus columnas salomonicas, paso de los Andes con cordillera nevada de fondo y campamento militar patriota.",
                "fondo_andes.jpg", "pjm_epoca_independencia.jpg", "pjw_epoca_independencia.jpg",
                recursos("item1_independencia.jpg", "item2_independencia.jpg")),
            new Nivel("04_epoca_organizacion", "Era de la Organizacion Nacional y la Frontera (1830s-1870s)",
                "Estancia bonaerense con pulperia de adobe, llanura pampeana con ombues al atardecer y fortin de la frontera con mangrullo.",
                "fondo_rural.jpg", "pjm_epoca_organizacion.jpg", "pjw_epoca_organizacion.jfif",
                recursos("item1_organizacion.jpg", "item2_organizacion.jpg")),
            new Nivel("05_epoca_agroexportador", "Modelo Agroexportador y Gran Inmigracion (1880-1920)",
                "Puerto de Buenos Aires con barcos de vapor e inmigrantes, campos dorados de trigo con molinos de viento y Hotel de Inmigrantes.",
                "fondo_puerto.jpg", "pjm_epoca_agroexportadora.jfif", "pjw_epoca_agroexportadora.jfif",
                recursos("item1_agroexportador.jpg", "item2_agroexportador.jpg")),
            new Nivel("06_epoca_industrializacion", "Industrializacion y Belle Epoque Porteña (1930s-1960s)",
                "Avenida Corrientes antigua con tranvias y luces de teatro, el Obelisco recien construido y talleres metalurgicos del conurbano.",
                "fondo_obelisco.jpg", "pjm_epoca_industrializacion.jfif", "pjw_epoca_industrializacion.jfif",
                recursos("item1_industrilizacion.jpg", "item2_industrializacion.jpg")),
            new Nivel("07_epoca_actual", "Epoca Contemporanea y Era Digital (Actualidad)",
                "Microcentro porteño / peatonal Florida con casas de cambio, Puerto Madero con rascacielos y avenida con colectivos y murga urbana.",
                "fondo_actual.jpg", "pjm_epoca_actual.jfif", "pjw_epoca_actual.jfif",
                recursos("item1_actual.jpg", "item2_actual.jpg"))
        );
        return new Modulo("historia-argentina", "Historia Argentina", niveles, null, PUNTAJE_VICTORIA_HISTORICO);
    }

    // ---------------------------------------------------------------------
    // Modulo 3: Territorio Argentino
    // Personajes: pjm_X / pjw_X (o pjw-X con guion en un par de mapas, real
    // nombre de archivo en Drive, no un typo mio).
    // ---------------------------------------------------------------------
    private static Modulo moduloTerritorioArgentino() {
        List<Nivel> niveles = List.of(
            new Nivel("01_noa_jujuy_salta", "NOA - Jujuy y Salta",
                "Cerros multicolores de la Serrania del Hornocal y la Quebrada de Humahuaca, terrazas de cultivo ancestrales, cardones gigantes y Salinas Grandes.",
                "fondo_noa.jpg", "pjm_noa.jfif", "pjw-noa.jfif",
                recursos("item1_noa.jpg", "item2_noa.jpg")),
            new Nivel("02_nea_misiones_corrientes", "NEA - Misiones y Corrientes",
                "Espesa selva paranaense con helechos gigantes y palmeras caranday; al fondo, la bruma y los saltos de las Cataratas del Iguazu.",
                "fondo_nea.jpg", "pjm_nea.jfif", "pjw-nea.jfif",
                recursos("item1_nea.jpg", "item2_nea.jpg")),
            new Nivel("03_cuyo_mendoza_sanjuan", "Cuyo - Mendoza y San Juan",
                "La pared nevada de la Cordillera de los Andes con el Aconcagua al fondo, viñedos en spaliers, alamos dorados y acequias.",
                "fondo_cuyo.jpg", "pjm_cuyo.jfif", "pjw_cuyo.jfif",
                recursos("item1_cuyo.jpg", "item2_cuyo.jpg")),
            new Nivel("04_centro_cordoba_sanluis", "Centro - Cordoba y San Luis",
                "Sierras verdes redondeadas, arroyos de montaña con piedras pulidas y la arquitectura colonial de las Estancias Jesuiticas.",
                "fondo_centro.jpg", "pjm_centro.jfif", "pjw_centro.jfif",
                recursos("item1_centro.jpg", "item2_centro.jpg")),
            new Nivel("05_pampahumeda_bs_pampa", "Pampa Humeda - Buenos Aires y La Pampa",
                "La llanura infinita con horizontes limpios al atardecer, estancias rurales, molinos de viento y sombras de ombues lejanos.",
                "fondo_papahumeda.jpg", "pjm_pampahumeda.jfif", "pjw_pampahumeda.jfif",
                recursos("item1_pampahumeda.jpg", "item2_pampahumeda.jpg")),
            new Nivel("06_cordillera_rionegro_neuquen", "Patagonia Cordillerana - Rio Negro y Neuquen",
                "Lagos turquesas rodeados de bosques de coihues y araucarias; al fondo, picos con nieve eterna y villas de montaña alpinas.",
                "fondo_cordillera.jpg", "pjm_cordillera.jfif", "pjw_cordillera.jfif",
                recursos("item1_cordillera.jpg", "item2_cordillera.jpg")),
            new Nivel("07_austral_santacruz_tierradelfuego", "Patagonia Austral - Santa Cruz y Tierra del Fuego",
                "La pared de hielo del Glaciar Perito Moreno sobre el Lago Argentino y los picos de granito del Monte Fitz Roy.",
                "fondo_austral.jpg", "pjm_austral.jfif", "pjw_austral.jfif",
                recursos("item1_austral.jpg", "item2_austral.jpg"))
        );
        return new Modulo("territorio-argentino", "Territorio Argentino", niveles, null, PUNTAJE_VICTORIA_HISTORICO);
    }

    // ---------------------------------------------------------------------
    // Modulo 4: Barrios de CABA
    // A diferencia de los otros 3, ACA los recursos son compartidos por todo
    // el modulo (7 billetes argentinos reales, subidos sueltos en la raiz de
    // "Modulo4" en Drive, no dentro de cada carpeta de barrio): van en
    // client/assets/modulos/barrios-caba/recursos/, no en cada subcarpeta de mapa.
    // ---------------------------------------------------------------------
    private static Modulo moduloBarriosCaba() {
        List<Nivel> niveles = List.of(
            new Nivel("01_lomas_de_zamora", "Lomas de Zamora",
                "Vista urbana de Zona Sur con vias del ferrocarril Roca, calles de adoquines, casas bajas de ladrillo visto y comercios locales.",
                "fondo_lomas.jpg", "pjm_lomas_de_zamora.jfif", "pjw_lomas_de_zamora.jfif", null),
            new Nivel("02_villa_lugano", "Villa Lugano",
                "Calzada ancha de asfalto con la silueta de la Torre Espacial del Parque de la Ciudad y los monoblocks del barrio de fondo.",
                "fondo_lugano.jpg", "pjm_villa_lugano.jfif", "pjw_villa_lugano.jfif", null),
            new Nivel("03_la_boca", "La Boca",
                "Pasaje inspirado en Caminito, conventillos de chapa ondulada en colores primarios saturados y el riachuelo de fondo.",
                "fondo_la_boca.jpg", "pjm_la_boca.jfif", "pjw_la_boca.jfif", null),
            new Nivel("04_palermo", "Palermo",
                "Senderos entre veredas arboladas, arcos de piedra de parques y locales gastronomicos modernos con carteles de neon tenue.",
                "fondo_palermo.jpg", "pjm_palermo.jfif", "pjw_palermo.jfif", null),
            new Nivel("05_recoleta", "Recoleta",
                "Arquitectura neoclasica de marmol blanco, portones de hierro forjado y edificios de estilo frances bajo un cielo despejado.",
                "fondo_recoleta.jpg", "pjm_recoleta.jfif", "pjw_recoleta.jfif", null),
            // OJO: esta carpeta se llama "06_belgrano" y sus archivos dicen "belgrano",
            // pero el texto que escribieron en el ideas.txt describe Nuñez / el Monumental.
            // Lo dejamos con el titulo real que escribieron (Nuñez); ver el README de
            // assets para la nota completa sobre esta inconsistencia.
            new Nivel("06_belgrano", "Nuñez (Cancha de River / Barrio Parque)",
                "Avenida arbolada con la estructura del estadio de River Plate (el Mas Monumental) dominando el fondo al atardecer.",
                "fondo_belgrano.jpg", "pjm_belgrano.jfif", "pjw_belgrano.jfif", null),
            new Nivel("07_puerto_madero", "Puerto Madero",
                "Deck de madera de muelle en primer plano, rascacielos de vidrio e iluminacion nocturna en tonos azul frio y cian.",
                "fondo_puerto_madero.jpg", "pjm_puerto_madero.jfif", "pjw_puerto_madero.jfif", null)
        );

        // 7 billetes argentinos reales, compartidos por los 7 mapas de este modulo.
        // Velocidad creciente por denominacion (el de 20000 cae mas rapido / es mas dificil).
        List<RecursoDef> billetesReales = List.of(
            new RecursoDef("10", "diez_pesos.jpg", 10, 7f),
            new RecursoDef("50", "cincuenta_pesos.jpg", 50, 8f),
            new RecursoDef("100", "cien_pesos.jpg", 100, 9f),
            new RecursoDef("1000", "mil_pesos.jpg", 1000, 10f),
            new RecursoDef("2000", "dos_mil_pesos.jpg", 2000, 11f),
            new RecursoDef("10000", "diez_mil_pesos.jpg", 10000, 12.5f),
            new RecursoDef("20000", "veinte_mil_pesos.jpg", 20000, 14f)
        );

        // Umbral bastante mas alto que los otros modulos (billetes reales,
        // no vasijas de barro): numero de partida, facil de ajustar aca.
        int puntajeVictoriaCaba = 100_000;

        return new Modulo("barrios-caba", "Barrios de CABA", niveles, billetesReales, puntajeVictoriaCaba);
    }

    /** Los 2 recursos "comunes" de un mapa de modulo historico: item1 (comun) e item2 (raro). */
    private static List<RecursoDef> recursos(String archivoItem1, String archivoItem2) {
        return List.of(
            new RecursoDef("item1", archivoItem1, VALOR_COMUN, VELOCIDAD_COMUN),
            new RecursoDef("item2", archivoItem2, VALOR_RARO, VELOCIDAD_RARO)
        );
    }
}
