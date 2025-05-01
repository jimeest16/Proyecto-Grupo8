package ucr.lab.domain;

import org.junit.jupiter.api.Test;
import ucr.lab.utility.Util;


class HeaderLinkedQueueTest {

    @Test
    public void testEnqueue() {

        HeaderLinkedQueue headerLinkedQueue = new HeaderLinkedQueue();
        try {
            for (int i = 0; i < 15; i++)
                headerLinkedQueue.enQueue(Util.random(30));

            System.out.println(headerLinkedQueue);

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }
}


