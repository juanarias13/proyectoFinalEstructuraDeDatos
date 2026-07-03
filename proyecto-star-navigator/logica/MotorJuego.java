package logica;

import estructuras.*;
import hilos.*;
import java.util.*;
import modelo.*;

/**
 * Motor central del juego. Conecta las estructuras de datos con la lógica.
 * 
 * INSTRUCCIONES: Implementa los métodos marcados con TODO.
 * Los métodos de inicialización y getters están completos.
 * 
 * También debes integrar el HiloAnimacion para que la nave se mueva
 * visualmente cuando viaja entre planetas.
 */
public class MotorJuego {

    private Nave nave;
    private Grafo mapaGalactico;
    private Map<String, Planeta> planetas;
    private Cola<Mision> colaMisiones;
    private Cola<Enemigo> colaEnemigos;
    private ArbolBST ranking;

    private HiloEnemigos hiloEnemigos;
    private HiloEventos hiloEventos;
    private HiloCombate hiloCombate;
    private HiloAnimacion hiloAnimacion;

    public MotorJuego(String nombreJugador) {
        nave = new Nave(nombreJugador);
        mapaGalactico = new Grafo();
        planetas = new HashMap<>();
        colaMisiones = new Cola<>();
        colaEnemigos = new Cola<>();
        ranking = new ArbolBST();

        inicializarMapa();
        inicializarMisiones();
        inicializarRankingBase();
    }

    // --- Inicialización (completa) ---

    private void inicializarMapa() {
        crearPlaneta("Terra Nova", "comercial", 150, 200);
        crearPlaneta("Nebula X", "recursos", 300, 100);
        crearPlaneta("Orion VII", "hostil", 450, 200);
        crearPlaneta("Zephyr", "misterioso", 300, 300);
        crearPlaneta("Kronos", "hostil", 550, 350);
        crearPlaneta("Arcadia", "comercial", 100, 380);
        crearPlaneta("Helios", "recursos", 200, 450);
        crearPlaneta("Vortex", "misterioso", 450, 450);
        crearPlaneta("Titan", "hostil", 600, 150);
        crearPlaneta("Elysium", "comercial", 350, 550);

        mapaGalactico.agregarArista("Terra Nova", "Nebula X");
        mapaGalactico.agregarArista("Terra Nova", "Zephyr");
        mapaGalactico.agregarArista("Terra Nova", "Arcadia");
        mapaGalactico.agregarArista("Nebula X", "Orion VII");
        mapaGalactico.agregarArista("Nebula X", "Zephyr");
        mapaGalactico.agregarArista("Orion VII", "Titan");
        mapaGalactico.agregarArista("Orion VII", "Kronos");
        mapaGalactico.agregarArista("Zephyr", "Kronos");
        mapaGalactico.agregarArista("Zephyr", "Helios");
        mapaGalactico.agregarArista("Arcadia", "Helios");
        mapaGalactico.agregarArista("Helios", "Vortex");
        mapaGalactico.agregarArista("Helios", "Elysium");
        mapaGalactico.agregarArista("Kronos", "Vortex");
        mapaGalactico.agregarArista("Vortex", "Elysium");
        mapaGalactico.agregarArista("Titan", "Kronos");

        nave.setPlanetaActual("Terra Nova");
        planetas.get("Terra Nova").setExplorado(true);
    }

    private void crearPlaneta(String nombre, String tipo, int x, int y) {
        Planeta p = new Planeta(nombre, tipo, x, y);
        planetas.put(nombre, p);
        mapaGalactico.agregarNodo(nombre);
    }

