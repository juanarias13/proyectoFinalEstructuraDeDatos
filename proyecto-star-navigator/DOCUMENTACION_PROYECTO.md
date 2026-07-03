# Documentacion del Proyecto Star Navigator

Este archivo resume la organizacion del proyecto, los archivos de cada carpeta y la logica principal de cada clase y funcion.

## Vista General por Carpetas

### `app/`
- `Main.java`: punto de entrada del programa. Configura Swing y abre la ventana principal.

### `modelo/`
- `Nave.java`: estado del jugador: vida, ataque, escudo, combustible, puntuacion, inventario e historial.
- `Planeta.java`: datos de cada planeta del mapa.
- `Mision.java`: datos de cada mision en cola.
- `Enemigo.java`: datos y comportamiento basico de enemigos.
- `ObjetoEspacial.java`: objetos recolectables del inventario.

### `estructuras/`
- `Nodo.java`: nodo generico reutilizado por estructuras enlazadas.
- `ListaEnlazada.java`: inventario de la nave.
- `Pila.java`: historial de navegacion.
- `Cola.java`: cola de misiones y cola de enemigos.
- `ArbolBST.java`: ranking de puntuaciones.
- `Grafo.java`: mapa galactico y algoritmos BFS/DFS.

### `hilos/`
- `HiloEnemigos.java`: genera oleadas de enemigos.
- `HiloEventos.java`: genera eventos aleatorios.
- `HiloCombate.java`: procesa combates por turnos.
- `HiloAnimacion.java`: anima el movimiento de la nave.

### `gui/`
- `VentanaPrincipal.java`: ventana principal y conexion entre botones, motor, paneles e hilos.
- `PanelMapa.java`: dibujo del mapa galactico.
- `PanelInfo.java`: panel lateral de datos de la nave.
- `PanelCombate.java`: panel visual del combate.

### Archivos auxiliares
- `sources.txt`: lista de archivos Java usada para compilar con `javac @sources.txt`.
- `out/`: carpeta generada por compilacion, contiene clases `.class`.

---

## `app/Main.java`

### Clase `Main`
Es la clase inicial del programa.

### `main(String[] args)`
Configura el estilo visual de Swing con el aspecto del sistema operativo. Luego usa `SwingUtilities.invokeLater` para crear `VentanaPrincipal` dentro del hilo correcto de Swing, conocido como EDT. Esto evita problemas de interfaz grafica causados por modificar componentes desde hilos incorrectos.

---

## `modelo/Nave.java`

### Clase `Nave`
Representa la nave del jugador. Guarda nombre, vida, ataque, escudo, combustible, puntuacion, planeta actual, inventario y pila de historial.

### `Nave(String nombre)`
Inicializa la nave con valores base: 100 de vida, 20 de ataque, 5 de escudo, 50 de combustible, 0 puntos, inventario vacio y pila de historial vacia.

### `viajarA(String planeta)`
Sirve para mover la nave a otro planeta. Primero verifica si hay al menos 10 de combustible. Si la nave ya estaba en un planeta, guarda ese planeta en la pila `historialNavegacion`. Despues actualiza `planetaActual` y resta 10 de combustible.

Logica usada: pila LIFO para poder volver al planeta anterior.

### `retroceder()`
Sirve para volver al ultimo planeta visitado. Verifica que la pila no este vacia y que haya al menos 5 de combustible. Luego hace `pop` del historial, actualiza el planeta actual y resta combustible.

Logica usada: el ultimo planeta guardado es el primero al que se regresa.

### `agregarObjeto(ObjetoEspacial objeto)`
Agrega un objeto al inventario si todavia hay espacio. El limite es `MAX_INVENTARIO = 10`. Si hay espacio, llama `inventario.agregar(objeto)`.

### `usarObjeto(ObjetoEspacial objeto)`
Busca un objeto en el inventario. Si existe, aplica su efecto segun el tipo:
- `combustible`: suma combustible.
- `arma`: aumenta ataque.
- `escudo`: aumenta escudo.
- `material`: suma puntuacion.

Despues elimina el objeto de la lista enlazada.

### `recibirDaño(int daño)`
Reduce la vida de la nave. Primero calcula el daño real restando el escudo, pero siempre deja al menos 1 de daño. Si la vida baja de 0, la ajusta a 0.

### `curar(int cantidad)`
Suma vida sin pasar de `vidaMaxima`.

### `estaVivo()`
Retorna `true` si la vida es mayor que 0.

