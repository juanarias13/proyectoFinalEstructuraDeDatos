package hilos;

import estructuras.Cola;
import modelo.Enemigo;

/**
 * Hilo que genera oleadas de enemigos periódicamente.
 * Los enemigos se encolan para ser procesados por el sistema de combate.
 * 
 * INSTRUCCIONES: Implementa el método run() marcado con TODO.
 * Este hilo debe:
 * 1. Esperar 8 segundos (Thread.sleep(8000))
 * 2. Generar entre 1 y 4 enemigos aleatorios
 * 3. Encolarlos en colaEnemigos
 * 4. Notificar al listener
 * 5. Repetir mientras 'activo' sea true
 */
public class HiloEnemigos extends Thread {

    private Cola<Enemigo> colaEnemigos;
    private volatile boolean activo;
    private volatile boolean oleadaPendiente;
    private final Object lock = new Object();
    private int oleada;
    private HiloListener listener;

    public interface HiloListener {
        void onOleadaGenerada(int oleada, int cantidadEnemigos);
    }

    private static final String[] NOMBRES_ENEMIGOS = {
        "Pirata Estelar", "Dron Centinela", "Nave Fantasma",
        "Crucero Imperial", "Cazador Oscuro", "Minero Rebelde",
        "Destructor", "Interceptor", "Acorazado"
    };

    public HiloEnemigos(Cola<Enemigo> colaEnemigos) {
        this.colaEnemigos = colaEnemigos;
        this.activo = true;
        this.oleadaPendiente = false;
        this.oleada = 0;
        setDaemon(true);
        setName("Hilo-Enemigos");
    }

    public void solicitarOleada() {
        synchronized (lock) {
            this.oleadaPendiente = true;
            lock.notifyAll();
        }
    }

    public void setListener(HiloListener listener) {
        this.listener = listener;
    }

    /**
     * TODO: Implementar el ciclo del hilo.
     * 
     * Pseudocódigo:
     * mientras activo:
     *     dormir 8 segundos
     *     si no activo, salir
     *     oleada++
     *     cantidadEnemigos = 1 + (oleada / 2), máximo 4
     *     por cada enemigo:
     *         nombre aleatorio del arreglo NOMBRES_ENEMIGOS
     *         vida = 20 + oleada*5 + random(0-9)
     *         ataque = 5 + oleada*2 + random(0-4)
     *         recompensa = 10 + oleada*5
     *         encolar nuevo Enemigo(nombre, vida, ataque, recompensa)
     *     si listener != null:
     *         listener.onOleadaGenerada(oleada, cantidadEnemigos)
     */
    @Override
    public void run() {
        // TODO: Implementar el ciclo de generación de oleadas
        // Recuerda envolver Thread.sleep en try-catch InterruptedException
        while (activo) {
            synchronized (lock) {
                if (!oleadaPendiente) {
                    try {
                        lock.wait(8000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                oleadaPendiente = false;
                if (!activo) break;
            }

            if (!activo) break;
            if (!colaEnemigos.estaVacia()) {
                continue;
            }
            oleada++;
            int cantidadEnemigos = Math.min(4, 1 + (oleada / 2));
            for (int i = 0; i < cantidadEnemigos; i++) {
                String nombre = NOMBRES_ENEMIGOS[(int) (Math.random() * NOMBRES_ENEMIGOS.length)];
                int vida = 20 + oleada * 5 + (int) (Math.random() * 10);
                int ataque = 5 + oleada * 2 + (int) (Math.random() * 5);
                int recompensa = 10 + oleada * 5;
                colaEnemigos.enqueue(new Enemigo(nombre, vida, ataque, recompensa));
            }
            if (listener != null) {
                listener.onOleadaGenerada(oleada, cantidadEnemigos);
            }
        }
    }

    public void detener() {
        activo = false;
        interrupt();
    }

    public int getOleada() {
        return oleada;
    }
}
