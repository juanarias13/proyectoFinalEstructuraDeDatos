package estructuras;

/**
 * Árbol binario de búsqueda para el ranking de puntuaciones.
 * 
 * INSTRUCCIONES: Implementa todos los métodos marcados con TODO.
 * El árbol ordena por puntuación. El recorrido inorden debe mostrar
 * el ranking de mayor a menor.
 */
public class ArbolBST {

    private NodoArbol raiz;

    private static class NodoArbol {
        int puntuacion;
        String nombre;
        NodoArbol izquierdo;
        NodoArbol derecho;

        NodoArbol(String nombre, int puntuacion) {
            this.nombre = nombre;
            this.puntuacion = puntuacion;
        }
    }

    public ArbolBST() {
        raiz = null;
    }

    /**
     * Inserta un nuevo jugador con su puntuación en el BST.
     * TODO: Implementar (usar recursividad)
     */
    public void insertar(String nombre, int puntuacion) {
        // TODO: Llamar al método recursivo insertarRecursivo

        raiz = insertarRecursivo(raiz, nombre, puntuacion);
    }

    private NodoArbol insertarRecursivo(NodoArbol nodo, String nombre, int puntuacion) {
        // TODO: Implementar inserción recursiva en BST.
        // Si nodo es null, crear nuevo NodoArbol.
        // Si puntuacion < nodo.puntuacion, insertar a la izquierda.
        // Si puntuacion > nodo.puntuacion, insertar a la derecha.
        if (nodo == null) {
            return new NodoArbol(nombre, puntuacion);
        }
        if (puntuacion < nodo.puntuacion) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, nombre, puntuacion);
        } else {
            // Si la puntuación es igual o mayor, insertar a la derecha para
            // permitir múltiples jugadores con la misma puntuación.
            nodo.derecho = insertarRecursivo(nodo.derecho, nombre, puntuacion);
        }
        return nodo;
    }

    /**
     * Busca si una puntuación existe en el árbol.
     * TODO: Implementar (usar recursividad)
     */
    public boolean buscar(int puntuacion) {
        // TODO: Implementar búsqueda recursiva en BST.

        return buscarRecursivo(raiz, puntuacion);
    }

    private boolean buscarRecursivo(NodoArbol nodo, int puntuacion) {

        if (nodo == null) {
            return false;
        }
        if (puntuacion == nodo.puntuacion) {
            return true;
        }
        if (puntuacion < nodo.puntuacion) {
            return buscarRecursivo(nodo.izquierdo, puntuacion);
        } else {
            return buscarRecursivo(nodo.derecho, puntuacion);
        }
    }

    /**
     * Retorna el ranking como String, ordenado de MAYOR a menor puntuación.
     * Usa recorrido inorden invertido (derecho → raíz → izquierdo).
     * TODO: Implementar
     */
    public String obtenerRanking() {
        StringBuilder sb = new StringBuilder();
        // TODO: Llamar a un método recursivo que haga recorrido inorden invertido
        // (primero derecho, luego raíz, luego izquierdo) para obtener de mayor a menor.
        inorden(raiz, sb);
        return sb.toString();
    }

    private void inorden(NodoArbol nodo, StringBuilder sb) {
        // TODO: Implementar recorrido inorden invertido (derecho → raíz → izquierdo)
        // Para cada nodo, agregar: nombre + ": " + puntuacion + " pts\n"

        if (nodo == null) {
            return;
        }
        inorden(nodo.derecho, sb);
        sb.append(nodo.nombre).append(": ").append(nodo.puntuacion).append(" pts\n");
        inorden(nodo.izquierdo, sb);

    }

    /**
     * Calcula la altura del árbol recursivamente.
     * TODO: Implementar
     */
    public int altura() {
        // TODO: Implementar cálculo recursivo de altura.
        // Árbol vacío = -1. Altura = 1 + max(alturaIzq, alturaDer)
        return alturaRecursiva(raiz);
    }
    
    private int alturaRecursiva(NodoArbol nodo) {
    if (nodo == null) {
        return -1;
    }

    int alturaIzq = alturaRecursiva(nodo.izquierdo);
    int alturaDer = alturaRecursiva(nodo.derecho);

    return 1 + Math.max(alturaIzq, alturaDer);
}

    public boolean estaVacio() {
        return raiz == null;
    }
}
