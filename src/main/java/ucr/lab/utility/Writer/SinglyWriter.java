package ucr.lab.utility.Writer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.Destination;

import java.io.IOException;

public class SinglyWriter extends JsonSerializer<SinglyLinkedList> {

    @Override
    public void serialize(SinglyLinkedList value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isEmpty()) {
            gen.writeStartArray(); // Escribe un arreglo vacío
            gen.writeEndArray();
            return;
        }

        gen.writeStartArray(); // Comienza el arreglo JSON

        try {
            for (int i = 1; i <= value.size(); i++) {
                Object element = value.get(i);
                if (element instanceof Destination) {
                    gen.writeObject(element); // Deja que Jackson serialice el objeto Destination
                } else {
                    // Manejo de error si un elemento no es Destination
                    System.err.println("Advertencia: Se encontró un objeto no-Destination en SinglyLinkedList durante la serialización: " + element.getClass().getName());

                }
            }
        } catch (ListException e) {
            throw new IOException("Error al serializar SinglyLinkedList: " + e.getMessage(), e);
        }

        gen.writeEndArray(); // Cierra el arreglo JSON
    }
}