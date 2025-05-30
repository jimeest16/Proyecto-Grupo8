package ucr.lab.TDA;

import ucr.lab.utility.Util;

import java.lang.reflect.Field;

public class CircularLinkedList implements List {
    private Node first;
    private Node last;
    private boolean sorted;
    private Node aux;

    public CircularLinkedList() {
        this.first = null;
        this.last = null;
        this.sorted = false;
        this.aux = null;
    }

    @Override
    public int size() throws ListException {
        if (isEmpty()) {
            return 0;
        }
        int counter = 1;
        Node aux = first.next;
        while (aux != first) {
            counter++;
            aux = aux.next;
        }
        return counter;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        sorted = false;
        aux = null;
    }

    @Override
    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public boolean contains(Object element) {
        if (isEmpty() || element == null) return false;

        Node current = first;
        do {
            if (Util.compare(current.data, element) == 0) return true;
            current = current.next;
        } while (current != first);

        return false;
    }

    @Override
    public void add(Object element) {
        addLast(element);
    }

    @Override
    public void addFirst(Object element) {
        if (element == null)
            throw new IllegalArgumentException("Element cannot be null");

        Node newNode = new Node(element);
        if (isEmpty()) {
            first = last = newNode;
            newNode.next = newNode;
        } else {
            newNode.next = first;
            first = newNode;
            last.next = first;
        }
        sorted = false;
    }

    @Override
    public void addLast(Object element) {
        if (element == null)
            throw new IllegalArgumentException("Element cannot be null");

        Node newNode = new Node(element);
        if (isEmpty()) {
            first = last = newNode;
            newNode.next = newNode;
        } else {
            last.next = newNode;
            newNode.next = first;
            last = newNode;
        }
        sorted = false;
    }

    @Override
    public void addInSortedList(Object element) throws ListException {
        if (element == null)
            throw new IllegalArgumentException("Element cannot be null");

        if (isEmpty() || Util.compare(first.data, element) > 0) {
            addFirst(element);
            sorted = true;
            return;
        }

        if (!sorted) {
            sort();
        }

        Node current = first;
        while (current.next != first && Util.compare(current.next.data, element) <= 0) {
            current = current.next;
        }

        Node newNode = new Node(element);
        newNode.next = current.next;
        current.next = newNode;

        if (current == last) {
            last = newNode;
        }
        sorted = true;
    }

    @Override
    public void remove(Object element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        Node current = first;
        Node prev = last;
        boolean found = false;

        do {
            if (Util.compare(current.data, element) == 0) {
                found = true;
                break;
            }
            prev = current;
            current = current.next;
        } while (current != first);

        if (!found) {
            throw new ListException("The list doesn't contain the element: " + element);
        }

        // If only one element in list
        if (first == last && current == first) {
            clear();
            return;
        }

        // If removing first node
        if (current == first) {
            first = first.next;
            last.next = first;
        } else {
            prev.next = current.next;
            if (current == last) {
                last = prev;
            }
        }
    }

    @Override
    public Object removeFirst() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");

        Object value = first.data;
        if (first == last) {
            clear();
        } else {
            first = first.next;
            last.next = first;
        }
        return value;
    }

    @Override
    public Object removeLast() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");

        Object value = last.data;
        if (first == last) {
            clear();
        } else {
            Node current = first;
            while (current.next != last) {
                current = current.next;
            }
            current.next = first;
            last = current;
        }
        return value;
    }

    @Override
    public void sort() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        if (sorted) return;

        boolean swapped;
        do {
            swapped = false;
            Node current = first;
            do {
                Node next = current.next;
                if (next != first && Util.compare(current.data, next.data) > 0) {
                    Object temp = current.data;
                    current.data = next.data;
                    next.data = temp;
                    swapped = true;
                }
                current = current.next;
            } while (current != last);
        } while (swapped);

        sorted = true;
    }

    public void sortByAttribute(String attributeName) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        if (attributeName == null || attributeName.isEmpty())
            throw new IllegalArgumentException("Attribute name cannot be null or empty");

        boolean swapped;
        do {
            swapped = false;
            Node current = first;
            do {
                Node next = current.next;
                if (next == first) break; // evitar comparar con el primero en la última iteración

                try {
                    Field field = current.data.getClass().getDeclaredField(attributeName);
                    field.setAccessible(true);
                    Comparable value1 = (Comparable) field.get(current.data);
                    Comparable value2 = (Comparable) field.get(next.data);
                    if (value1.compareTo(value2) > 0) {
                        Object temp = current.data;
                        current.data = next.data;
                        next.data = temp;
                        swapped = true;
                    }
                } catch (NoSuchFieldException nsfe) {
                    throw new ListException("Field not found: " + attributeName);
                } catch (ClassCastException cce) {
                    throw new ListException("Field '" + attributeName + "' is not Comparable");
                } catch (Exception e) {
                    throw new ListException("Attribute sorting failed: " + e.getMessage());
                }
                current = current.next;
            } while (current != last);
        } while (swapped);

        // No garantizamos orden luego de ordenar por atributo (depende del atributo)
        sorted = false;
    }

    @Override
    public int indexOf(Object element) throws ListException {
        if (isEmpty() || element == null) return -1;

        Node current = first;
        int index = 0;
        do {
            if (Util.compare(current.data, element) == 0)
                return index;
            current = current.next;
            index++;
        } while (current != first);

        return -1;
    }

    @Override
    public Object getFirst() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        aux = first; // inicializamos aux para iterar
        return first.data;
    }

    @Override
    public Object getLast() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        return last.data;
    }

    @Override
    public Object getPrev(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        if (element == null)
            throw new IllegalArgumentException("Element cannot be null");

        Node current = first;
        Node prev = last;
        do {
            if (Util.compare(current.data, element) == 0)
                return prev.data;
            prev = current;
            current = current.next;
        } while (current != first);

        throw new ListException("The list doesn't contain the element: " + element);
    }

    @Override
    public Object getNext(Object element) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        if (element == null)
            throw new IllegalArgumentException("Element cannot be null");

        Node current = first;
        do {
            if (Util.compare(current.data, element) == 0)
                return current.next.data;
            current = current.next;
        } while (current != first);

        throw new ListException("The list doesn't contain the element: " + element);
    }

    @Override
    public Object getNext() throws ListException {
        if (aux == null) {
            throw new ListException("Use getFirst() to initialize iteration.");
        }
        aux = aux.next;
        return aux.data;
    }

    @Override
    public Node getNode(int index) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        if (index < 0)
            throw new IllegalArgumentException("Index must be non-negative");

        Node current = first;
        int i = 0;
        do {
            if (i == index)
                return current;
            i++;
            current = current.next;
        } while (current != first);

        throw new ListException("No node at index: " + index);
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "Circular Linked List is empty";

        StringBuilder result = new StringBuilder("Circular Linked List Content:\n");
        Node current = first;
        String instance = Util.instanceOf(first.data, first.data);
        boolean isClass = !instance.equals("Integer") && !instance.equals("Double") && !instance.equals("Character") && !instance.equals("String");

        do {
            result.append(current.data);
            result.append(isClass ? "\n" : " ");
            current = current.next;
        } while (current != first);

        return result.toString();
    }
}
