package ucr.lab.utility.Reader; // Asegúrate de que esto coincide con la estructura de tu paquete

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.Flight; // Importa tu clase Flight

import java.io.IOException;

/**
 * Serializador personalizado para SinglyLinkedList (lista enlazada simple) para Jackson.
 * Esto permite a Jackson saber cómo convertir tu estructura de lista personalizada
 * en un array JSON cuando se serializa un objeto que la contiene (por ejemplo, Passenger).
 */
public class SinglyListSerializer extends StdSerializer<SinglyLinkedList> {

    public SinglyListSerializer() {
        this(null);
    }

    public SinglyListSerializer(Class<SinglyLinkedList> t) {
        super(t);
    }

    /**
     * Método principal de serialización. Jackson lo llama para escribir la lista en JSON.
     * @param value La SinglyLinkedList que se va a serializar.
     * @param gen El JsonGenerator para escribir contenido JSON.
     * @param provider El SerializerProvider.
     * @throws IOException Si ocurre un error de E/S durante la escritura JSON.
     */
    @Override
    public void serialize(SinglyLinkedList value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // --- INICIO DE DEPURACIÓN ---
        System.out.println("[DEBUG SERIALIZER] Iniciando serialización de SinglyLinkedList.");
        // --- FIN DE DEPURACIÓN ---

        gen.writeStartArray(); // Comienza a escribir un array JSON para representar la lista
        if (value != null && !value.isEmpty()) {
            try {
                // Itera sobre cada elemento de la SinglyLinkedList
                for (int i = 1; i <= value.size(); i++) {
                    Object element = value.get(i); // Obtiene el elemento en la posición actual
                    if (element instanceof Flight) { // Verifica si el elemento es una instancia de Flight
                        // --- INICIO DE DEPURACIÓN ---
                        System.out.println("[DEBUG SERIALIZER] Serializando Vuelo: " + ((Flight)element).getNumber() + " para el historial del pasajero.");
                        // --- FIN DE DEPURACIÓN ---
                        // Delega la serialización del objeto Flight al serializador predeterminado de Jackson.
                        // Esto funciona si la clase Flight tiene las anotaciones @JsonProperty
                        // y los getters/setters adecuados.
                        gen.writeObject(element);
                    } else {
                        // --- INICIO DE DEPURACIÓN ---
                        System.err.println("[DEBUG SERIALIZER ERROR] Advertencia: El elemento en SinglyLinkedList no es un Flight: " + (element != null ? element.getClass().getName() : "null"));
                        // --- FIN DE DEPURACIÓN ---
                        gen.writeNull(); // Opcionalmente escribe null o simplemente salta el elemento
                    }
                }
            } catch (ListException e) {
                // Maneja las excepciones específicas de tu implementación de SinglyLinkedList
                // --- INICIO DE DEPURACIÓN ---
                System.err.println("[DEBUG SERIALIZER ERROR] Error al iterar SinglyLinkedList durante la serialización: " + e.getMessage());
                // --- FIN DE DEPURACIÓN ---
                throw new IOException("Fallo al serializar SinglyLinkedList debido a un error de lista.", e);
            }
        } else {
            // --- INICIO DE DEPURACIÓN ---
            System.out.println("[DEBUG SERIALIZER] SinglyLinkedList es nula o está vacía. Escribiendo un array vacío.");
            // --- FIN DE DEPURACIÓN ---
        }
        gen.writeEndArray(); // Termina de escribir el array JSON

        // --- INICIO DE DEPURACIÓN ---
        System.out.println("[DEBUG SERIALIZER] Serialización de SinglyLinkedList finalizada.");
        // --- FIN DE DEPURACIÓN ---
    }
}