### Getters y setters
Los metodos `getNombre`, `getVida`, `getAtaque`, `getCombustible`, `getPuntuacion`, `getPlanetaActual`, etc. permiten consultar el estado de la nave desde la GUI y el motor. `setPlanetaActual`, `sumarPuntuacion` y `agregarCombustible` modifican valores puntuales.

---

## `modelo/Planeta.java`

### Clase `Planeta`
Representa un nodo del grafo galactico. Tiene nombre, tipo, coordenadas para dibujar y estado de exploracion.

### `Planeta(String nombre, String tipo, int x, int y)`
Crea un planeta con sus datos basicos y lo marca inicialmente como no explorado.

### Getters
`getNombre`, `getTipo`, `getX`, `getY` e `isExplorado` devuelven datos usados por el mapa y la logica.

### `setExplorado(boolean explorado)`
Marca si el planeta ya fue descubierto por el jugador.

### `toString()`
Devuelve un texto con el nombre y tipo del planeta.

---

## `modelo/Mision.java`

### Clase `Mision`
Representa una mision que se guarda en la cola FIFO.

### `Mision(String nombre, String descripcion, String planetaObjetivo, int recompensa)`
Guarda los datos de la mision y la marca como no completada.

### Getters
Permiten obtener nombre, descripcion, planeta objetivo, recompensa y estado.

### `completar()`
Cambia `completada` a `true`.

### `toString()`
Devuelve un resumen para mostrar en el panel lateral.

---

## `modelo/Enemigo.java`

### Clase `Enemigo`
Representa un enemigo en la cola de combate.

### `Enemigo(String nombre, int vida, int ataque, int recompensa)`
Inicializa un enemigo con vida, ataque y recompensa.

### Getters
Devuelven nombre, vida, ataque y recompensa.

### `recibirDaño(int daño)`
Resta vida al enemigo. Si baja de 0, la deja en 0.

### `estaVivo()`
Retorna `true` si la vida es mayor que 0.

### `toString()`
Devuelve un texto con nombre, HP y ataque.

---

## `modelo/ObjetoEspacial.java`

### Clase `ObjetoEspacial`
Representa un item recolectable.

### `ObjetoEspacial(String nombre, String tipo, int valor)`
Crea un objeto con nombre, tipo y valor.

### Getters
Devuelven nombre, tipo y valor.

### `toString()`
Devuelve el objeto en formato legible para inventario.

### `equals(Object obj)`
Compara objetos por nombre. Sirve para que la lista enlazada pueda buscar y eliminar objetos equivalentes.

### `hashCode()`
Devuelve un hash basado en el nombre. Acompana a `equals` para mantener consistencia.

---

## `estructuras/Nodo.java`

### Clase `Nodo<T>`
Nodo generico usado por lista, pila y cola.

### `Nodo(T valor)`
Guarda el valor y deja `siguiente` en `null`.

---

## `estructuras/ListaEnlazada.java`

### Clase `ListaEnlazada<T>`
Estructura lineal donde cada nodo apunta al siguiente. Se usa como inventario.

### `ListaEnlazada()`
Inicializa la cabeza en `null` y el tamaño en 0.

### `agregar(T valor)`
Crea un nodo nuevo. Si la lista esta vacia, ese nodo queda como cabeza. Si no, recorre hasta el ultimo nodo y enlaza el nuevo al final. Incrementa el tamaño.

### `eliminar(T valor)`
Busca la primera ocurrencia. Si esta en la cabeza, mueve la cabeza al siguiente nodo. Si esta en otra posicion, enlaza el nodo anterior con el siguiente del eliminado. Reduce el tamaño si elimina.

### `obtener(int indice)`
Valida que el indice este dentro del rango. Recorre nodo por nodo hasta llegar a esa posicion y devuelve el valor.

### `contiene(T valor)`
Recorre la lista comparando con `equals`. Retorna `true` si encuentra el valor.

### `tamaño()`
Retorna la cantidad de elementos.

### `estaVacia()`
Retorna `true` si el tamaño es 0.

### `toArray()`
Convierte el contenido de la lista en un arreglo de `String`, usando `toString()` de cada objeto. La GUI lo usa para mostrar inventario.

---

## `estructuras/Pila.java`

### Clase `Pila<T>`
Estructura LIFO. El ultimo elemento agregado es el primero en salir. Se usa para retroceder en navegacion.

