package ucr.lab.utility;

import org.junit.jupiter.api.Test;
import ucr.lab.TDA.list.CircularLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.User;

import java.util.List;

class PasswordEncriptionTest {

    @Test
    public void testing() {
        String password = "user123";
        String encrypted = PasswordEncription.encriptPassWord(password);
        System.out.println("Contraseña encriptada: " + encrypted);

        String password2 = "admin123";
        String encrypted2 = PasswordEncription.encriptPassWord(password2);
        System.out.println("Contraseña encriptada: " + encrypted2);
    }

    @Test
    public void testJSon() throws ListException {
        CircularLinkedList userList= FileReader.loadUsers();

        if(userList.isEmpty()){
            System.out.println("No se han agregado usuarios al documento");

        }else {
            System.out.println("Usuarios agregados:");

            Object current = userList.getFirst();
            Object start = current;
            do {
                System.out.println((User) current);
                current = userList.getNext();
            } while (current != start);
        }
    }
}