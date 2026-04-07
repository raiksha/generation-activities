public class Operadores {

    public static void main(String[] args) {
        System.out.print("\nOPERADORES EN JAVA\n\n");

        int a = 10;
        int b = 3;

        // Operadores Aritmeticos

        System.out.println("a = " + a + ", b = " + b);

        System.out.println("Suma ( a + b ): " + (a + b) );
        System.out.println("Resta ( a - b ): " + (a - b) );
        System.out.println("Multiplicacion ( a * b ): " + (a * b) );
        System.out.println("Division ( a / b ): " + (a / b) ); // Division entera
        System.out.println("Modulo ( a % b ): " + (a % b) );

        System.out.println("Ojiiiiiitooo; int / int = division entera ( sin decimales)");

        // Operadores Incremento

        System.out.println(" Incremento / Decremento");

        int x = 5;

        System.out.println("Valor Inicial x : " + x); // Aqui X vale 5

        System.out.println(" x++ ( post incremento ) : " + (x = x + 1)); // le aumneto 1 a x
        // x++ si bien incrementa no se muestra, java cosas

        System.out.println("Valor Despues del Incremento : " + (x*5)); // 6

        System.out.println("++x ( pre incremento)" + (++x)) ;
        System.out.println("Despues : " + x);

    }
}
