package ucr.lab.domain;

import ucr.lab.utility.Util;
import java.util.ArrayList;
import java.util.List;

public class PriorityLinkedQueue implements Queue {

    private Node front, rear;
    private int counter;

    public PriorityLinkedQueue() {
        front = rear = null;
        counter = 0;
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public void clear() {
        front = rear = null;
        counter = 0;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int indexOf(Object element) throws QueueException {
        if (isEmpty()) throw new QueueException("Queue is empty");

        PriorityLinkedQueue aux = new PriorityLinkedQueue();
        int index = -1, i = 0;

        while (!isEmpty()) {
            Object current = front();
            if (Util.compare(current, element) == 0 && index == -1) {
                index = i;
            }
            aux.enQueue(deQueue());
            i++;
        }

        restore(aux);
        return index;
    }

    @Override
    public void enQueue(Object element) throws QueueException {
        Node newNode = new Node(element);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        counter++;
    }


    public void enQueue(Object element, String priority) throws QueueException {
        // Verificamos si la prioridad es nula o no válida
        if (priority == null || (!priority.equals("High") && !priority.equals("Medium") && !priority.equals("Low"))) {
            throw new QueueException("Priority must be one of: High, Medium, Low");
        }

        // Creamos el nuevo nodo con el elemento y la prioridad
        Node newNode = new Node(element, priority);

        // Si la cola está vacía, el nuevo nodo se convierte en el frente y el final
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            Node current = front;
            // Si la nueva prioridad es más alta que la del nodo en el frente, lo insertamos al principio
            if (newNode.priority.equals("High")) {
                newNode.next = front;
                front = newNode;
            } else {
                // Buscamos el lugar correcto para insertar el nuevo nodo según su prioridad
                while (current.next != null && comparePriorities(current.next.priority, newNode.priority) >= 0) {
                    current = current.next;
                }
                // Insertamos el nuevo nodo en la posición encontrada
                newNode.next = current.next;
                current.next = newNode;

                // Si se inserta al final, actualizamos el 'rear'
                if (newNode.next == null) {
                    rear = newNode;
                }
            }
        }

        // Incrementamos el contador de elementos
        counter++;
    }


    private int comparePriorities(String priority1, String priority2) {

        if (priority1.equals("High")) {
            return 1;
        } else if (priority1.equals("Medium") && !priority2.equals("High")) {
            return 1;
        } else if (priority1.equals("Low") && priority2.equals("Low")) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public Object deQueue() throws QueueException {
        if (isEmpty()) throw new QueueException("Queue is empty");

        Object data = front.data;
        front = front.next;
        if (front == null) rear = null;
        counter--;
        return data;
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if (isEmpty()) return false;

        PriorityLinkedQueue colaAux = new PriorityLinkedQueue();
        boolean found = false;

        while (!isEmpty()) {
            Object nodoAux = front();
            if (nodoAux.equals(element)) {
                found = true;
            }
            colaAux.enQueue(deQueue());
        }

        restore(colaAux);
        return found;
    }

    @Override
    public Object peek() throws QueueException {
        return front();
    }

    @Override
    public Object front() throws QueueException {
        if (isEmpty()) throw new QueueException("Queue is empty");
        return front.data;
    }

    public List<Person> getList() {
        List<Person> result = new ArrayList<>();
        Node current = front;
        while (current != null) {
            result.add((Person) current.data);
            current = current.next;
        }
        return result;
    }

    private void restore(PriorityLinkedQueue aux) throws QueueException {
        while (!aux.isEmpty()) {
            Node node = aux.front;
            enQueue(aux.deQueue(), node.priority); // conserva prioridad original
        }
    }

    public void display() {
        Node current = front;
        while (current != null) {
            System.out.println(current.data.toString());
            current = current.next;
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Queue is empty";

        String result = "Priority Queue:\n";
        Node current = front;
        while (current != null) {
            result += current.data.toString() + "\n";
            current = current.next;
        }
        return result;
    }
}
