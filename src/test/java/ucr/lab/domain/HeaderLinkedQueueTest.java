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
        filterWeather(q1,q2);
        System.out.println(q1);
        System.out.println(q2);

        //Desencole todos los objetos pertenecientes a los lugares “Paraíso” y “Liberia” de q1 y encólelos en q3
        filterPlace(q1,q3);
        System.out.println(q1);
        System.out.println(q3);

        //Desencole todos los objetos con estados del tiempo “thunderstorm” y “cloudy” de q1
        //y encólelos en q2 y q3
        filterWeather2(q2,q3);
        System.out.println(q2);
        System.out.println(q3);
    }

    private void filterWeather(HeaderLinkedQueue q1, HeaderLinkedQueue q2) throws QueueException {
        //Desencole todos los objetos con estados del tiempo “sunny” y “foggy” de q1 y encólelos en q2
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

    private void filterWeather2(HeaderLinkedQueue q1, HeaderLinkedQueue q2) throws QueueException {
        int size = q1.size(); // variable tamaño
        for (int i = 0; i < size; i++) {
            Climate aux = (Climate) q1.deQueue();
            String weather = aux.getWeather().getWeatherDescription().toLowerCase();
            if (weather.equals("thunderstorm") || weather.equals("cloudy")) { // si el elemento es igual al aux entonces lo encontró
                q2.enQueue(aux); // lo encola en la cola aux
            }
            q1.enQueue(aux);  // restauramos q1
        }
    }

    private void filterPlace(HeaderLinkedQueue q1, HeaderLinkedQueue q2) throws QueueException {
        int size = q1.size(); // variable tamaño
        for (int i = 0; i < size; i++) {
            Climate aux = (Climate) q1.deQueue();
            String place = aux.getPlace().getName().toLowerCase();
            if (place.equals("paraíso") || place.equals("liberia")) { // si el elemento es igual al aux entonces lo encontró
                q2.enQueue(aux); // lo encola en la cola aux
            }
            q1.enQueue(aux);  // restauramos q1
        }
    }
}




