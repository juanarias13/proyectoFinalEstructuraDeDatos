package estructuras;

import java.util.*;

/**
 * Grafo implementado con lista de adyacencia para el mapa galáctico.
 * 
 * INSTRUCCIONES: Implementa los métodos de BFS y DFS marcados con TODO.
 * Los métodos básicos (agregarNodo, agregarArista, vecinos) están implementados.
 * 
 * NOTA: Se permite usar HashMap, HashSet, ArrayList y LinkedList de java.util
 * SOLO para la estructura interna del grafo y los algoritmos BFS/DFS.
 */
public class Grafo {

    private Map<String, List<String>> adyacencia;

    public Grafo() {
        adyacencia = new HashMap<>();
    }

    public void agregarNodo(String nodo) {
        adyacencia.putIfAbsent(nodo, new ArrayList<>());
    }

    public void agregarArista(String origen, String destino) {
        agregarNodo(origen);
        agregarNodo(destino);
        if (!adyacencia.get(origen).contains(destino)) {
            adyacencia.get(origen).add(destino);
        }
        if (!adyacencia.get(destino).contains(origen)) {
            adyacencia.get(destino).add(origen);
        }
    }

    public List<String> vecinos(String nodo) {
        return adyacencia.getOrDefault(nodo, new ArrayList<>());
    }

    public Set<String> obtenerNodos() {
        return adyacencia.keySet();
    }

    public boolean hayArista(String origen, String destino) {
        List<String> vec = adyacencia.get(origen);
        return vec != null && vec.contains(destino);
    }

    public int cantidadNodos() {
        return adyacencia.size();
    }

    /**
     * BFS: Encuentra el camino más corto entre dos planetas.
     * Retorna la lista de planetas en orden del camino.
     * Si no hay camino, retorna lista vacía.
     * 
     * TODO: Implementar usando una cola (java.util.LinkedList como Queue).
     * Algoritmo:
     * 1. Crear un Map<String,String> para guardar el padre de cada nodo
     * 2. Crear un Set<String> de visitados
     * 3. Encolar el nodo inicio, marcarlo como visitado
     * 4. Mientras la cola no esté vacía:
     *    - Desencolar un nodo
     *    - Si es el destino, reconstruir el camino usando el mapa de padres
     *    - Si no, encolar todos sus vecinos no visitados
     * 5. Si la cola se vacía sin encontrar destino, retornar lista vacía
     */
    public List<String> caminoMasCorto(String inicio, String destino) {
        List<String> camino = new ArrayList<>();
        if (inicio == null || destino == null) return camino;
        if (!adyacencia.containsKey(inicio) || !adyacencia.containsKey(destino)) return camino;

        Queue<String> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();
        Map<String, String> padre = new HashMap<>();

        cola.add(inicio);
        visitados.add(inicio);
        padre.put(inicio, null);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            if (actual.equals(destino)) {
                // Reconstruir camino
                LinkedList<String> ruta = new LinkedList<>();
                for (String nodo = destino; nodo != null; nodo = padre.get(nodo)) {
                    ruta.addFirst(nodo);
                }
                return ruta;
            }
            for (String vecino : vecinos(actual)) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    padre.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        return camino; // vacío si no se encontró
    }
    

    /**
     * DFS: Descubre todos los planetas alcanzables desde un punto.
     * Retorna la lista de planetas en orden de descubrimiento.
     * 
     * TODO: Implementar usando recursividad.
     * Algoritmo:
     * 1. Crear un Set<String> de visitados y una List<String> para el orden
     * 2. Llamar al método recursivo dfsRecursivo
     * 3. En dfsRecursivo: marcar nodo como visitado, agregarlo a la lista,
     *    y llamar recursivamente para cada vecino no visitado
     */
    public List<String> explorarDFS(String inicio) {
        List<String> orden = new ArrayList<>();
        if (inicio == null) return orden;
        if (!adyacencia.containsKey(inicio)) return orden;
        Set<String> visitados = new HashSet<>();
        dfsRecursivo(inicio, visitados, orden);
        return orden;
    }

    private void dfsRecursivo(String nodo, Set<String> visitados, List<String> orden) {
        if (visitados.contains(nodo)) return;
        visitados.add(nodo);
        orden.add(nodo);
        for (String vecino : vecinos(nodo)) {
            if (!visitados.contains(vecino)) {
                dfsRecursivo(vecino, visitados, orden);
            }
        }
    }
}
