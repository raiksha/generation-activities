public class Variables {

    public static void main(String[] args) {

        // NUMEROS ENTEROS

        // ln nos da un salto de linea despues del string
        // \n tambien es un salto de linea antes del string
        System.out.println("\nTipos de Entero");

        //Variable Primitivo Declarada e Inicializada
        byte b = 127;
        short s = 32000;
        int i = 1000000;
        long l = 10000000000L;

        //Los nombres de las variables no son las correctas segun nuestra convension de dev

        System.out.println("Byte (8 bits): " + b);
        System.out.println("Short (16 bits): " + s);
        System.out.println("Int (32 bits): " + i);
        System.out.println("Long (64 bits): " + l);

        System.out.println("\n-- Todos representan numeros enteros --");


        // DECIMAL
        System.out.println("\nTipos Decimal");

        float f = 3.14f;
        double d = 3.1415926535;

        System.out.println("Float (32 bits): " + f);
        System.out.println("Double (64 bits): " + d);

        System.out.println("\n-- Double es mas preciso que float --");


        // CHAR
        System.out.println("\nTipos Char");

        char letra = 'A';

        System.out.println("Char: " + letra);
        System.out.println("Representa un solo caracter");


        // BOOLEAN
        System.out.println("\nTipos BOOLEAN");

        boolean activo = true;

        System.out.println("Boolean: " + activo);
        System.out.println("Solo puede ser true o false");

        // WRAPPER CLASSES
        System.out.println("\nWRAPPER CLASSES");

        Integer wi = 100;
        Double wd = 3.14;
        Character wc = 'A';
        Boolean wb = true;

        System.out.println("Integer: " + wi);
        System.out.println("Double: " + wd);
        System.out.println("Character: " + wc);
        System.out.println("Boolean: " + wb);

        System.out.println("Son versiones de Objetos de los tipos primitivos");

        // Comparacion
        System.out.println(" Diferencia Clave");

        int x = 10;
        Integer y = 10;

        System.out.println("int x = " + x);
        System.out.println("Integer y = " + y);
        System.out.println(" int = valor simple");
        System.out.println(" Integer = objeto con metodos");
        
        // FIN 

    }

}
