package ucr.lab.domain;

import org.junit.jupiter.api.Test;
import ucr.lab.utility.Util;

// el metodo remove es un spoiler del examen
class ArrayQueueTest {
    @Test
    public void testArrayQueue() {
        ArrayQueue arrayQueue = new ArrayQueue(20);
        try {
            for (int i = 0; i < 15; i++)
                arrayQueue.enQueue(Util.random( 30));
            System.out.println(arrayQueue);
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPerson(){
        ArrayQueue queue= new ArrayQueue(25); // para 25 personas
        try {
            for( int i =0;i< 20;i++)
                queue.enQueue(new Person(Util.getName(), Util.getMood(), Util.random(0, 99), Util.random(3)));
            System.out.println(queue);
            System.out.println("Tamaño de la cola: " + queue.size());
            for( int i =0;i< 20;i++) {
                Person p = new Person(Util.getName());
                System.out.println(queue.contains(p)
                        ? "La lista contiene la persona " + p.getName() + " en el indice " + queue.indexOf(p)
                        : "La lista no contiene la persona " + p.getName()
                );
            }
            filterMood(queue);
            System.out.println("\n" + queue);
        } catch (QueueException e) {
                throw new RuntimeException(e);
        }
    }

    private void filterMood(ArrayQueue q1) throws QueueException {
        int size = q1.size(); // variable tamaño
        for (int i = 0; i < size; i++) {
            Person aux = (Person) q1.deQueue();
            String mood = aux.getMood().toLowerCase();
            if (!mood.equals("cheerful"))
                q1.enQueue(aux);  // restauramos
        }
    }
}

