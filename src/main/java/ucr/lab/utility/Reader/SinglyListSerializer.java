package ucr.lab.utility.Reader;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ucr.lab.TDA.list.SinglyLinkedList; // Importa la SinglyLinkedList NO genérica
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.Flight;

import java.io.IOException;

// Serializador para SinglyLinkedList (no genérica)
public class SinglyListSerializer extends StdSerializer<SinglyLinkedList> { // Tipo aquí es SinglyLinkedList

    public SinglyListSerializer() {
        this(null);
    }

    protected SinglyListSerializer(Class<SinglyLinkedList> t) {
        super(t);
    }

    @Override
    public void serialize(SinglyLinkedList value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartArray();

        if (value != null && !value.isEmpty()) {
            try {
                for (int i = 1; i <= value.size(); i++) {
                    // *** CRÍTICO: Castear a Flight aquí ***
                    Flight flight = (Flight) value.get(i); // get(i) retorna Object, necesitas castear
                    gen.writeObject(flight); // Jackson serializa el objeto Flight
                }
            } catch (ListException e) {
                System.err.println("Error durante la serialización de SinglyLinkedList: " + e.getMessage());
                throw new IOException("Error al serializar la lista de vuelos", e);
            } catch (ClassCastException e) {
                // Es posible si la lista contiene algo que no sea Flight
                System.err.println("Error de tipo al serializar: Elemento inesperado en SinglyLinkedList. " + e.getMessage());
                throw new IOException("Error de tipo de dato inesperado en la lista de vuelos", e);
            }
        }
        gen.writeEndArray();
    }
}