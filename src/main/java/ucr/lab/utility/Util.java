package ucr.lab.utility;


import ucr.lab.domain.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class Util {
    private static Random random;
    private static LinkedStack stackList;
    private static LinkedQueue queueList;
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

            case "Climate":
                Character c1 = (Character) a;
                Character c2 = (Character) b;
                return c1.compareTo(c2) < 0 ? -1 : c1.compareTo(c2) > 0 ? 1 : 0;

            default:
                return 2; // Unknown
        }
    }

    public static String infixToPostfixConverter(String exp) throws StackException {
        LinkedStack stack = new LinkedStack(); // Pila para operadores
        String expPostFix = "";

        for (char c : exp.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                expPostFix += c; // Si es operando, añadir directamente al resultado
            } else if (c == '(') {
                stack.push(c); // Apilar paréntesis izquierdo
            } else if (c == ')') {
                // Desapilar hasta encontrar paréntesis izquierdo
                while (!stack.isEmpty() && (char) stack.peek() != '(')
                    expPostFix += stack.pop();
                if (!stack.isEmpty())
                    stack.pop(); // eliminar '('
            } else { // operador
                // Mientras el operador en la pila tenga mayor o igual prioridad
                while (!stack.isEmpty() && getPriority(c) <= getPriority((char) stack.peek()))
                    expPostFix += stack.pop(); // Desapilar operador
                stack.push(c); // Apilar el operador actual
            }
        }

        while (!stack.isEmpty()) // Añadir todos los operadores restantes
            expPostFix += stack.pop();

        // Si es una expresión numérica, calcular y agregar el resultado
        return  exp.matches("[0-9+\\-*/()]+")
                ? expPostFix + " = " + evaluatePostfix(expPostFix)
                : expPostFix;
    }
    // infijo a prefijo
    public static String infixToPrefixConverter(String exp) throws StackException {
        LinkedStack stack = new LinkedStack(); // Pila para operadores
        StringBuilder result = new StringBuilder(); // Resultado en prefijo
        String reversed = new StringBuilder(exp).reverse().toString();// Invertir la expresión

        // Aplicar lógica similar entendiendo ')' por '(' y viceversa.
        // Siempre insertando al principio en el resultado
        for (char c : reversed.toCharArray()) {
            if (Character.isLetterOrDigit(c))
                result.insert(0, c); // Agregar operandos directamente

            else if (c == ')')
                stack.push(c); // Apilar paréntesis

            else if (c == '(') {
                while (!stack.isEmpty() && (char) stack.peek() != ')')
                    result.insert(0, stack.pop()); // Desapilar hasta ')'

                stack.pop(); // Eliminar ')'

            } else {
                // Prioridad de operadores
                while (!stack.isEmpty() && getPriority(c) < getPriority((char) stack.peek()))
                    result.insert(0, stack.pop());
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) // Añadir operadores restantes
            result.insert(0, stack.pop());
        String prefix = result.toString();

        return exp.matches("[0-9+\\-*/()]+")
                ? prefix + " = " + evaluatePrefix(prefix)
                : prefix;
    }
    // postfijo a infijo
    public static String postfixToInfixConverter(String exp) throws StackException{
        LinkedStack stack = new LinkedStack();
        for(char c: exp.toCharArray()){
            if(Character.isLetterOrDigit(c)){
                stack.push(String.valueOf(c)); // Si es operando, se apila como cadena
            }else { // seria mi operador
                // Desapilar dos operandos
                String oper2= (String) stack.pop();
                String oper1= (String) stack.pop();
                // Combinar en notación infija con paréntesis
                String res= "(" + oper1+c+oper2+")"; // operando operador operando
                stack.push(res); // Apilar resultado parcial
            }
        }
        String result = (String) stack.pop();
        return exp.matches("[0-9+\\-*/]+") // Evaluar si es numérica
                ? result + " = " + evaluatePostfix(exp)
                : result;
    }
    // postfijo a prefijo
    public static String postfixToPrefixConverter(String exp) throws StackException {
        LinkedStack stack = new LinkedStack(); // Pila para operandos
        for (char c : exp.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                stack.push(String.valueOf(c)); // Apilar operandos
            } else {
                // Desapilar dos operandos
                String oper2 = (String) stack.pop();
                String oper1 = (String) stack.pop();
                String res = c + oper1 + oper2; // Combinar en prefijo
                stack.push(res);
            }
        }
        String result = (String) stack.pop(); // Resultado final
        return exp.matches("[0-9+\\-*/]+") // Evaluar si es numérica
                ? result + " = " + evaluatePostfix(exp)
                : result;
    }
    //prefijo a postfijo
    public static String prefixToPostfixConverter(String exp) throws StackException {
        LinkedStack stack = new LinkedStack(); // Pila para operandos
        String reversed = new StringBuilder(exp).reverse().toString();// Invertir la expresión
        for (char c : reversed.toCharArray()) {
            if (Character.isLetterOrDigit(c))
                stack.push(String.valueOf(c)); // Apilar operandos
            else {
                // Desapilar los dos operandos
                String oper1 = (String) stack.pop();
                String oper2 = (String) stack.pop();
                // Formar la expresión en postfijo: oper1 oper2 operador
                String res = oper1 + oper2 + c;
                stack.push(res); // Apilar el resultado parcial
            }
        }
        String result = (String) stack.pop(); // Resultado final
        return exp.matches("[0-9+\\-*/]+") // Evaluar si es numérica
                ? result + " = " + evaluatePostfix(exp)
                : result;
    }
