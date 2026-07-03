package hilos;

import modelo.ObjetoEspacial;

/**
 * Hilo que genera eventos aleatorios en el espacio.
 * Eventos posibles: asteroides (daño), cofres (objetos), señales (puntos), nebulosas (combustible).
 * 
 * INSTRUCCIONES: Implementa el método run() marcado con TODO.
 * Este hilo debe:
 * 1. Esperar entre 5 y 12 segundos (aleatorio)
 * 2. Generar un evento aleatorio
 * 3. Notificar al listener con el tipo, descripción y dato
 * 4. Repetir mientras 'activo' sea true
 */
public class HiloEventos extends Thread {

    private volatile boolean activo;
    private EventoListener listener;

    public interface EventoListener {
        void onEvento(String tipo, String descripcion, Object dato);
    }

    public HiloEventos() {
        this.activo = true;
        setDaemon(true);
        setName("Hilo-Eventos");
    }

    public void setListener(EventoListener listener) {
        this.listener = listener;
    }

    /**
     * TODO: Implementar el ciclo del hilo.
     * 
     * Pseudocódigo:
     * mientras activo:
     *     espera = 5000 + random(0-6999) milisegundos
     *     dormir(espera)
     *     si no activo, salir
     *     generarEventoAleatorio()
     * 
     * generarEventoAleatorio():
     *     random = Math.random()
     *     si random < 0.3 → evento "asteroide" con daño 5-19
     *         listener.onEvento("asteroide", "¡Lluvia de asteroides! Daño: X", daño)
     *     si random < 0.6 → evento "cofre" con objeto aleatorio
     *         listener.onEvento("cofre", "Cofre flotante: nombre", objeto)
     *     si random < 0.8 → evento "señal" con 25 puntos
     *         listener.onEvento("señal", "Señal de auxilio. +25 pts", 25)
     *     sino → evento "nebulosa" con combustible 10-19
     *         listener.onEvento("nebulosa", "Nebulosa energética. +X", combustible)
     */
    @Override
    public void run() {
        // TODO: Implementar el ciclo de generación de eventos
        while (activo) {
            int espera = 5000 + (int) (Math.random() * 7000);
            try {
                Thread.sleep(espera);
            } catch (InterruptedException e) {
                break;
            }
            if (!activo) break;
            double valor = Math.random();
            if (listener == null) continue;
            if (valor < 0.3) {
                int danio = 5 + (int) (Math.random() * 15);
                listener.onEvento("asteroide", "¡Lluvia de asteroides! Daño: " + danio, danio);
            } else if (valor < 0.6) {
                String[] nombres = {"Cristal Cósmico", "Energia Pura", "Artefacto Alien", "Núcleo Lumínico"};
                String[] tipos = {"combustible", "arma", "escudo", "material"};
                String nombre = nombres[(int) (Math.random() * nombres.length)];
                String tipo = tipos[(int) (Math.random() * tipos.length)];
                int valorObjeto = 10 + (int) (Math.random() * 11);
                ObjetoEspacial objeto = new ObjetoEspacial(nombre, tipo, valorObjeto);
                listener.onEvento("cofre", "Cofre flotante: " + nombre, objeto);
            } else if (valor < 0.8) {
                listener.onEvento("señal", "Señal de auxilio. +25 pts", 25);
            } else {
                int combustible = 10 + (int) (Math.random() * 10);
                listener.onEvento("nebulosa", "Nebulosa energética. +" + combustible, combustible);
            }
        }
    }

    public void detener() {
        activo = false;
        interrupt();
    }
}
