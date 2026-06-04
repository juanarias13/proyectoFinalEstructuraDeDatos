package estructuras;

/**
 * Lista enlazada genérica para el inventario de la nave.
 * 
 * INSTRUCCIONES: Implementa todos los métodos marcados con TODO.
 * Usa la clase Nodo<T> como base.
 */
public class ListaEnlazada<T> {

    private Nodo<T> cabeza;
    private int tamaño;

    public ListaEnlazada() {
        cabeza = null;
        tamaño = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     * TODO: Implementar
     */
    public void agregar(T valor) {
        // TODO: Crear un nuevo nodo y agregarlo al final de la lista
        // Si la lista está vacía, el nuevo nodo es la cabeza.
        // Si no, recorrer hasta el último nodo y enlazar el nuevo.

        Nodo<T> nuevoNodo = new Nodo<>(valor);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamaño++;
    }

    /**
     * Elimina la primera ocurrencia del valor en la lista.
     * @return true si se eliminó, false si no se encontró
     * TODO: Implementar
     */
    public boolean eliminar(T valor) {
        // TODO: Buscar el nodo con el valor dado y eliminarlo.
        // Caso especial: si es la cabeza.
        // Caso general: buscar el nodo anterior al que se quiere eliminar.
        if (cabeza == null) {
            return false;
        }
        if (cabeza.valor.equals(valor)) {
            cabeza = cabeza.siguiente;
            tamaño--;
            return true;
        }
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.valor.equals(valor)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Obtiene el elemento en la posición indicada.
     * @return el valor o null si el índice es inválido
     * TODO: Implementar
     */
    public T obtener(int indice) {
        // TODO: Recorrer la lista hasta la posición 'indice' y retornar el valor.

        if (indice < 0 || indice >= tamaño) {
            return null;
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    /**
     * Verifica si un valor existe en la lista.
     * TODO: Implementar
     */
    public boolean contiene(T valor) {
        // TODO: Recorrer la lista buscando el valor. Usar equals() para comparar.

        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.valor.equals(valor)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public int tamaño() {
        return tamaño;
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    /**
     * Convierte la lista a un arreglo de Strings (para mostrar en la GUI).
     * TODO: Implementar
     */
    public String[] toArray() {
        // TODO: Crear un arreglo de tamaño 'tamaño', recorrer la lista
        // y llenar el arreglo con toString() de cada valor.
        
            if (tamaño == 0) {
                return new String[0];
            }
        String[] arreglo = new String[tamaño];
        Nodo<T> actual = cabeza;
        int i = 0;
        while (actual != null) {
            arreglo[i] = actual.valor.toString();
            actual = actual.siguiente;
            i++;
        }
        return arreglo;
    }
}
