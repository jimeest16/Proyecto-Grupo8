package ucr.lab.data;

import org.junit.jupiter.api.Test;
import ucr.lab.TDA.list.DoublyLinkedList;
import ucr.lab.domain.Airport;
import ucr.lab.domain.User;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonManagerTest {
    @Test
    void addAirport() {
        DoublyLinkedList airports = new DoublyLinkedList();
        airports.add(new Airport(1, "Los Angeles International Airport", "USA", "Activo"));
        airports.add(new Airport(2, "John F. Kennedy International Airport", "USA", "Inactivo"));
        airports.add(new Airport(3, "Aeropuerto Internacional de San José", "COSTA RICA", "Activo"));
        airports.add(new Airport(4, "Ciudad Celeste International Airport", "TERRANOVA", "Inactivo"));
        airports.add(new Airport(5, "Narita International Airport", "JAPAN", "Activo"));
        airports.add(new Airport(6, "Frankfurt Airport", "GERMANY", "Activo"));
        airports.add(new Airport(7, "Aeropuerto de Buenos Aires", "ARGENTINA", "Inactivo"));
        airports.add(new Airport(8, "Dragon City AirHub", "FANTASIA", "Activo"));
        airports.add(new Airport(9, "Aeropuerto Internacional Arturo Merino Benítez", "CHILE", "Activo"));
        airports.add(new Airport(10, "Beijing Capital International Airport", "CHINA", "Activo"));
        airports.add(new Airport(11, "Aeropuerto de Atlantis", "MITICA", "Inactivo"));
        airports.add(new Airport(12, "Aeropuerto Internacional de Monterrey", "MEXICO", "Activo"));
        airports.add(new Airport(13, "Cape Town International Airport", "SOUTH AFRICA", "Activo"));
        airports.add(new Airport(14, "Aeropuerto de Nimbus Prime", "AETHER", "Activo"));
        airports.add(new Airport(15, "Aeropuerto de Lima", "PERU", "Inactivo"));
        airports.add(new Airport(16, "Istanbul Airport", "TURKEY", "Activo"));
        airports.add(new Airport(17, "Barcelona–El Prat Airport", "SPAIN", "Activo"));
        airports.add(new Airport(18, "Aeropuerto Internacional Simón Bolívar", "VENEZUELA", "Inactivo"));
        airports.add(new Airport(19, "Zürich Airport", "SWITZERLAND", "Activo"));
        airports.add(new Airport(20, "Cielo Alto Air Station", "ESTELIA", "Activo"));
        AirportManager.setAirports(airports);
        try {
            AirportManager.saveAirports();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loadAirport() {
        System.out.println("Loading Airport");
        try {
            AirportManager.loadAirports();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(AirportManager.getAirports());
    }

    @Test
    void addUsers() {
        try {
            UserManager.getUsers().clear();
            UserManager.saveUsers();
            UserManager.add(new User(1, "usuario", "usuario123", "usuario@correo.com", "user"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loadUser() {
        System.out.println("Loading Users");
        try {
            UserManager.loadUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(UserManager.getUsers());
    }
}