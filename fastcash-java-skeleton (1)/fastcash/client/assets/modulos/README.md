# Assets de los modulos (contenido real, del Drive del equipo)

Esto ya NO es un placeholder: los nombres de abajo son EXACTAMENTE los que
estan en el Drive ("FAST_CASH" > Modulo1..Modulo4). La idea es que alcance
con copiar cada subcarpeta de mapa tal cual, sin renombrar nada.

## Como copiarlo

Por cada uno de los 28 mapas de abajo: bajar esa carpeta desde Drive (click
derecho > Descargar sobre la carpeta del mapa, ej. "01_edad_piedra") y
pegarla sin cambios dentro de:

    client/assets/modulos/<id-del-modulo>/<id-del-mapa>/

Ejemplo: la carpeta de Drive "Modulo1/01_edad_piedra" va a parar a
`client/assets/modulos/historia-mundial/01_edad_piedra/`, con TODOS sus
archivos adentro (fondo_piedra.jpg, item1_piedra.jpg, item2_piedra.jpg,
hombre.png, mujer.png, y el ideas.txt de yapa, que no hace falta borrar:
el juego no lo lee, pero tampoco molesta si se queda ahi).

Los 7 billetes reales de barrios-caba (estaban sueltos en la raiz de
"Modulo4" en Drive, no dentro de una carpeta de mapa) van aparte, en:

    client/assets/modulos/barrios-caba/recursos/

## Modulo 1: historia-mundial (7 mapas, personajes = hombre.png / mujer.png)

| id de mapa                     | fondo               | item1              | item2              |
|---------------------------------|----------------------|----------------------|----------------------|
| 01_edad_piedra                  | fondo_piedra.jpg     | item1_piedra.jpg     | item2_piedra.jpg     |
| 02_edad_clasica                 | fondo_clasica.jpg    | item1_clasica.jpg    | item2_clasica.jpg    |
| 03_edad_media                   | fondo_media.jpg      | item1_media.jpg      | item2_media.jpg      |
| 04_edad_pirateria                | fondo_pirateria.jpg  | item1_pirateria.jpg  | item2_pirateria.jpg  |
| 05_edad_revolucionindustrial     | fondo_revolucion.jpg | item1_industrial.jpg | item2_industrial.jpg |
| 06_edad_metropolis               | fondo_metropolis.jpg | item1_metropolis.jpg | item2_metropolis.jpg |
| 07_edad_futura                   | fondo_futuros.jpg    | item1_futura.jpg     | item2_futura.jpg     |

Los 7 mapas tienen "hombre.png" y "mujer.png" (los subio Mili el 23/09,
reemplazando a unos pjm_X/pjw_X mas viejos que tambien estan ahi: se puede
ignorar/borrar esos viejos, el juego usa hombre.png/mujer.png).

## Modulo 2: historia-argentina (7 mapas, personajes = pjm_X / pjw_X)

| id de mapa               | fondo               | item1                        | item2                      | personajes                                             |
|----------------------------|-----------------------|---------------------------------|--------------------------------|-----------------------------------------------------------|
| 01_epoca_precolombina      | fondo_humahua.jpg     | item1_precolombina.jpg          | item2_precolombina.jpg         | pjm_edad_precolombina.jpg / pjw_edad_precolombina.jpg     |
| 02_epoca_colonial          | fondo_cabildo2d.jpg   | item1_colonial.jpg              | item2_colonial.jpg             | pjm_epoca_colonial.jpg / pjw_epoca_colonial.jpg           |
| 03_epoca_indepencia        | fondo_andes.jpg       | item1_independencia.jpg         | item2_independencia.jpg        | pjm_epoca_independencia.jpg / pjw_epoca_independencia.jpg |
| 04_epoca_organizacion      | fondo_rural.jpg       | item1_organizacion.jpg          | item2_organizacion.jpg         | pjm_epoca_organizacion.jpg / pjw_epoca_organizacion.jfif  |
| 05_epoca_agroexportador    | fondo_puerto.jpg      | item1_agroexportador.jpg        | item2_agroexportador.jpg       | pjm_epoca_agroexportadora.jfif / pjw_epoca_agroexportadora.jfif |
| 06_epoca_industrializacion | fondo_obelisco.jpg    | item1_industrilizacion.jpg *    | item2_industrializacion.jpg    | pjm_epoca_industrializacion.jfif / pjw_epoca_industrializacion.jfif |
| 07_epoca_actual            | fondo_actual.jpg      | item1_actual.jpg                | item2_actual.jpg               | pjm_epoca_actual.jfif / pjw_epoca_actual.jfif             |

`*` item1_industrilizacion.jpg esta asi escrito en Drive (le falta la primera
"a" a "industrializacion"). Es el nombre real del archivo, no un error mio:
si lo renombran en Drive, actualizar tambien GameModules.java.

Ojo: el ideas.txt de 03_epoca_indepencia y de 05_epoca_agroexportador tienen
ademas un archivo suelto "img9.jpg" / "img10.jpg" que no se sabe bien para
que es (no hay fondo/item/personaje con ese nombre): no se usan en el juego,
se pueden ignorar.

## Modulo 3: territorio-argentino (7 mapas, personajes = pjm_X / pjw_X, mayormente .jfif)

