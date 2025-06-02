package ucr.lab.domain;

import org.junit.jupiter.api.Test;
import ucr.lab.TDA.AVLTree;
import ucr.lab.TDA.TreeException;
import ucr.lab.utility.FileReader;

import java.util.List;

class PassengerTest {


    @Test
    public void TESTUser() {
        List<User> userList = FileReader.loadUsers();

        if (userList.isEmpty()) {
            System.out.println("No se han agregado usuarios al documento");

        } else {
            System.out.println("Usuarios agregados:");
            for (User user : userList) {
                System.out.println(user);
            }
        }
    }

    @Test
    public void TESTPassenger() {
        List<Passenger> passengers = FileReader.loadPassengers();

        if (passengers.isEmpty()) {
            System.out.println("No se han agregado usuarios al documento");

        } else {
            System.out.println("Usuarios agregados:");
            for (Passenger passenger : passengers) {
                System.out.println(passenger);
            }
        }
    }

    @Test
    public void TESTPassengersINAVL() {

        AVLTree passengerTree = new AVLTree();

        // cargar los passangers desde json
        List<Passenger> passengers = FileReader.loadPassengers();
        for (Passenger passenger : passengers) {
            try {
                passengerTree.add(passenger);
                passengerTree.add("Gloriana");


            } catch (TreeException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Pasajeros en el arbol AVL:");

        System.out.println(passengerTree);
    }
}

