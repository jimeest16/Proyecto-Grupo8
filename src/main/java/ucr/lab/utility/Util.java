package ucr.lab.utility;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class Util {
    private static Random random;

    //constructor estatico, inicializador estatico
    static {
        // semilla para el random
        long seed = System.currentTimeMillis();
        random = new Random(seed);

    }

    // Método para generar un número aleatorio en un rango
    public static int Random(int min, int max) {
        // Generación de un número aleatorio en el rango [min, max]
        return 1 + random.nextInt(max - min + 1);
    }

    public static int random(int bound) {
        //return (int)Math.floor(Math.random()*bound); //forma 1
        return 1 + random.nextInt(bound);
    }

    // Método para llenar un arreglo con valores aleatorios
    public static void fillArray(int[] a, int min, int max) {
        for (int i = 0; i < a.length; i++) {
            a[i] = Random(min, max);
        }
    }

    public static void fill(int[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = random(99);
        }
    }

    // Método para formatear números largos
    public static String format(long n) {
        return new DecimalFormat("###,###,###.##").format(n);
    }

    // Método para obtener el mínimo de dos números
    public static int min(int x, int y) {
        return (x < y) ? x : y;
    }

    // Método para obtener el máximo de dos números
    public static int max(int x, int y) {
        return (x > y) ? x : y;
    }

    // Método para mostrar el contenido de un arreglo
    public static String show(int[] a) {
        String result = "";
        for (int item : a) {
            if (item == 0) break;
            result += item + " ";
        }
        return result.trim(); // Elimina el espacio extra al final
    }

    // Método para comparar dos objetos de distintos tipos
    public static int compare(Object a, Object b) {
        switch (instanceOf(a, b)) {
            case "Integer":
                Integer int1 = (Integer) a;
                Integer int2 = (Integer) b;
                return int1 < int2 ? -1 : int1 > int2 ? 1 : 0;

            case "String":
                String str1 = (String) a;
                String str2 = (String) b;
                return str1.compareTo(str2) < 0 ? -1 : str1.compareTo(str2) > 0 ? 1 : 0;

            case "Character":
                Character ch1 = (Character) a;
                Character ch2 = (Character) b;
                return ch1.compareTo(ch2) < 0 ? -1 : ch1.compareTo(ch2) > 0 ? 1 : 0;


            default:
                return 2; // Unknown
        }
    }

    // Método para obtener el tipo de instancia de dos objetos
    public static String instanceOf(Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) return "Integer";
        if (a instanceof String && b instanceof String) return "String";
        if (a instanceof Character && b instanceof Character) return "Character";


        return "Unknown";
    }

    public static int getAge(Date date) {

        LocalDate birthDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate today = LocalDate.now();

        return Period.between(birthDate, today).getYears();
    }

    public static String dateFormat(Date value) {
        return new SimpleDateFormat("dd/MM/yyyy").format(value);


    }

    public static String getPlace() {
        String[] places = {
                "San José", "Ciudad Quesada", "Paraíso", "Turrialba", "Limón", "Liberia",
                "Puntarenas", "San Ramón", "Puerto Viejo", "Volcán Irazú", "Pérez Zeledón",
                "Palmares", "Orotina", "El coco", "Ciudad Neilly", "Sixaola", "Guápiles",
                "Siquirres", "El Guarco", "Cartago", "Santa Bárbara", "Jacó", "Manuel Antonio",
                "Quepos", "Santa Cruz", "Nicoya"
        };
        return places[random(places.length-1)];
    }
    public static String getWeather(){
        String[] weathers = {
                "rainy", "thunderstorm", "sunny", "cloudy", "foggy"
        };
        return weathers[random(weathers.length-1)];
    }
}