### `Pila()`
Inicializa el tope en `null` y el tamaño en 0.

### `push(T valor)`
Crea un nodo, lo enlaza al tope anterior y luego actualiza el tope. Incrementa el tamaño.

### `pop()`
Verifica que no este vacia. Toma el valor del tope, mueve el tope al siguiente nodo, reduce el tamaño y retorna el valor.

### `peek()`
Retorna el valor del tope sin sacarlo.

### `estaVacia()`
Retorna `true` si el tope es `null`.

### `tamaño()`
Retorna la cantidad de elementos.

---

## `estructuras/Cola.java`

### Clase `Cola<T>`
Estructura FIFO. El primer elemento en entrar es el primero en salir. Se usa para misiones y enemigos. Sus metodos son `synchronized` porque varios hilos pueden acceder a la cola.

### `Cola()`
Inicializa frente, fin y tamaño.

### `enqueue(T valor)`
Agrega al final. Si la cola esta vacia, frente y fin apuntan al nuevo nodo. Si no, se enlaza el nodo despues de `fin` y se actualiza `fin`.

### `dequeue()`
Quita el elemento del frente. Si al quitarlo la cola queda vacia, tambien pone `fin` en `null`. Lanza error si no hay elementos.

### `peek()`
Devuelve el elemento del frente sin quitarlo.

### `estaVacia()`
Retorna `true` si `frente` es `null`.

### `tamaño()`
Retorna la cantidad de elementos en cola.

---

## `estructuras/ArbolBST.java`

### Clase `ArbolBST`
Arbol binario de busqueda para ranking. Ordena por puntuacion.

### Clase interna `NodoArbol`
Guarda nombre, puntuacion, hijo izquierdo e hijo derecho.

### `ArbolBST()`
Inicializa la raiz como `null`.

### `insertar(String nombre, int puntuacion)`
Inserta un jugador llamando al metodo recursivo. Las puntuaciones menores van a la izquierda y las mayores o iguales van a la derecha, permitiendo empates.

### `insertarRecursivo(NodoArbol nodo, String nombre, int puntuacion)`
Si el nodo actual es `null`, crea uno nuevo. Si la puntuacion es menor, continua por la izquierda. Si es mayor o igual, continua por la derecha.

### `buscar(int puntuacion)`
Busca si una puntuacion existe en el arbol.

### `buscarRecursivo(NodoArbol nodo, int puntuacion)`
Compara contra el nodo actual. Si coincide retorna `true`; si es menor busca en izquierda; si es mayor busca en derecha.

### `obtenerRanking()`
Crea un texto con el ranking de mayor a menor.

### `inorden(NodoArbol nodo, StringBuilder sb)`
Hace recorrido inorden invertido: derecha, raiz, izquierda. Asi muestra primero las puntuaciones mas altas.

### `altura()`
Calcula la altura del arbol.

### `alturaRecursiva(NodoArbol nodo)`
Si el nodo es `null`, retorna -1. Si no, calcula altura izquierda y derecha, y retorna 1 mas la mayor.

### `estaVacio()`
Retorna `true` si no hay raiz.

---

## `estructuras/Grafo.java`

### Clase `Grafo`
Representa el mapa galactico con lista de adyacencia. Cada planeta es un nodo y cada ruta es una arista.

### `Grafo()`
Inicializa el `HashMap` de adyacencia.

### `agregarNodo(String nodo)`
Agrega un nodo si todavia no existe.

### `agregarArista(String origen, String destino)`
Crea una conexion bidireccional entre dos nodos. Primero asegura que ambos existan, luego agrega cada uno como vecino del otro si no estaba ya.

### `vecinos(String nodo)`
Devuelve la lista de vecinos de un nodo. Si no existe, devuelve una lista vacia.

### `obtenerNodos()`
Devuelve todos los nombres de nodos.

### `hayArista(String origen, String destino)`
Verifica si dos planetas tienen ruta directa.

### `cantidadNodos()`
Devuelve el numero de planetas del grafo.

### `caminoMasCorto(String inicio, String destino)`
Usa BFS para encontrar la ruta mas corta. Mantiene una cola de nodos por visitar, un conjunto de visitados y un mapa de padres. Cuando llega al destino, reconstruye el camino caminando desde el destino hacia atras usando el mapa de padres.

### `explorarDFS(String inicio)`
Usa DFS recursivo para listar todos los planetas alcanzables desde un punto.

