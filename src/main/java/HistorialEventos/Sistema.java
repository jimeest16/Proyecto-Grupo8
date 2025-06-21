package HistorialEventos;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;

public class Sistema {
    private LinkedQueue bitacora; // Usamos tu LinkedQueue

    public Sistema() {
        this.bitacora = new LinkedQueue();
    }

    public void registrarEvento(String usuario, String descripcion) {
        Evento evento = new Evento(usuario, descripcion);
        try {
            bitacora.enQueue(evento);
            System.out.println("Evento registrado: " + evento);
        } catch (QueueException e) {
            System.err.println("Error al registrar evento en la bitácora: " + e.getMessage());
        }
    }

    public void ingresoAlSistema(String usuario) {
        registrarEvento(usuario, "Ingreso al sistema");
    }

    public void registrarReservacion(String usuario, String detalleReservacion) {
        registrarEvento(usuario, "Registro de reservación: " + detalleReservacion);
    }

    public void historialVuelos(String usuario) {
        registrarEvento(usuario, "Consulta de historial de vuelos realizados");
    }

    /**
     * Muestra todos los eventos registrados en la bitácora.
     * Este método utiliza el toString() de tu LinkedQueue, que desencola y luego vuelve a encolar
     * los elementos para mantener la cola intacta después de la visualización.
     */
    public void mostrarBitacora() {
        System.out.println("\n--- Bitácora General de Eventos ---");
        if (bitacora.isEmpty()) {
            System.out.println("La bitácora está vacía.");
            return;
        }
        System.out.println(bitacora.toString());
    }
}