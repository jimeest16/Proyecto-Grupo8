package ucr.lab.TDA.queue;

import HistorialEventos.Sistema;

public class Main {
    public static void main(String[] args) {
        Sistema miSistema = new Sistema();

        // Simular eventos
        miSistema.ingresoAlSistema("JuanPerez");
        miSistema.registrarReservacion("AnaGarcia", "Vuelo CR-NYC el 2025-07-15");
        miSistema.historialVuelos("JuanPerez");

        // Pequeña pausa para simular el paso del tiempo o permitir que las operaciones se asienten
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción
        }

        miSistema.ingresoAlSistema("PedroLopez");
        miSistema.registrarReservacion("PedroLopez", "Hotel San Jose del 2025-08-01 al 2025-08-05");

        // Mostrar la bitácora
        // Tu toString() de LinkedQueue se encarga de mostrar y re-encolar,
        // por lo que la cola no se vacía permanentemente.
        miSistema.mostrarBitacora();

        // Puedes mostrarla de nuevo para confirmar que los elementos siguen ahí
        System.out.println("\n--- Mostrando bitácora de nuevo (debería tener los mismos elementos) ---");
        miSistema.mostrarBitacora();
    }
}