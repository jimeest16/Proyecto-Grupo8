package ucr.lab.utility.Reader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.Flight;
import ucr.lab.TDA.list.ListException; // Asegúrate de que esto está importado si tu add() lo lanza

import java.io.IOException;

public class FlightReader extends JsonDeserializer<SinglyLinkedList> {

    @Override
    public SinglyLinkedList deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        SinglyLinkedList list = new SinglyLinkedList(); // Tu lista vacía
        ArrayNode arrayNode = mapper.readTree(p);


        System.out.println("\nDEBUG SinglyReader: --- Entrando al deserializador ---");
        System.out.println("DEBUG SinglyReader: ArrayNode recibido. ¿Está vacío? " + arrayNode.isEmpty());
        System.out.println("DEBUG SinglyReader: Número de elementos en JSON ArrayNode: " + arrayNode.size());


        if (arrayNode.isEmpty()) {
            System.out.println("DEBUG SinglyReader: El array JSON está vacío, devolviendo lista vacía.");
            return list;
        }

        for (JsonNode node : arrayNode) {
            try {

                System.out.println("DEBUG SinglyReader: Intentando mapear un nodo a Flight. Nodo JSON: " + node.toString());
                Flight flight = mapper.treeToValue(node, Flight.class);

                System.out.println("DEBUG SinglyReader: Flight deserializado (su toString()): " + flight.toString()); // Aquí veremos el toString()

                list.add(flight);

                System.out.println("DEBUG SinglyReader: Flight añadido a SinglyLinkedList. Tamaño actual de la lista: " + list.size());
            } catch (ListException e) {
                System.err.println("ERROR SinglyReader: Falló al añadir Flight a SinglyLinkedList: " + e.getMessage());
                throw new IOException("Error interno de la lista al añadir vuelo.", e);
            } catch (Exception e) {
                System.err.println("ERROR SinglyReader: Falló al deserializar o añadir Flight. Nodo JSON: " + node.toString() + ". Error: " + e.getMessage());
                e.printStackTrace();
                throw new IOException("Error inesperado procesando el elemento del vuelo.", e);
            }
        }

        try {
            System.out.println("DEBUG SinglyReader: SinglyLinkedList para flightHistory finalizó de poblarse. Tamaño final de la lista: " + list.size());
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
        System.out.println("DEBUG SinglyReader: Contenido final de la lista (SinglyLinkedList.toString()): " + list.toString());
        System.out.println("DEBUG SinglyReader: --- Saliendo del deserializador --- \n");
        return list;
    }
}