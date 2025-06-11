package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import ucr.lab.TDA.stack.LinkedStack;
import ucr.lab.TDA.stack.StackException;

import java.util.List;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String role; // user o admin
    LinkedStack flightHistory;

    public User() {
        // Constructor requerido por Jackson
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String name, String password, String email, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.flightHistory = new LinkedStack();
    }

    @JsonSetter
    public void setFlightHistory (List<Object> list) throws StackException {
        LinkedStack linkedList = new LinkedStack();
        for (Object i : list)
            linkedList.push(i);
        this.flightHistory = linkedList;
    }

    @JsonGetter
    public List<Object> getFlightHistory () {
        return flightHistory.toList();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public LinkedStack listGetFlightHistory() {
        return flightHistory;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFlightHistory(LinkedStack flightHistory) {
        this.flightHistory = flightHistory;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", flightHistory=" + flightHistory +
                '}';
    }
}