package hilos;

import estructuras.Cola;
import modelo.Enemigo;
import modelo.Nave;

/**
 * Hilo que procesa el combate por turnos contra los enemigos encolados.
 * 
 * INSTRUCCIONES: Implementa el método procesarCombate() marcado con TODO.
 * Este hilo:
 * 1. Espera hasta que 'enCombate' sea true y haya enemigos en la cola
 * 2. Desencola un enemigo
 * 3. Ejecuta turnos alternados (jugador ataca, enemigo ataca) hasta que uno muera
 * 4. Notifica al listener en cada turno
 */
public class HiloCombate extends Thread {

    private Cola<Enemigo> colaEnemigos;
    private Nave nave;
    private volatile boolean activo;
    private volatile boolean enCombate;
    private int recompensaCombate;
    private CombateListener listener;

    public interface CombateListener {
        void onNuevoEnemigo(Enemigo enemigo);
        void onTurnoCombate(String mensaje);
        void onDisparo(boolean esDelJugador);
        void onVidasActualizadas(int vidaJugador, int vidaEnemigo);
        void onCombateTerminado(boolean victoria, int recompensa);
    }

    public HiloCombate(Cola<Enemigo> colaEnemigos, Nave nave) {
        this.colaEnemigos = colaEnemigos;
        this.nave = nave;
        this.activo = true;
        this.enCombate = false;
        this.recompensaCombate = 0;
        setDaemon(true);
        setName("Hilo-Combate");
    }

    public void setListener(CombateListener listener) {
        this.listener = listener;
    }

    public void iniciarCombate() {
        this.enCombate = true;
        this.recompensaCombate = 0; // mejora: acumular recompensa total de la sesión de combate
    }

    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(500);
                if (!activo) break;

                if (enCombate && !colaEnemigos.estaVacia()) {
                    procesarCombate();
                }

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * TODO: Implementar la lógica de combate por turnos.
     * 
     * Pseudocódigo:
     * 1. Si colaEnemigos está vacía, poner enCombate=false y retornar
     * 2. Desencolar un enemigo
     * 3. Notificar: "Combate contra: " + enemigo.toString()
     * 4. Mientras enemigo vivo Y nave viva:
     *    a. Esperar 1 segundo (Thread.sleep(1000))
     *    b. Turno jugador: daño = nave.getAtaque() + random(0-4)
     *       enemigo.recibirDaño(daño)
     *       Notificar el daño hecho
     *    c. Si enemigo murió: sumar recompensa a la nave, notificar, break
     *    d. Esperar 0.8 segundos
     *    e. Turno enemigo: nave.recibirDaño(enemigo.getAtaque())
     *       Notificar el daño recibido
     * 5. Si la nave murió: enCombate=false, listener.onCombateTerminado(false, 0)
     * 6. Si la cola está vacía: enCombate=false, listener.onCombateTerminado(true, recompensa)
     */
    private void procesarCombate() {
        // TODO: Implementar combate por turnos usando la Cola de enemigos
        if (colaEnemigos.estaVacia()) {
            enCombate = false;
            return;
        }

        Enemigo enemigo = colaEnemigos.dequeue();
        if (listener != null) {
            listener.onNuevoEnemigo(enemigo);
            listener.onTurnoCombate("Combate contra: " + enemigo.toString());
        }

        while (enemigo.estaVivo() && nave.estaVivo()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }

            if (listener != null) {
                listener.onDisparo(true);
            }
            int danioJugador = nave.getAtaque() + (int) (Math.random() * 5);
            enemigo.recibirDaño(danioJugador);
            if (listener != null) {
                listener.onTurnoCombate("Atacaste a " + enemigo.getNombre() + " por " + danioJugador + " de daño.");
                listener.onVidasActualizadas(nave.getVida(), enemigo.getVida());
            }

            if (!enemigo.estaVivo()) {
                int recompensa = enemigo.getRecompensa();
                recompensaCombate += recompensa; // mejora: acumular recompensa total de la sesión
                nave.sumarPuntuacion(recompensa);
                if (listener != null) {
                    listener.onTurnoCombate("Enemigo derrotado: " + enemigo.getNombre() + " (+" + recompensa + " pts)");
                    listener.onVidasActualizadas(nave.getVida(), enemigo.getVida());
                }
                if (colaEnemigos.estaVacia()) {
                    enCombate = false;
                    if (listener != null) {
                        listener.onCombateTerminado(true, recompensaCombate);
                    }
                }
                return;
            }

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                return;
            }

            if (listener != null) {
                listener.onDisparo(false);
            }
            int danioEnemigo = enemigo.getAtaque();
            nave.recibirDaño(danioEnemigo);
            if (listener != null) {
                listener.onTurnoCombate("Recibiste " + danioEnemigo + " de daño.");
                listener.onVidasActualizadas(nave.getVida(), enemigo.getVida());
            }
        }

        if (!nave.estaVivo()) {
            enCombate = false;
            if (listener != null) {
                listener.onCombateTerminado(false, 0);
            }
        }
    }

    public boolean isEnCombate() {
        return enCombate;
    }

    public void detener() {
        activo = false;
        interrupt();
    }
}
