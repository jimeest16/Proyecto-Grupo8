package ucr.lab.domain;


import org.junit.jupiter.api.Test;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.list.CircularLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.utility.FileReader;


import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {


    @Test
    public void TESTUser() throws ListException {
        // Cargar la lista de usuarios
        CircularLinkedList userList = FileReader.loadUsers();

        System.out.println("\n--- Lista de Usuarios ---");
        if (userList.isEmpty()) {
            System.out.println("No se han agregado usuarios al documento.");
            assertTrue(userList.isEmpty(), "La lista de usuarios debería estar vacía.");
        } else {
            System.out.println("Usuarios agregados:");
            Object current = userList.getFirst();
            Object start = current;
            do {
                System.out.println((User) current);
                current = userList.getNext();
            } while (current != start);
            assertFalse(userList.isEmpty(), "La lista de usuarios no debería estar vacía.");
            assertTrue(userList.size() > 0, "La lista de usuarios debería contener elementos.");
        }
    }


    @Test
    public void TESTPassenger() {
        try {

            SinglyLinkedList passengers = FileReader.loadPassengers();

            System.out.println("\n --- Lista de Pasajeros ---");
            if (passengers.isEmpty()) {
                System.out.println("No se han agregado pasajeros al documento.");
                assertTrue(passengers.isEmpty(), "La lista de pasajeros debería estar vacía.");
            } else {
                System.out.println("Pasajeros agregados:");

                for (int i = 1; i <= passengers.size(); i++) {
                    Passenger passenger = (Passenger) passengers.getNode(i).data;
                    System.out.println(passenger);
                }
                assertFalse(passengers.isEmpty(), "La lista de pasajeros no debería estar vacía.");
                assertTrue(passengers.size() > 0, "La lista de pasajeros debería contener elementos.");
            }
        } catch (ListException e) {
            System.err.println("Error al cargar pasajeros desde SinglyLinkedList: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado en TESTPassenger: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Test
    public void TESTPassengersINAVL() throws TreeException {
        AVLTree passengerTree = new AVLTree();

        try {

            SinglyLinkedList passengers = FileReader.loadPassengers();
            System.out.println("\n---Pasajeros en Árbol AVL ---");
            if (passengers.isEmpty()) {
                System.out.println("No hay pasajeros para agregar al árbol AVL.");
                assertTrue(passengerTree.isEmpty(), "El árbol AVL debería estar vacío.");
            } else {

                for (int i = 1; i <= passengers.size(); i++) {
                    Passenger passenger = (Passenger) passengers.getNode(i).data;
                    try {
                        passengerTree.add(passenger);
                    } catch (TreeException e) {
                        System.err.println("Error al agregar pasajero al árbol: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                System.out.println("Contenido del árbol AVL (InOrder):");
                System.out.println(passengerTree.inOrder()+"\n");
                assertFalse(passengerTree.isEmpty(), "El árbol AVL no debería estar vacío.");
                assertEquals(passengers.size(), passengerTree.size(), "El tamaño del árbol debería coincidir con el número de pasajeros cargados.");
            }
        } catch (ListException e) {
            System.err.println("Error al cargar pasajeros desde SinglyLinkedList para AVL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado en TESTPassengersINAVL: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Test
    public void testAddAndFindUser() throws ListException {
        System.out.println("\n--- AddAndFindUser: Agregar y Buscar Usuario ---");

        User newUser = new User(1000, "JimenaSalasThames", "pass123", "jime@123.com", "usuario");

        // Antes de añadir, verificar que el usuario no existe en la lista cargada
        CircularLinkedList initialUsers = FileReader.loadUsers();
        boolean foundBeforeAdd = false;
        if (!initialUsers.isEmpty()) {
            Object current = initialUsers.getFirst();
            Object start = current;
            do {
                if (((User) current).getName().equals(newUser.getName())) {
                    foundBeforeAdd = true;
                    break;
                }
                current = initialUsers.getNext();
            } while (current != start);
        }
        assertFalse(foundBeforeAdd, "El usuario de prueba no debería existir antes de agregarlo.");

        // Añadir el nuevo usuario
        FileReader.addUser(newUser);
        System.out.println("Usuario '" + newUser.getName() + "' agregado.");

        // Cargar la lista actualizada y verificar que el usuario ahora existe
        CircularLinkedList updatedUsers = FileReader.loadUsers();
        boolean foundAfterAdd = false;
        if (!updatedUsers.isEmpty()) {
            Object current = updatedUsers.getFirst();
            Object start = current;
            do {
                User u = (User) current;
                if (u.getName().equals(newUser.getName()) && u.getId() == newUser.getId()) {
                    foundAfterAdd = true;
                    System.out.println("Usuario '" + newUser.getName() + "' encontrado en la lista actualizada.");
                    break;
                }
                current = updatedUsers.getNext();
            } while (current != start);
        }
        assertTrue(foundAfterAdd, "El usuario de prueba debería existir después de agregarlo.");
    }


}