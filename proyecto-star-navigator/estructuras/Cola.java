package estructuras;

/**
 * Cola genérica implementada con lista enlazada.
 * Usada para misiones y oleadas de enemigos.
 * 
 * INSTRUCCIONES: Implementa todos los métodos marcados con TODO.
 * Recuerda: FIFO (First In, First Out).
 * IMPORTANTE: Los métodos deben ser synchronized porque los hilos acceden a la cola.
 */
public class Cola<T> {

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamaño;

    public Cola() {
        frente = null;
        fin = null;
        tamaño = 0;
    }

    /**
     * Agrega un elemento al final de la cola.
     * TODO: Implementar (debe ser synchronized)
     */
    public synchronized void enqueue(T valor) {
        // TODO: Crear nuevo nodo, enlazarlo al final.
        // Si la cola está vacía, frente y fin apuntan al nuevo nodo.
        // Si no, enlazar fin.siguiente al nuevo nodo y actualizar fin.
        
        Nodo<T> nuevoNodo = new Nodo<>(valor);
        if(estaVacia()){
            frente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            fin.siguiente = nuevoNodo;
            fin = nuevoNodo;
        }
        tamaño++;
        

    }

    /**
     * Quita y retorna el elemento del frente.
     * @throws RuntimeException si la cola está vacía
     * TODO: Implementar (debe ser synchronized)
     */
    public synchronized T dequeue() {
        // TODO: Guardar el valor del frente, mover frente al siguiente.
        // Si frente queda null, fin también debe ser null.
        // Lanzar RuntimeException si está vacía.


        if(estaVacia()){
            throw new RuntimeException("Cola vacía");
        }
        T valor = frente.valor;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        tamaño--;
        return valor;
    }

    /**
     * Retorna el elemento del frente sin quitarlo.
     * @throws RuntimeException si la cola está vacía
     * TODO: Implementar (debe ser synchronized)
     */
    public synchronized T peek() {
        // TODO: Retornar el valor del frente sin modificar la cola.
        if(estaVacia()){
            throw new RuntimeException("Cola vacía");
        }
        return frente.valor;
    }

    public synchronized boolean estaVacia() {
        return frente == null;
    }

    public synchronized int tamaño() {
        return tamaño;
    }
}
