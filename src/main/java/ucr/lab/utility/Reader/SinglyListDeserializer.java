package ucr.lab.utility.Reader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ucr.lab.TDA.list.SinglyLinkedList; // Importa la SinglyLinkedList NO genérica
import ucr.lab.domain.Flight;

import java.io.IOException;

// Deserializador para SinglyLinkedList (no genérica)
public class SinglyListDeserializer extends StdDeserializer<SinglyLinkedList> { // Tipo aquí es SinglyLinkedList

    public SinglyListDeserializer() {
        this(null);
    }

    protected SinglyListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public SinglyLinkedList deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        SinglyLinkedList list = new SinglyLinkedList(); // Crea una nueva lista NO genérica
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.isArray()) {
            for (JsonNode element : node) {
                // Deserializa cada elemento como un objeto Flight
                Flight flight = jp.getCodec().treeToValue(element, Flight.class);
                if (flight != null) {
                    list.add(flight); // Añade el objeto Flight (que se guarda como Object)
                }
            }
        } else {
            System.err.println("Advertencia: Se esperaba un array JSON para flightHistory, pero se encontró: " + node.getNodeType());
        }
        return list;
    }
}