package ejercicios;

public class Boleta {

    public static void main(String[] args) {
        System.out.println(" == UNA BOLETA SIMPLE ==");

        //==========
        // PRODUCTOS
        //==========
        String producto1 = "Pan";
        int precio1 = 1200;
        int cantidad1 = 2;

        String producto2 = "Queso";
        int precio2 = 10000;
        int cantidad2 = 4;

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
    }
}