### `dfsRecursivo(String nodo, Set<String> visitados, List<String> orden)`
Marca el nodo como visitado, lo agrega al orden y llama recursivamente a cada vecino no visitado.

---

## `logica/MotorJuego.java`

### Clase `MotorJuego`
Es el centro de la logica. Conecta nave, grafo, planetas, misiones, enemigos, ranking e hilos.

### `MotorJuego(String nombreJugador)`
Crea todas las estructuras del juego. Inicializa mapa, misiones y ranking base.

### `inicializarMapa()`
Crea los planetas, agrega las rutas del grafo, pone la nave en Terra Nova y marca ese planeta como explorado.

### `crearPlaneta(String nombre, String tipo, int x, int y)`
Crea un objeto `Planeta`, lo guarda en el mapa de planetas y agrega su nombre como nodo del grafo.

### `inicializarMisiones()`
Encola cinco misiones en orden FIFO.

### `inicializarRankingBase()`
Inserta puntuaciones iniciales en el BST.

### `viajarA(String destino)`
Valida que haya planeta actual, ruta directa y combustible. Si hay animacion disponible, inicia el viaje visual. Despues llama a `nave.viajarA`, marca el destino como explorado, verifica misiones y solicita oleada de enemigos.

### `retroceder()`
Valida que haya historial y combustible. Luego llama a `nave.retroceder`, marca el planeta como explorado y solicita oleada.

### `calcularRuta(String destino)`
Usa `mapaGalactico.caminoMasCorto` para obtener la ruta BFS desde el planeta actual hasta el destino. Devuelve el camino como texto.

### `explorarDesdeActual()`
Usa DFS para obtener todos los planetas alcanzables desde el planeta actual.

### `verificarMision(String planeta)`
Mira la mision al frente de la cola. Si el planeta objetivo coincide con el planeta actual, la desencola, la marca como completada y suma la recompensa.

### `iniciarHilos()`
Crea y arranca los hilos de enemigos, eventos, combate y animacion.

### `detenerHilos()`
Llama `detener()` en cada hilo si existe.

### `getHiloAnimacion()`
Devuelve el hilo de animacion para conectar su listener con la GUI.

### `combatir()`
Verifica que exista hilo de combate y que haya enemigos en cola. Si se cumple, inicia el combate.

### `terminarJuego()`
Detiene hilos e inserta la puntuacion final de la nave en el ranking.

### Getters
Permiten a la GUI consultar nave, mapa, planetas, colas, ranking e hilos.

### `getDestinosDisponibles()`
Devuelve los vecinos del planeta actual. Se usa para llenar el combo de destinos.

---

## `hilos/HiloEnemigos.java`

### Clase `HiloEnemigos`
Hilo encargado de generar oleadas de enemigos y encolarlas.

### Interface `HiloListener`
Define `onOleadaGenerada`, que la GUI usa para recibir aviso de nuevas oleadas.

### `HiloEnemigos(Cola<Enemigo> colaEnemigos)`
Recibe la cola compartida de enemigos e inicializa el hilo como daemon.

### `solicitarOleada()`
Marca que hay una oleada pendiente y despierta el hilo con `notifyAll`.

### `setListener(HiloListener listener)`
Guarda el listener que recibira eventos de oleada.

### `run()`
Mantiene el hilo vivo mientras `activo` sea true. Espera hasta 8 segundos o hasta que se solicite una oleada. Si la cola esta vacia, genera enemigos aleatorios, los encola y notifica a la GUI.

### `detener()`
Cambia `activo` a false e interrumpe el hilo.

### `getOleada()`
Devuelve el numero de oleada actual.

---

## `hilos/HiloEventos.java`

### Clase `HiloEventos`
Genera eventos aleatorios durante el juego.

### Interface `EventoListener`
Define `onEvento`, que envia tipo, descripcion y dato del evento.

### `HiloEventos()`
Inicializa el hilo como activo y daemon.

### `setListener(EventoListener listener)`
Guarda el listener para avisar a la GUI.

### `run()`
Mientras el hilo este activo, espera entre 5 y 12 segundos. Luego genera un evento aleatorio:
- asteroide: causa daño.
- cofre: crea un `ObjetoEspacial`.
- señal: suma puntos.
- nebulosa: suma combustible.

### `detener()`
Detiene el ciclo e interrumpe el hilo.

---

## `hilos/HiloCombate.java`

### Clase `HiloCombate`
Procesa combates por turnos en segundo plano.

