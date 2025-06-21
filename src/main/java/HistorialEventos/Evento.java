package HistorialEventos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Evento {
    private LocalDate fecha;
    private LocalTime hora;
    private String usuario;
    private String descripcion;

    public Evento(String usuario, String descripcion) {
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.usuario = usuario;
        this.descripcion = descripcion;
    }

    // Getters
    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("[%s %s] Usuario: %s - Evento: %s",
                fecha.format(dateFormatter),
                hora.format(timeFormatter),
                usuario,
                descripcion);
    }
}