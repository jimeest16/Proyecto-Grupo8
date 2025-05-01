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

        for( int i =0;i< 20;i++){

        }
    }
}

