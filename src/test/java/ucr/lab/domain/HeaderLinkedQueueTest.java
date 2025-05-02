package ucr.lab.domain;

import org.junit.jupiter.api.Test;
import ucr.lab.utility.Util;

import java.util.Objects;


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
    @Test
    void test2() throws QueueException {
        HeaderLinkedQueue q1 = new HeaderLinkedQueue();
        HeaderLinkedQueue q2 = new HeaderLinkedQueue();
        HeaderLinkedQueue q3 = new HeaderLinkedQueue();

        for (int i = 0; i < 20; i++) {
            try {
                q1.enQueue(new Climate(new Place(Util.getPlace()),new Weather(Util.getWeather())));
            } catch (QueueException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(q1);

        //Desencole todos los objetos con estados del tiempo “sunny” y “foggy” de q1 y encólelos en q2

        System.out.println(q1);
        filterWeather(q1,q2);
        System.out.println(q2);
    }

    private void filterWeather(HeaderLinkedQueue q1, HeaderLinkedQueue q2) throws QueueException {
            ///Desencole todos los objetos con estados del tiempo “sunny” y “foggy” de q1 y encólelos en q2
            // cola original: q1 cola auxiliar: q2
            //Declarar dos objetos weather por buscar

            // si la cola no está vacía entonces añade

                //dequeu:descola la queue
            int size = q1.size(); // variable tamaño
                for (int i = 0; i < size; i++) {

                Climate aux = (Climate) q1.deQueue();
                String weather = aux.getWeather().getWeatherDescription().toLowerCase();
                    if (weather.equals("sunny") || weather.equals("foggy")) { // si el elemento es igual al aux entonces lo encontró
                        q2.enQueue(aux); // lo encola en la cola aux
                    }
                    q1.enQueue(aux);  // restauramos q1
                }
            }
        }