### Interface `CombateListener`
Permite comunicar a la GUI: nuevo enemigo, mensajes de turno, disparos, vidas actualizadas y final del combate.

### `HiloCombate(Cola<Enemigo> colaEnemigos, Nave nave)`
Recibe la cola de enemigos y la nave del jugador.

### `setListener(CombateListener listener)`
Guarda el listener de combate.

### `iniciarCombate()`
Activa la bandera `enCombate` y reinicia la recompensa acumulada.

### `run()`
Mientras el hilo este activo, revisa cada 500 ms si hay combate activo y enemigos disponibles. Si los hay, llama `procesarCombate`.

### `procesarCombate()`
Desencola un enemigo y ejecuta turnos:
1. Jugador dispara y hace daño aleatorio basado en su ataque.
2. Si el enemigo muere, suma recompensa y termina o pasa al siguiente.
3. Si sigue vivo, enemigo ataca a la nave.
4. Si la nave muere, termina el juego con derrota.

Durante el proceso notifica a la GUI para log, proyectiles y barras de vida.

### `isEnCombate()`
Indica si hay combate activo.

### `detener()`
Detiene el hilo.

---

## `hilos/HiloAnimacion.java`

### Clase `HiloAnimacion`
Anima el viaje visual de la nave entre coordenadas.

### Interface `AnimacionListener`
Define eventos de frame y fin de animacion.

### `HiloAnimacion()`
Inicializa el hilo activo, sin animacion, como daemon.

### `setListener(AnimacionListener listener)`
Guarda el listener usado por `PanelMapa`.

### `animarViaje(double fromX, double fromY, double toX, double toY)`
Guarda origen, destino y posicion inicial. Activa `animando`.

### `run()`
Mientras el hilo este activo, si `animando` es true interpola la posicion durante 30 frames. En cada frame llama al listener para repintar la nave. Al final notifica que la animacion termino.

### `isAnimando()`
Retorna si la nave se esta animando.

### `getNaveX()` y `getNaveY()`
Devuelven la posicion visual actual.

### `setPosicion(double x, double y)`
Actualiza manualmente la posicion visual.

### `detener()`
Detiene el hilo.

---

## `gui/VentanaPrincipal.java`

### Clase `VentanaPrincipal`
JFrame principal del juego. Crea la interfaz y conecta acciones del usuario con el motor.

### `VentanaPrincipal()`
Pide el nombre de la nave, crea `MotorJuego`, inicializa la UI, arranca hilos, registra listeners, inicia el timer y actualiza pantalla.

### `initUI()`
Configura la ventana, crea `PanelMapa`, `PanelInfo`, `PanelCombate` y panel de controles.

### `crearPanelControles()`
Crea combo de destinos, botones y area de log.

### `crearBoton(String texto, Color color)`
Crea botones con estilo comun.

### `iniciarListeners()`
Conecta botones con acciones. Tambien conecta los listeners de hilos:
- enemigos: escribe oleadas en log.
- eventos: procesa cofres, asteroides, señales y nebulosas.
- combate: actualiza panel de combate.
- animacion: actualiza posicion de nave en mapa.

### `procesarEvento(String tipo, String descripcion, Object dato)`
Aplica el efecto de un evento:
- asteroide: daño a la nave.
- cofre: agrega objeto.
- señal: suma puntos.
- nebulosa: suma combustible.

### `accionViajar()`
Toma el destino seleccionado, llama `motor.viajarA`, escribe resultado en log y actualiza UI.

### `accionRetroceder()`
Llama `motor.retroceder`, escribe resultado y actualiza UI.

### `accionCombatir()`
Verifica si hay enemigos. Si los hay, inicia el panel de combate y llama `motor.combatir`.

### `accionBFS()`
Calcula ruta corta al destino seleccionado y la muestra en log.

### `accionDFS()`
Explora planetas alcanzables, los marca como explorados y repinta el mapa.

### `accionRanking()`
Obtiene el ranking del BST y lo muestra en un cuadro.

### `accionTerminar()`
Termina el juego, detiene timer, muestra puntuacion final y ranking, y desactiva botones principales.

### `actualizarUI()`
Actualiza combo de destinos, panel de informacion y mapa. Conserva el destino seleccionado si sigue disponible.

### `iniciarTimerActualizacion()`
Crea un `Timer` de Swing que actualiza la UI cada 2 segundos.

### `log(String mensaje)`
Agrega mensajes al area de log y mueve el cursor al final.

