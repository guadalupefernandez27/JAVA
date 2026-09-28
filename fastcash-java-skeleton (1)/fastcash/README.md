# FAST CASH - version Java cliente/servidor

Puerto del juego original en Python/Pygame a una arquitectura cliente-servidor
en Java, siguiendo la consigna: servidor con la logica de negocio, cliente
"tonto" que solo dibuja y manda inputs.

Desde el rediseño en modulos: 4 modulos tematicos (Historia Mundial /
Historia Argentina / Territorio Argentino / Barrios de CABA), cada uno con
7 mapas y 2 personajes jugables propios. La partida arranca con: elegir
modulo -> elegir personaje -> jugar, y muestra un cartel informativo cada
vez que el jugador pasa a un mapa nuevo dentro del modulo.

## Estructura

    fastcash/
      core/    -> modelos y protocolo de red compartidos (sin LibGDX, sin logica de juego)
      server/  -> game loop + sockets, corre por consola (sin ventana)
      client/  -> app LibGDX, se conecta al server y dibuja lo que este le manda

## Flujo del juego

1. MenuScreen: pantalla de inicio, ESPACIO para conectar al servidor.
2. ModuleSelectScreen: se elige uno de los 4 modulos con las flechas.
   La eleccion es UNICA para toda la partida: gana el primer jugador que
   confirma con ESPACIO (ver GameEngine.elegirModulo, un compareAndExchange
   que ignora las elecciones posteriores). Si el otro jugador ya eligio
   antes, esta pantalla lo detecta solo (via GameStateSnapshot.moduloId)
   y pasa derecho a la siguiente.
3. CharacterSelectScreen: cada jugador elige su propio personaje (0 o 1)
   dentro del modulo ya confirmado. A diferencia del modulo, esto es por
   jugador y no hace falta esperar al otro: es puramente cosmetico.
4. GameScreen: se juega. El mapa es UNO SOLO para toda la partida
   (GameStateSnapshot.mapaActual): los 2 jugadores comparten pantalla, fondo
   y recursos que caen, asi que el mapa avanza segun el jugador que va
   ganando (ver GameEngine.actualizarMapaActual y GameConfig.calcularMapaActual).
   Cada vez que cambia, aparece el cartel informativo para los dos (ver abajo).
   El genero elegido (hombre/mujer) se mantiene, pero el traje cambia solo
   en cada mapa: cada carpeta de mapa trae su propio archivo de personaje.

## Recursos y economia

- Modulos 1 a 3: 2 recursos por mapa (item1 comun: 100 pts, cae a 8; item2
  raro: 500 pts, cae a 10). Ganar = 7000 pts (1000 por mapa).
- Modulo 4 (barrios-caba): 7 billetes argentinos reales (10 a 20000 pesos),
  compartidos por los 7 mapas. Ganar = 100000 pts.
- Cuanto mas vale un recurso, menos seguido cae (RecursoSpawner: probabilidad
  inversa al valor). Todos los numeros estan en core/GameModules.java y son
  faciles de ajustar si al probar no se siente balanceado.

## El cartel informativo entre mapas

Cuando el `mapaActual` del propio jugador cambia, GameScreen.java muestra un
cartel superpuesto con el titulo y el texto informativo de ese mapa
(GameModules.MODULOS -> Modulo.niveles -> Nivel.titulo / textoInformativo, ya con el texto real de los ideas.txt del Drive).

Decisiones de diseño (ya definidas, no son TODO):
- Aparece solo y se cierra solo a los 4 segundos (DURACION_INFO_SEGUNDOS en
  GameScreen.java), o antes si el jugador aprieta ESPACIO.
- Mientras esta en pantalla, el jugador no se mueve (el cliente manda
  input en falso), PERO el servidor sigue tickeando normalmente para todos.
  Es una decision a proposito: si el cartel frenara tambien al servidor,
  un jugador leyendo el cartel le haria perder tiempo de juego al otro.
  Como contrapartida, mientras el cartel esta en pantalla pueden caer
  billetes que el jugador no ve/atrapa — es el costo de que el otro
  jugador no se vea perjudicado.
- La imagen del cartel (modulos/<id>/info/mapaN.png) es opcional: si no
  esta, se dibuja un color solido de fondo con el texto igual, no rompe nada.

## Contenido de los modulos (texto e imagenes)

Todo el catalogo de modulos/mapas/personajes vive en UN SOLO lugar:
`core/src/main/java/com/fastcash/core/GameModules.java`. Es lo unico que
hay que tocar para cambiar nombres, titulos o el texto informativo de cada
mapa — ningun otro archivo necesita cambios.

El contenido ya esta cargado (28 mapas, transcripto de los ideas.txt del Drive). Se edita en ese mismo archivo.

Las imagenes van en `client/assets/modulos/<id-del-modulo>/`, ver
`client/assets/modulos/README.md` para la convencion exacta de carpetas
y nombres de archivo. Mientras un archivo no exista, el cliente cae en un
color solido de respaldo (mismo mecanismo que ya se usaba antes con los
fondos de CABA) — el juego nunca se rompe por una imagen faltante.

## Que se porteo del Python y donde quedo

