package ucr.lab.TDA.queue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private static final LinkedQueue bitacora = new LinkedQueue();
    private static final String RUTA_BITACORA = "bitacora.txt";
    private static final DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        registrarEvento("JuanPerez", "Ingresó al sistema.");
        registrarEvento("AnaGarcia", "Registró reservación para vuelo CR-NYC el 2025-07-15.");
        registrarEvento("JuanPerez", "Consultó historial de vuelos.");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        registrarEvento("PedroLopez", "Ingresó al sistema.");
        registrarEvento("PedroLopez", "Registró reservación para hotel del 2025-08-01 al 2025-08-05.");

        // Mostrar bitácora
        System.out.println("\n--- Bitácora de eventos ---");
        mostrarBitacora();

        System.out.println("\n--- Mostrando bitácora de nuevo (debería tener los mismos elementos) ---");
        mostrarBitacora();
    }

    private static void registrarEvento(String autor, String mensaje) {
        String marcaTiempo = LocalDateTime.now().format(formato);
        String entrada = "[" + marcaTiempo + "] " + autor + ": " + mensaje;
        try {
            bitacora.enQueue(entrada);
            try (FileWriter fw = new FileWriter(RUTA_BITACORA, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(entrada);
            }
        } catch (IOException | QueueException e) {
            System.err.println("Error al registrar en la bitácora: " + e.getMessage());
        }
    }

    private static void mostrarBitacora() {
        try {
            System.out.println(bitacora);
        } catch (Exception e) {
            System.err.println("Error al mostrar la bitácora: " + e.getMessage());
        }
    }
}
