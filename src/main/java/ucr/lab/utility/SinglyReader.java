package ucr.lab.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.Destination;

import java.io.IOException;
//Info investigada:como tal Jackson no sabe como tratar nuestras clases por lo tanto  primero se tiene que
// dar a entender que es una lista y que ademas, campos, mayusculas o camelCase no estropen la lectura del text

// Esta clase le dice a Jackson cómo leer un arreglo JSON y convertirlo en mi  SinglyLinkedList
public class SinglyReader extends JsonDeserializer<SinglyLinkedList> {

//    @Override
//    public SinglyLinkedList deserialize(JsonParser p, DeserializationContext ctxt)
//            throws IOException {
//        SinglyLinkedList list = new SinglyLinkedList();
//        JsonNode node = p.getCodec().readTree(p); // Lee el nodo JSON completo para este campo
//
//        if (node.isArray()) { // Si el valor JSON es un arreglo
//            for (JsonNode elementNode : node) {
//                // Supongo que los elementos son Strings
//
//                list.add(elementNode.asText()); // Agrega cada elemento del arreglo JSON a la list personalizada TDA
//            }
//        } else if (node.isTextual()) {
//            list.add(node.asText());
//        }
//
//        return list; // Retorna tu lista personalizada llena con los datos
//    }

    @Override
    public SinglyLinkedList deserialize(JsonParser listToConvert, DeserializationContext context) throws IOException {

        ObjectMapper mapper = (ObjectMapper) listToConvert.getCodec();
        SinglyLinkedList list = new SinglyLinkedList();


        ArrayNode arrayNode = mapper.readTree(listToConvert);


        for (JsonNode node : arrayNode) {

            Destination destination = mapper.treeToValue(node, Destination.class);
            try {

                list.add(destination);
            } catch (Exception e) {
                throw new IOException("Error inesperado al agregar elemento a SinglyLinkedList: " + e.getMessage(), e);
            }
        }
        return list;
    }
}
