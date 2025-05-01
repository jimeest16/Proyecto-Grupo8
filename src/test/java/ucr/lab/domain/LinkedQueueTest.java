package ucr.lab.domain;
import ucr.lab.utility.Util;

import org.junit.jupiter.api.Test;

public class LinkedQueueTest {
    @Test
    public void testEnqueue() {
        LinkedQueue linkedQueue = new LinkedQueue();
        try {
        for (int i = 0; i < 15; i++)
                linkedQueue.enQueue(Util.random(30));
        System.out.println(linkedQueue);
            } catch (QueueException e) {
                throw new RuntimeException(e);
            }
    }

    public boolean isBalanced(String expression) {
        LinkedQueue linkedQueue = new LinkedQueue();
        int contador = 0;

        try {

            for (int i = 0; i < expression.length(); i++) {
                linkedQueue.enQueue(expression.charAt(i));
            }


            LinkedQueue aux = new LinkedQueue();

            while (!linkedQueue.isEmpty()) {
                char ch = (char) linkedQueue.deQueue();
                aux.enQueue(ch); // guardar en auxiliar

                if (ch == '(') {
                    contador++;
                } else if (ch == ')') {
                    contador--;
                    if (contador < 0) return false; // un paréntesis cierra antes de abrir
                }
            }

            // restaurar la cola original
            while (!aux.isEmpty()) {
                linkedQueue.enQueue(aux.deQueue());
            }

        } catch (QueueException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return contador == 0;
    }

    @Test
    public void testIsBalanced() throws QueueException {
       try{
        LinkedQueue linkedQueue = new LinkedQueue();
        String[] array = {"(())()", "(()", "())(", "", "((()))", "(()(()))"};

        for (String parentesis : array) {
            boolean result = isBalanced(parentesis);
            System.out.println("Expression: \"" + parentesis + "\" → " + (result ? "is balanced" : "is not balanced"));
        }
           System.out.println();

           System.out.println("EXERSICE DUPLICATES:");
        for (int i = 0; i < 20; i++)
            linkedQueue.enQueue(Util.random(20));

           System.out.println("___________________________________");
        System.out.println("With duplicates  in " + linkedQueue);
        removeDuplicates(linkedQueue);

           System.out.println("___________________________________");
        System.out.println("Without duplicates in " + linkedQueue);
    } catch (QueueException e) {
        throw new RuntimeException(e);
    }

}


    private void removeDuplicates(LinkedQueue linkedQueue) throws QueueException {
        // remueve los duplicados

        // definimos una cola auxiliar
        LinkedQueue colaAux = new LinkedQueue();

        // si la cola no está vacía entonces añade
        // booleano es falso
        while (!linkedQueue.isEmpty()) {
            //dequeu:descola la queue
            int aux = (int) linkedQueue.deQueue();
            boolean dupplicados = false;

            // variable tamaño lo hice para no tener que poner
            int tamaño = colaAux.size();
            for (int i = 0; i < tamaño; i++) {
                int element = (int) colaAux.deQueue(); // desencola la aux
                if (element == aux) { // si el elemento es igual al aux entonces lo encontró
                    dupplicados = true;
                }
                colaAux.enQueue(element); // lo encola en la cola aux
            }
            if (!dupplicados) { // si está duplicado entonces lo encola
                colaAux.enQueue(aux); // toda la cola de los elementos
            }


        }
        while (!colaAux.isEmpty()) { // mientras la pila no esta vacía
            linkedQueue.enQueue(colaAux.deQueue());
        }
    }
}


