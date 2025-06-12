package ucr.lab.utility.Reader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.Flight;

import java.io.IOException;

// Esta clase le dice a Jackson cómo leer un arreglo JSON y convertirlo en mi SinglyLinkedList
public class SinglyReader extends JsonDeserializer<SinglyLinkedList> {

    @Override
    public SinglyLinkedList deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        SinglyLinkedList list = new SinglyLinkedList();
        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        JsonNode node = p.getCodec().readTree(p);

        if (node.isArray()) {
            for (JsonNode elementNode : node) {
                try {
                    Flight flight = mapper.treeToValue(elementNode, Flight.class);
                    list.add(flight);
                } catch (IOException e) {
                    System.err.println("ERROR SinglyReader: Fallo al deserializar un objeto Flight: " + elementNode.toString() + " - " + e.getMessage());
                }
            }
        } else if (node.isObject()) {
            try {
                Flight flight = mapper.treeToValue(node, Flight.class);
                list.add(flight);
            } catch (IOException e) {
                System.err.println("ERROR SinglyReader: Fallo al deserializar un único objeto Flight: " + node.toString() + " - " + e.getMessage());
            }
        }



        return list;
    }
}