    private void inicializarMisiones() {
        // TODO: Encolar 5 misiones usando colaMisiones.enqueue()
        // Misión 1: "Recolectar cristales", planeta objetivo "Nebula X", recompensa 30
        // Misión 2: "Eliminar piratas", planeta objetivo "Orion VII", recompensa 50
        // Misión 3: "Explorar anomalía", planeta objetivo "Zephyr", recompensa 40
        // Misión 4: "Rescate en Kronos", planeta objetivo "Kronos", recompensa 60
        // Misión 5: "Comerciar en Elysium", planeta objetivo "Elysium", recompensa 35

        colaMisiones.enqueue(new Mision("Recolectar cristales", "Recolectar cristales raros.", "Nebula X", 30));
        colaMisiones.enqueue(new Mision("Eliminar piratas", "Eliminar banda pirata.", "Orion VII", 50));
        colaMisiones.enqueue(new Mision("Explorar anomalía", "Investigar anomalía espacial.", "Zephyr", 40));
        colaMisiones.enqueue(new Mision("Rescate en Kronos", "Rescatar colonos.", "Kronos", 60));
        colaMisiones.enqueue(new Mision("Comerciar en Elysium", "Comerciar recursos.", "Elysium", 35));

    }

    private void inicializarRankingBase() {
        // TODO: Insertar puntuaciones base en el ranking (ArbolBST)
        // Usa ranking.insertar(nombre, puntuacion) para:
        // "Capitán Cosmos" - 500 pts
        // "Nova" - 350 pts
        // "Estelar" - 200 pts
        // "Rookie" - 50 pts
        // "Leyenda" - 800 pts

        ranking.insertar("Capitán Cosmos", 500);
        ranking.insertar("Nova", 350);
        ranking.insertar("Estelar", 200);
        ranking.insertar("Rookie", 50);
        ranking.insertar("Leyenda", 800);
    }

    // --- Acciones del jugador ---

    /**
     * Viaja a un planeta destino si hay ruta directa y combustible.
     * Marca el planeta como explorado y verifica misiones.
     * 
     * TODO: Implementar
     * 1. Verificar que existe arista entre planetaActual y destino (grafo.hayArista)
     * 2. Verificar que hay combustible suficiente (nave.getCombustible() >= 10)
     * 3. Llamar nave.viajarA(destino)
     * 4. Marcar el planeta como explorado
     * 5. Verificar si se completa la misión actual (verificarMision)
     * 6. Retornar mensaje apropiado
     */
    public String viajarA(String destino) {
        // TODO: Implementar la lógica de viaje
        String origen = nave.getPlanetaActual();
        if (origen == null) return "Origen desconocido.";
        // 1. Verificar arista
        if (!mapaGalactico.hayArista(origen, destino)) {
            return "No hay ruta directa a " + destino + ".";
        }
        // 2. Verificar combustible
        if (nave.getCombustible() < 10) {
            return "Combustible insuficiente para viajar.";
        }
        // Integrar animación si está disponible
        Planeta pOrigen = planetas.get(origen);
        Planeta pDestino = planetas.get(destino);
        if (hiloAnimacion != null && pOrigen != null && pDestino != null) {
            hiloAnimacion.animarViaje(pOrigen.getX(), pOrigen.getY(), pDestino.getX(), pDestino.getY());
        }
        // 3. Realizar viaje
        boolean ok = nave.viajarA(destino);
        if (!ok) return "Viaje fallido (combustible).";
        // 4. Marcar explorado
        if (pDestino != null) pDestino.setExplorado(true);
        // 5. Verificar misión
        verificarMision(destino);
        // 5b. Solicitar oleada de enemigos tras llegar al nuevo planeta
        if (hiloEnemigos != null) {
            hiloEnemigos.solicitarOleada();
        }
        // 6. Retornar mensaje
        return "Viajaste a " + destino + ".";
    }

    /**
     * Retrocede al planeta anterior usando la pila de historial.
     * TODO: Implementar
     */
    public String retroceder() {
        if (nave.getHistorialNavegacion().estaVacia()) {
            return "No hay historial para retroceder.";
        }
        if (nave.getCombustible() < 5) {
            return "Combustible insuficiente para retroceder.";
        }
        String anterior = nave.retroceder();
        if (anterior == null) return "No se pudo retroceder.";
        Planeta p = planetas.get(anterior);
        if (p != null) p.setExplorado(true);
        if (hiloEnemigos != null) {
            hiloEnemigos.solicitarOleada();
        }
        return "Retrocediste a " + anterior + ".";
    }

