package ucr.lab.TDA;

public class Rutas {
    private SpecialSinglyLinkedListGraph grafo;

    public Rutas(SpecialSinglyLinkedListGraph grafo) {
        this.grafo = grafo;
    }
    // agregar ruta
    public void agregarRuta(Object origen, Object destino,Object peso) throws Exception{

        grafo.addEdgeWeight(origen,destino,peso);
    }
    // para modificar la ruta -> cambiarle el peso
    public void modificarRuta(Object origen, Object destino, Object nuevoPeso) throws Exception{
        if(!grafo.containsEdge(origen, destino)){
            throw new Exception("No existe una ruta entre los dos aeropuertos");

        }
        grafo.addWeight(origen,destino,nuevoPeso);
    }
    // calcular la ruta más corta usando Dijkstra
    public int[] calcularRutaMasCorta(Object origen, Object destino) throws Exception {
        return grafo.dijkstra(origen, destino);
    }

}
