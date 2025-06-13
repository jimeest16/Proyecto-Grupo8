package ucr.lab.utility.Reader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ucr.lab.TDA.list.SinglyLinkedList; // Importa la SinglyLinkedList NO genérica
import ucr.lab.domain.Flight;

import java.io.IOException;

// Deserializador para SinglyLinkedList
public class SinglyListDeserializer extends StdDeserializer<SinglyLinkedList> {

    public SinglyListDeserializer() {
        this(null);
    }

    protected SinglyListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public SinglyLinkedList deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        SinglyLinkedList list = new SinglyLinkedList();
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.isArray()) {
            for (JsonNode element : node) {

                Flight flight = jp.getCodec().treeToValue(element, Flight.class);
                if (flight != null) {
                    list.add(flight); // Añade el objeto Flight
                }
            }
        } else {
            System.err.println("Advertencia: Se esperaba un array JSON para flightHistory, pero se encontró: " + node.getNodeType());
        }
        return list;
    }
}