    /**
     * Calcula la ruta más corta al destino usando BFS del grafo.
     * TODO: Implementar
     */
    public String calcularRuta(String destino) {
        // TODO: Usar mapaGalactico.caminoMasCorto() y formatear el resultado
        String inicio = nave.getPlanetaActual();
        if (inicio == null) return "Origen desconocido.";
        List<String> ruta = mapaGalactico.caminoMasCorto(inicio, destino);
        if (ruta.isEmpty()) return "No hay ruta a " + destino + ".";
        return String.join(" -> ", ruta);
    }

    /**
     * Explora todos los planetas alcanzables usando DFS del grafo.
     * TODO: Implementar
     */
    public String explorarDesdeActual() {
        // TODO: Usar mapaGalactico.explorarDFS() y formatear el resultado
        String inicio = nave.getPlanetaActual();
        if (inicio == null) return "Origen desconocido.";
        List<String> orden = mapaGalactico.explorarDFS(inicio);
        if (orden.isEmpty()) return "No se encontraron planetas alcanzables.";
        return String.join(", ", orden);
    }

    /**
     * Verifica si al llegar a un planeta se completa la misión actual.
     * La misión actual es la que está al frente de la cola.
     * 
     * TODO: Implementar
     * 1. Si colaMisiones no está vacía
     * 2. Hacer peek() para ver la misión al frente
     * 3. Si el planetaObjetivo de la misión coincide con el planeta actual
     * 4. Hacer dequeue(), marcar completada, sumar recompensa
     */
    private void verificarMision(String planeta) {
        // TODO: Implementar verificación de misiones con la Cola
        if (colaMisiones.estaVacia()) return;
        Mision m = colaMisiones.peek();
        if (m.getPlanetaObjetivo().equals(planeta)) {
            colaMisiones.dequeue();
            m.completar();
            nave.sumarPuntuacion(m.getRecompensa());
        }
    }

    // --- Hilos (completo) ---

    public void iniciarHilos() {
        hiloEnemigos = new HiloEnemigos(colaEnemigos);
        hiloEventos = new HiloEventos();
        hiloCombate = new HiloCombate(colaEnemigos, nave);
        hiloAnimacion = new HiloAnimacion();

        hiloEnemigos.start();
        hiloEventos.start();
        hiloCombate.start();
        hiloAnimacion.start();
    }

    public void detenerHilos() {
        if (hiloEnemigos != null) hiloEnemigos.detener();
        if (hiloEventos != null) hiloEventos.detener();
        if (hiloCombate != null) hiloCombate.detener();
        if (hiloAnimacion != null) hiloAnimacion.detener();
    }

    /**
     * TODO: Integrar la animación al viajar.
     * Cuando el jugador viaja de un planeta a otro, debes:
     * 1. Obtener las coordenadas (x, y) del planeta origen y destino
     * 2. Llamar hiloAnimacion.animarViaje(origenX, origenY, destinoX, destinoY)
     * 3. El HiloAnimacion moverá la nave visualmente entre ambos puntos
     * 
     * Para que funcione, necesitas configurar el listener del hiloAnimacion
     * en la VentanaPrincipal para que actualice el PanelMapa en cada frame.
     */
    public HiloAnimacion getHiloAnimacion() { return hiloAnimacion; }

    public void combatir() {
        if (hiloCombate == null) return;
        if (colaEnemigos.estaVacia()) return;
        hiloCombate.iniciarCombate();
    }

    public void terminarJuego() {
        detenerHilos();
        ranking.insertar(nave.getNombre(), nave.getPuntuacion());
    }

    // --- Getters (completos) ---

    public Nave getNave() { return nave; }
    public Grafo getMapaGalactico() { return mapaGalactico; }
    public Map<String, Planeta> getPlanetas() { return planetas; }
    public Cola<Mision> getColaMisiones() { return colaMisiones; }
    public Cola<Enemigo> getColaEnemigos() { return colaEnemigos; }
    public ArbolBST getRanking() { return ranking; }
    public HiloEnemigos getHiloEnemigos() { return hiloEnemigos; }
    public HiloEventos getHiloEventos() { return hiloEventos; }
    public HiloCombate getHiloCombate() { return hiloCombate; }

    public List<String> getDestinosDisponibles() {
        return mapaGalactico.vecinos(nave.getPlanetaActual());
    }
}
