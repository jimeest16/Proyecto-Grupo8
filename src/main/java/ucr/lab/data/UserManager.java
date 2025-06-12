package ucr.lab.data;

import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.list.CircularLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.User;
import ucr.lab.utility.PasswordEncription;

import java.io.IOException;
import java.util.List;

public class UserManager {
    private static CircularLinkedList users = new CircularLinkedList();

    private static final String filePath = "src/main/resources/data/user.json";

    public static void loadUsers() throws IOException {
        List<User> list = JsonManager.load(filePath, new TypeReference<>() {});
        users.clear();
        for (User user : list)
            users.add(user);
    }

    public static void saveUsers() throws IOException {
        List<User> list = users.toList();
        JsonManager.save(filePath, list);
    }

    public static void add(User u) throws IOException {
        u.setPassword(PasswordEncription.encriptPassWord(u.getPassword()));
        users.add(u);
        saveUsers();
    }

    public static void remove(User u) throws IOException, ListException {
        users.remove(u);
        saveUsers();
    }

    public static CircularLinkedList getUsers() {
        return users;
    }

    public static void setUsers(CircularLinkedList users) {
        UserManager.users = users;
    }
}