| Python original                          | Java, ahora en...                                    |
|-------------------------------------------|------------------------------------------------------|
| clase Billete (obtener_imagen)            | server/BilleteSpawner.java                            |
| clase Personaje (mover, velocidad)        | server/GameEngine.java (moverJugadores)               |
| deteccion de colisiones                   | server/GameEngine.java (detectarColisiones)           |
| energia_actual, contador_billetes         | core/model/PlayerState.java                           |
| nivel_actual = contador_billetes // 1000  | core/GameConfig.calcularMapaActual()                  |
| mostrar_titulo_nivel()                    | client/screens/GameScreen.java (cartel informativo)   |
| pantalla_fin(gano)                        | PlayerState.vivo / PlayerState.gano                   |
| clase Billete (3 billetes fijos)          | core/model/RecursoDef + RecursoCaido, server/RecursoSpawner |
| reloj.tick(60)                            | server/GameServer.java (ScheduledExecutorService)     |
| dibujar_barra_energia, mostrar_mensaje    | client/screens/GameScreen.java (dibujarHud)           |
| (no existia)                              | eleccion de modulo: ModuleSelectScreen + GameEngine.elegirModulo |
| (no existia)                              | eleccion de personaje por modulo: CharacterSelectScreen + GameEngine.elegirPersonaje |

## Como abrir el proyecto

1. Instalar JDK 17 o 21 (NO 27 ni ninguna version muy nueva: todavia no la
   soportan ni Gradle ni algunas dependencias) y tener internet (Gradle va
   a bajar LibGDX y Gson la primera vez que sincronicen).
2. Abrir la carpeta `fastcash/` con IntelliJ IDEA o Android Studio como
   proyecto Gradle existente. Va a detectar los 3 modulos solo.
3. El proyecto ya trae `gradlew`, `gradlew.bat` y
   `gradle/wrapper/gradle-wrapper.properties` (el Gradle Wrapper). Lo unico
   que falta es `gradle/wrapper/gradle-wrapper.jar`: es un archivo binario
   chiquito que no se puede generar a mano, asi que no viene incluido en
   este zip. Casi siempre IntelliJ lo descarga solo al sincronizar (tiene
   internet en tu maquina, a diferencia del entorno donde arme este
   proyecto). Si el sync tira error de todas formas por este motivo: en
   Settings > Build, Execution, Deployment > Build Tools > Gradle, cambiar
   "Distribution" de "Wrapper" a "Local installation" y apuntar a una
   instalacion de Gradle bajada de gradle.org/releases (9.5 o mas nueva).

## Como correrlo (2 ventanas, para probar en una sola maquina)

1. Correr el servidor:
   `./gradlew :server:run`
2. Correr un cliente (en otra terminal):
   `./gradlew :client:run`
3. Correr un segundo cliente para simular al otro jugador:
   `./gradlew :client:run`
4. En cada cliente: SPACE para conectar, flechas para elegir modulo/personaje
   y confirmar con SPACE, despues flechas izq/der para moverse en el juego.

Desde IntelliJ (click derecho > Run sobre Main.java / DesktopLauncher.java)
funciona igual; ver client/assets/README.md por el tema del working directory.

Para probar con dos compus distintas en la facultad: cambiar en
`client/.../screens/MenuScreen.java` el `"localhost"` por la IP de la
maquina que corre el servidor, y asegurarse que el puerto 5555
(GameConfig.PORT) no este bloqueado por el firewall.

## Lo que falta (a proposito, para que lo definan ustedes)

- [ ] Copiar las carpetas de Drive a client/assets/modulos/ (mapeo exacto en
      client/assets/modulos/README.md; el juego corre igual sin ellas, con colores).
- [ ] Aclarar la inconsistencia de 06_belgrano (carpeta/archivos dicen Belgrano,
      el texto describe Nuñez), ver client/assets/modulos/README.md.
- [ ] Imagenes de los 4 modulos: personajes, fondos de mapa, e imagen del
      cartel informativo (esta ultima es opcional). Ver
      client/assets/modulos/README.md para la convencion de nombres.
- [ ] Los assets viejos (client/assets/personajes/ y client/assets/fondos/,
      los 10 personajes y 10 barrios de CABA que habiamos cargado antes del
      rediseño en modulos) quedaron en el proyecto pero YA NO SE USAN: el
      codigo ahora carga todo desde client/assets/modulos/<id>/. Se pueden
      borrar esas dos carpetas, o reusar alguna de esas imagenes copiandolas
      dentro de client/assets/modulos/barrios-caba/ con los nombres nuevos
      si les sirven para ese modulo.
- [ ] Fuente pixel (press-start-2p/PressStart2P.ttf): hoy se usa la fuente
      default de LibGDX. Para usar la original hay que convertirla a .fnt
      con Hiero (viene con LibGDX) o sumar la extension FreeTypeFontGenerator.
- [ ] Pantalla de "gano/perdio" mas prolija en el cliente (hoy es un texto
      simple, el pantalla_fin() del Python tenia fondo + mensaje).
- [ ] Sonido y animaciones (no estaban en el Python original tampoco).
- [ ] Decidir si la partida corta apenas UN jugador gana/pierde o sigue
      hasta que terminen los dos (ver TODO en GameEngine.chequearFinDePartida).

## Nota sobre UDP

El PDF menciona investigar TCP/UDP. Este esqueleto usa TCP (java.net.Socket)
porque es mas simple para arrancar y no se pierden mensajes, ideal para
2 jugadores. Si en algun momento notan lag por la cantidad de mensajes,
ahi vale la pena investigar pasar el canal de STATE (que se manda 60 veces
por segundo) a UDP, dejando el JOIN/INPUT/MODULE_SELECT/CHARACTER_SELECT en TCP.