// prefijo a infijo

    public static String prefixToInfixConverter(String exp) throws StackException {
        LinkedStack stack = new LinkedStack();

        String reversed= new StringBuilder(exp).reverse().toString();

        for(char c:reversed.toCharArray()){
            if(Character.isLetterOrDigit(c)){
                stack.push(String.valueOf(c));
            }else{
                String oper1= (String) stack.pop();
                String ope2= (String) stack.pop();
                String resultado= "("+ oper1+ c+ ope2+")";
                stack.push(resultado);
            }
        }
        String result= (String) stack.pop();
        return exp.matches("[0-9+\\-*/]+")
                ?result+"="+ evaluatePrefix(exp):result;
    }
    private static int getPriority(char c) {
        return switch (c) {
            case '+', '-' -> 1; //prioridad mas baja
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }

    public static int evaluatePostfix(String exp) throws StackException {
        LinkedStack stack = new LinkedStack();
        for (char c : exp.toCharArray()) {
            if (Character.isDigit(c)) {
                stack.push(c - '0');
            } else {
                int val2 = (int) stack.pop();
                int val1 = (int) stack.pop();
                switch (c) {
                    case '+': stack.push(val1 + val2); break;
                    case '-': stack.push(val1 - val2); break;
                    case '*': stack.push(val1 * val2); break;
                    case '/': stack.push(val1 / val2); break;
                }
            }
        }
        return (int) stack.pop();
    }

    public static int evaluatePrefix(String exp) throws StackException {
        LinkedStack stack = new LinkedStack();
        for (int i = exp.length() - 1; i >= 0; i--) {
            char c = exp.charAt(i);
            if (Character.isDigit(c)) {
                stack.push(c - '0');
            } else {
                int val1 = (int) stack.pop();
                int val2 = (int) stack.pop();
                switch (c) {
                    case '+': stack.push(val1 + val2); break;
                    case '-': stack.push(val1 - val2); break;
                    case '*': stack.push(val1 * val2); break;
                    case '/': stack.push(val1 / val2); break;
                }
            }
        }
        return (int) stack.pop();
    }

    public static String decimalTo(Stack stack, int base, int decimal) throws StackException {
        if (decimal == 0)
            return "0";
        int n;
        StringBuilder res = new StringBuilder();

        while (decimal > 0) {
            n = decimal%base;
            if (n >= 10)
                stack.push(Character.toString((char)(n+55)));
            else
                stack.push(n);
            decimal = decimal/base;
        }

        while (!stack.isEmpty())
            res.append(stack.pop());

        //Para bases menores que 10 el resultado debe invertirse
        return base >= 10? res.toString() : res.reverse().toString();
    }

    // Método para obtener el tipo de instancia de dos objetos
    public static String instanceOf(Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) return "Integer";
        if (a instanceof String && b instanceof String) return "String";
        if (a instanceof Character && b instanceof Character) return "Character";
        if (a instanceof Climate && b instanceof Climate) return "Climate";

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

    public static void setQueueClimate(LinkedQueue queueClimate) {
        Util.queueList = queueClimate;
    }

    public static LinkedQueue getClimateQueue() {
        if(queueList== null) {
            queueList = new LinkedQueue();
        }
        return queueList;
    }

    public static LinkedStack getStack() {
        if(stackList== null) {
            stackList = new LinkedStack();
        }
        return stackList;
    }
}

