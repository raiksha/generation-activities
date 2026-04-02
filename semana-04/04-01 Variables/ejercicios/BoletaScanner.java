package ejercicios;

import java.util.Scanner;

public class BoletaScanner {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        System.out.println(" ========================");
        System.out.println(" == ALMACEN JAVALIMOS ==");
        System.out.println(" ========================");
        System.out.println(" == UNA BOLETA SIMPLE ( CON SCANNER ) ==");

        //==========
        // PRODUCTOS1
        // ==========
        System.out.print("Ingrese nombre del producto 1: ");
        String producto1 = teclado.nextLine();

        System.out.print("Ingrese precio del producto 1: ");
        int precio1 = teclado.nextInt();

        System.out.print("Ingrese cantidad del producto 1: ");
        int cantidad1 = teclado.nextInt();


        teclado.nextLine(); // Limpia el buffer del Scanner
        //==========
        // PRODUCTOS2
        // ==========
        System.out.print("Ingrese nombre del producto 2: ");
        String producto2 = teclado.nextLine();

        System.out.print("Ingrese precio del producto 2: ");
        int precio2 = teclado.nextInt();

        System.out.print("Ingrese cantidad del producto 2: ");
        int cantidad2 = teclado.nextInt();

        // Calculos
        int total1 = precio1 * cantidad1;
        int total2 = precio2 * cantidad2;


        int subtotal = total1 + total2;

        double iva = subtotal * 0.19;

        double totalFinal = subtotal + iva;

        // IMPRESION
        System.out.println("-- DETALLE --");

        System.out.println(producto1 + " x" + cantidad1 + " = $" + total1 );
        System.out.println(producto2 + " x" + cantidad2 + " = $" + total2 );

        System.out.println("\nSubtotal: $" + subtotal);
        System.out.println("IVA (19%): $" + iva );
        System.out.println("TOTAL: $" + totalFinal);

        System.out.println("GRACIAS NO VUELV NUNCA MAS");
        
        teclado.close(); // Para liberar memoria
    }
}