---

## `gui/PanelMapa.java`

### Clase `PanelMapa`
Dibuja el grafo galactico, planetas y nave.

### `PanelMapa(Grafo grafo, Map<String, Planeta> planetas)`
Recibe el grafo y los planetas que va a dibujar.

### `setPlanetaActual(String planeta)`
Actualiza el planeta actual y repinta.

### `setPosicionNave(double x, double y)`
Actualiza la posicion visual de la nave durante animacion.

### `ocultarNaveAnimada()`
Desactiva el dibujo de la nave animada.

### `paintComponent(Graphics g)`
Metodo principal de dibujo. Limpia el panel y llama a estrellas, rutas, planetas y nave.

### `dibujarEstrellas(Graphics2D g2d)`
Dibuja estrellas de fondo usando posiciones pseudoaleatorias.

### `dibujarRutas(Graphics2D g2d)`
Dibuja lineas entre planetas conectados. Usa escalado para que el mapa no se recorte.

### `dibujarPlanetas(Graphics2D g2d)`
Dibuja planetas con color segun tipo y muestra nombre si estan explorados.

### `dibujarNave(Graphics2D g2d)`
Dibuja la nave animada si esta viajando o estacionada sobre el planeta actual.

### `escalarX(double x)` y `escalarY(double y)`
Convierten coordenadas logicas del mapa a coordenadas reales del panel.

### `obtenerEscala()`
Calcula la escala necesaria para que el mapa completo quepa dentro del panel.

---

## `gui/PanelInfo.java`

### Clase `PanelInfo`
Panel lateral con estado de nave, inventario, mision y enemigos.

### `PanelInfo()`
Configura el layout, colores, tamaño y componentes.

### `initComponents()`
Crea etiquetas, barra de vida, inventario y seccion de mision.

### `crearLabel(String texto, int size, Color color)`
Crea una etiqueta con estilo reutilizable.

### `actualizar(Nave nave, Cola<Mision> misiones, int enemigosEnCola)`
Refresca todos los datos visibles. Lee vida, combustible, ataque, escudo, puntuacion, planeta actual, inventario, mision actual y enemigos en cola.

---

## `gui/PanelCombate.java`

### Clase `PanelCombate`
Dibuja la batalla entre nave y enemigo.

### `PanelCombate()`
Configura fondo y tamaño preferido.

### `iniciarCombate(Nave nave, Enemigo enemigo)`
Carga vida del jugador, vida del enemigo y nombre del enemigo.

### `actualizarVidas(int vidaJugador, int vidaEnemigo)`
Actualiza barras de vida y repinta.

### `animarDisparo(boolean esDelJugador)`
Activa un proyectil. Si dispara el jugador, va de izquierda a derecha. Si dispara el enemigo, va de derecha a izquierda. Usa un hilo corto que mueve el proyectil y repinta.

### `paintComponent(Graphics g)`
Dibuja fondo, nave del jugador, enemigo, barras de vida, texto VS y proyectil si esta activo.

### `dibujarNaveJugador(Graphics2D g2d, int cx, int cy)`
Dibuja la nave del jugador como un triangulo azul con motor.

### `dibujarNaveEnemigo(Graphics2D g2d, int cx, int cy)`
Dibuja al enemigo como un triangulo rojo.

### `dibujarBarraVida(...)`
Dibuja una barra de vida con texto.

### `setProyectilPos(double x, double y)`
Actualiza la posicion del proyectil.

### `ocultarProyectil()`
Oculta el proyectil y repinta.

---

## Flujo Principal del Juego

1. `Main` abre `VentanaPrincipal`.
2. `VentanaPrincipal` crea `MotorJuego`.
3. `MotorJuego` crea mapa, nave, misiones, ranking y hilos.
4. La GUI muestra mapa, informacion, combate, botones y log.
5. Al viajar, se valida ruta y combustible, se anima la nave y se revisa si se completa una mision.
6. Los hilos generan eventos, enemigos y combates en segundo plano.
7. Al combatir, `HiloCombate` consume enemigos de la cola y actualiza la GUI.
8. Al terminar, la puntuacion entra al ranking BST.

---

## Estructuras de Datos Usadas

- Lista enlazada: inventario de objetos.
- Pila: historial de planetas para retroceder.
- Cola: misiones y enemigos.
- Arbol BST: ranking ordenado por puntuacion.
- Grafo: mapa galactico, rutas directas, BFS y DFS.

