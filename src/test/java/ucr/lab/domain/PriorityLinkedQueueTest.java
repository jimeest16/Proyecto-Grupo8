package ucr.lab.domain;

import org.junit.jupiter.api.Test;

class PriorityLinkedQueueTest {

    @Test
    public void test() {
        try {

            PriorityLinkedQueue priorityLinkedQueue = new PriorityLinkedQueue();
            // 3  alta
            // 2 media
            // 1 baja
            priorityLinkedQueue.enQueue("Juan", "1");
            priorityLinkedQueue.enQueue("Pedro", "2");
            priorityLinkedQueue.enQueue("Maria", "3");
            priorityLinkedQueue.enQueue("Julio", "3");
            System.out.println(priorityLinkedQueue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
