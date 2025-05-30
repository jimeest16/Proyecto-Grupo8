package ucr.lab.utility;

public class PasswordEncription {

    // Existe un desplazamiento simple de caracteres llamado cesar
    //podemos desplazar cada char las veces que queramos
    //https://eduescaperoom.com/cifrado-cesar/#:~:text=El%20cifrado%20C%C3%A9sar%20consiste%20en,la%20Z%20por%20la%20A.

    public static String encriptPassWord(String textPassWord) {

        // si no hay nada que encriptar
        if (textPassWord == null || textPassWord.isEmpty()) {
            return textPassWord;
        }
        // primero el texto lo debo convertir a caracteres para que se lea de char en char
        char[] chars = textPassWord.toCharArray();

        // for para que cuente cada char
        for (int i = 0; i < chars.length; i++) {
            // es la raiz de la logica de desplazamiento
            // juega con el codigo ascii en donde por ejemplo;
            //CADENA:
            //JIMENA
            //J=105
            //j+3=105+3=108=L
            // casteo para devolverlo a un caracter
            chars[i] = (char) (chars[i] + 3);

        }
        return new String(chars); // lo devulve a string
    }

    // lo mismo pero con -3
    public static String decriptPassWord(String textPassWord) {

        // si no hay nada que encriptar
        if (textPassWord == null || textPassWord.isEmpty()) {
            return textPassWord;
        }
        // primero el texto lo debo convertir a caracteres para que se lea de char en char
        char[] chars = textPassWord.toCharArray();

        // for para que cuente cada char
        for (int i = 0; i < chars.length; i++) {
            // es la raiz de la logica de desplazamiento
            // juega con el codigo ascii en donde por ejemplo;
            // casteo para devolverlo a un caracter
            chars[i] = (char) (chars[i] -3 );

        }
        return new String(chars); // lo devulve a string
    }
}
