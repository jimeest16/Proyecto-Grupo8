package ucr.lab.domain;

import ucr.lab.utility.Util;

public class LinkedStack implements Stack {
    private Node top;
    private int counter; // contador de nodos en la lista

    public LinkedStack() {
        this.top = null;
        this.counter = 0;
    }

    @Override
    public void clear() {
        this.top = null;
        this.counter = 0;
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public Object top() throws StackException {
        if (isEmpty())
            throw new StackException("Linked Stack is empty");
        return top.data;
    }

    @Override
    public Object peek() throws StackException {
        return top();
    }

    @Override
    public void push(Object element) throws StackException {
        Node newNode = new Node(element);
        if (!isEmpty())
            newNode.next = top;
        top = newNode;
        counter++;
    }

    @Override
    public Object pop() throws StackException {
        if(isEmpty())
            throw new StackException("Linked Stack is empty");
        Object popped = top.data;
        top = top.next;
        counter--;
        return popped;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "Stack is empty";

        StringBuilder result = new StringBuilder("Linked Stack Content:\n");
        String instance = Util.instanceOf(top.data, top.data);
        boolean isClass = !instance.equals("Integer") && !instance.equals("Double") && !instance.equals("Character") && !instance.equals("String");

        try {
            LinkedStack aux = new LinkedStack();
            while (!isEmpty()) { //desapilar
                result.append(peek()).append(isClass? "\n" : " ");
                aux.push(pop());
            }
            while (!aux.isEmpty()) //volver a apilar
                push(aux.pop());
        } catch (StackException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }
}
