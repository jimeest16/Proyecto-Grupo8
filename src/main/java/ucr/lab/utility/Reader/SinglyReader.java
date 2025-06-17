
package ucr.lab.utility.Reader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.Destination;

import java.io.IOException;

public class SinglyReader extends JsonDeserializer<SinglyLinkedList> {


    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public SinglyLinkedList deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        SinglyLinkedList list = new SinglyLinkedList();


        if (p.getCurrentToken() != JsonToken.START_ARRAY) {
            System.err.println("ERROR SinglyReader: Se esperaba un arreglo JSON para destinationList, pero se encontró: " + p.getCurrentToken() + " en la ruta: " + p.getParsingContext().getCurrentName());
            p.skipChildren();
            return list;
        }


        while (p.nextToken() != JsonToken.END_ARRAY) {
            // Cada elemento dentro del arreglo debería ser un objeto Destination
            if (p.getCurrentToken() == JsonToken.START_OBJECT) {
                // AQUÍ ESTÁ LA LÍNEA CLAVE:
                Destination dest = mapper.readValue(p, Destination.class);
                try {
                    list.add(dest);

                } catch (Exception e) { // Cambié ListException a Exception para atrapar cualquier error de add
                    throw new IOException("Error al añadir Destination a SinglyLinkedList: " + e.getMessage(), e);
                }
            } else {
                System.err.println("Advertencia SinglyReader: Se encontró un token inesperado en el arreglo de destinos: " + p.getCurrentToken() + ". Saltando.");
                p.skipChildren();
            }
        }
        return list;
    }
}