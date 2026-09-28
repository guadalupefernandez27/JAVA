# Assets del cliente

## Vigentes (los lee el codigo)
- fondoprincipal.png — fondo de la pantalla de inicio (MenuScreen).
- modulos/<id-modulo>/<id-mapa>/... — fondo, recursos (item1/item2) y
  personajes de cada uno de los 28 mapas. Ver modulos/README.md: tiene el
  mapeo exacto, carpeta por carpeta, con los nombres reales del Drive.
- modulos/barrios-caba/recursos/ — los 7 billetes argentinos reales.

## Ya no se usan (sistema viejo, se pueden borrar)
- billetes/100.png, 500.png, 1000.png (los recursos ahora son por mapa/modulo)
- personajes/personaje1.png ... personaje10.png
- fondos/*.png (los 10 barrios de CABA)

## Todavia falta
- Copiar las carpetas de Drive a modulos/ (ver modulos/README.md). Mientras
  falten imagenes el juego corre igual con colores de respaldo.
- fuentes/PressStart2P.ttf (hoy se usa la fuente default de LibGDX; hay que
  generar un .fnt con Hiero o sumar FreeTypeFontGenerator).

## Importante: working directory
El task `run` de client/build.gradle arranca con esta carpeta (client/assets/)
como directorio de trabajo, por eso el codigo usa rutas relativas, por ejemplo
Gdx.files.internal("modulos/historia-mundial/01_edad_piedra/fondo_piedra.jpg").
Si corren desde IntelliJ con su propia configuracion de Run, el "Working
directory" tiene que apuntar a client/assets.

## Formatos
Varios personajes estan en .jfif (es un JPEG, LibGDX lo lee igual). Los .avif
que hay en Drive NO los soporta LibGDX: por eso el codigo usa siempre las
versiones .jpg de los fondos.