| id de mapa                              | fondo               | item1              | item2              | personajes                        |
|--------------------------------------------|-----------------------|-----------------------|-----------------------|---------------------------------------|
| 01_noa_jujuy_salta                          | fondo_noa.jpg         | item1_noa.jpg         | item2_noa.jpg         | pjm_noa.jfif / pjw-noa.jfif *         |
| 02_nea_misiones_corrientes                  | fondo_nea.jpg         | item1_nea.jpg         | item2_nea.jpg         | pjm_nea.jfif / pjw-nea.jfif *         |
| 03_cuyo_mendoza_sanjuan                     | fondo_cuyo.jpg        | item1_cuyo.jpg        | item2_cuyo.jpg        | pjm_cuyo.jfif / pjw_cuyo.jfif         |
| 04_centro_cordoba_sanluis                   | fondo_centro.jpg      | item1_centro.jpg      | item2_centro.jpg      | pjm_centro.jfif / pjw_centro.jfif     |
| 05_pampahumeda_bs_pampa                     | fondo_papahumeda.jpg **| item1_pampahumeda.jpg | item2_pampahumeda.jpg | pjm_pampahumeda.jfif / pjw_pampahumeda.jfif |
| 06_cordillera_rionegro_neuquen              | fondo_cordillera.jpg  | item1_cordillera.jpg  | item2_cordillera.jpg  | pjm_cordillera.jfif / pjw_cordillera.jfif   |
| 07_austral_santacruz_tierradelfuego         | fondo_austral.jpg     | item1_austral.jpg     | item2_austral.jpg     | pjm_austral.jfif / pjw_austral.jfif   |

`*` "pjw-noa.jfif" y "pjw-nea.jfif" llevan GUION, no guion bajo (a
diferencia de todos los demas). Nombre real, no error mio.
`**` "fondo_papahumeda.jpg" esta asi escrito en Drive (le falta la "m" a
"pampahumeda"). Nombre real.

## Modulo 4: barrios-caba (7 mapas, personajes = pjm_X / pjw_X en .jfif; SIN item1/item2 por mapa)

| id de mapa           | fondo                    | personajes                                     |
|------------------------|----------------------------|-----------------------------------------------|
| 01_lomas_de_zamora      | fondo_lomas.jpg            | pjm_lomas_de_zamora.jfif / pjw_lomas_de_zamora.jfif |
| 02_villa_lugano         | fondo_lugano.jpg           | pjm_villa_lugano.jfif / pjw_villa_lugano.jfif       |
| 03_la_boca              | fondo_la_boca.jpg          | pjm_la_boca.jfif / pjw_la_boca.jfif                 |
| 04_palermo              | fondo_palermo.jpg          | pjm_palermo.jfif / pjw_palermo.jfif                 |
| 05_recoleta             | fondo_recoleta.jpg         | pjm_recoleta.jfif / pjw_recoleta.jfif               |
| 06_belgrano *           | fondo_belgrano.jpg         | pjm_belgrano.jfif / pjw_belgrano.jfif               |
| 07_puerto_madero        | fondo_puerto_madero.jpg    | pjm_puerto_madero.jfif / pjw_puerto_madero.jfif     |

Los 7 billetes reales compartidos (van en `barrios-caba/recursos/`, no en
cada carpeta de mapa): diez_pesos.jpg, cincuenta_pesos.jpg, cien_pesos.jpg,
mil_pesos.jpg, dos_mil_pesos.jpg, diez_mil_pesos.jpg, veinte_mil_pesos.jpg.

`*` OJO - inconsistencia real en el Drive, no la resolvimos por ustedes:
la carpeta "06_belgrano" y sus archivos (fondo_belgrano.jpg, pjm_belgrano.jfif)
dicen "Belgrano", pero el texto que escribieron en su ideas.txt describe
Nuñez / el estadio Monumental de River. En GameModules.java dejamos el
TITULO que aparece en el cartel informativo como "Nuñez" (el texto que
redactaron, asumiendo que es el mas reciente/correcto), pero las IMAGENES
que se usan siguen llamandose "belgrano" porque son las que existen. Si en
realidad el mapa es Belgrano (y el texto esta mal), o es Nuñez (y las
imagenes estan mal nombradas), avisen y lo alineamos derecho.

## Informativa opcional (cartel entre mapas)

Si en algun momento quieren una ilustracion para el cartel que aparece al
cambiar de mapa (hoy es solo texto sobre un color solido), el nombre que
espera el codigo es `info.png` dentro de la carpeta de cada mapa, por
ejemplo `client/assets/modulos/historia-mundial/01_edad_piedra/info.png`.
Es opcional: mientras no este, no rompe nada.

## Fuente pixel

`fuentes/PressStart2P.ttf` sigue pendiente (no estaba en la carpeta que
compartieron). Mientras tanto se usa la fuente default de LibGDX. Para
usar la original hace falta generar un .fnt con Hiero (viene con LibGDX)
o sumar la extension FreeTypeFontGenerator al build.gradle.

## Assets viejos (ya no se usan)

`client/assets/personajes/` y `client/assets/fondos/` (los 10 personajes y
10 barrios de CABA de antes del sistema de modulos) siguen en el proyecto
pero ningun codigo los lee mas. Se pueden borrar.

## Importante: working directory

El build.gradle del modulo client configura el task `run` para que arranque
con `client/assets/` como directorio de trabajo, asi que las rutas de
arriba son relativas a esa carpeta. Si corren el cliente desde IntelliJ con
su propia configuracion de Run, revisen que el "Working directory" apunte
a `client/assets`.
