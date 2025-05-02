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
        int index = -1, i = 1;

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

    public void enQueue(Object element, int priority) throws QueueException {
        Node newNode = new Node(element, priority);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            if (priority > front.priority) {
                newNode.next = front;
                front = newNode;
            } else {
                Node current = front;
                while (current.next != null && current.next.priority >= priority) {
                    current = current.next;
                }
                newNode.next = current.next;
                current.next = newNode;
                if (newNode.next == null) rear = newNode;
            }
        }
        counter++;
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

        PriorityLinkedQueue aux = new PriorityLinkedQueue();
        boolean found = false;

        while (!isEmpty()) {
            Object current = front();
            if (Util.compare(current, element) == 0) {
                found = true;
            }
            aux.enQueue(deQueue());
        }

        restore(aux);
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
// trae la lista
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
            Person pe = (Person) aux.deQueue();
            enQueue(pe, pe.getPriority());
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

        StringBuilder sb = new StringBuilder("Priority Queue:\n");
        Node current = front;
        while (current != null) {
            sb.append(current.data).append("\n");
            current = current.next;
        }
        return sb.toString();
    }
}
