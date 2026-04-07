package ejercicios;

import java.util.ArrayList;
import java.util.Scanner;

public class BoletaPersonalizada {
    public static void main(String[] args) {
        // Scanner para leer datos desde el teclado y variable para almacenar la opción del menú
        Scanner teclado = new Scanner(System.in);
        int opcion;

        // Listas para guardar los productos, precios y cantidades
        ArrayList<String> nombres = new ArrayList<>();
        ArrayList<Integer> precios = new ArrayList<>();
        ArrayList<Integer> cantidades = new ArrayList<>();

        System.out.println("-----------------------------");
        System.out.println("-*    Almacén Javalimos    *-");
        System.out.println("-----------------------------");

        // Agregar a la lista de productos la información
        ingresarProducto(teclado, nombres, precios, cantidades);

        do {
            mostrarMenuOpciones();
            opcion = teclado.nextInt();

            switch (opcion) {
                case 1:
                    ingresarProducto(teclado, nombres, precios, cantidades);
                    break;
                case 2:
                    System.out.println("\n\n *** Imprimiendo boleta ***");
                    imprimirBoleta(nombres, precios, cantidades);
                    break;
                default:
                    System.out.println("XXX Opción inválida");
            }

        } while (opcion != 2);

        teclado.close(); // Para liberar memoria
    }

    public static void mostrarMenuOpciones() {
        System.out.println("\n? Ingresa una opción");
        System.out.println("   1. Añadir otro producto");
        System.out.println("   2. Finalizar compra");
        System.out.print("> ");
    }

    public static void ingresarProducto(Scanner teclado, ArrayList<String> nombres, ArrayList<Integer> precios, ArrayList<Integer> cantidades) {
        System.out.println("\n+ Añadir producto:");
        System.out.print("   Nombre: ");
        nombres.add(teclado.nextLine());
        System.out.print("   Precio: $");
        precios.add(teclado.nextInt());
        System.out.print("   Cantidad: ");
        cantidades.add(teclado.nextInt());
        teclado.nextLine(); // Limpia el buffer del Scanner
        System.out.println("+ Producto agregado exitosamente");
    }

    public static void imprimirBoleta(ArrayList<String> nombres, ArrayList<Integer> precios, ArrayList<Integer> cantidades) {
        // Variable para almacenar el subtotal de la compra
        int subtotal = 0;


        System.out.println("\n\n");
        System.out.println("---------------------------------------");
        System.out.println("-*         ALMACEN JAVALIMOS         *-");
        System.out.println("-*               Boleta              *-");
        System.out.println("---------------------------------------\n");

        for (int i = 0; i < nombres.size(); i++) {
            String nombre = nombres.get(i);
            Integer precio = precios.get(i);
            Integer cantidad = cantidades.get(i);

            System.out.println("x" + cantidad + " " + nombre + " @ $" + precio);
            System.out.println(".............................$ " + (precio * cantidad));

            subtotal = subtotal + (precio * cantidad);
        }

        System.out.println("\nSubtotal:                    $ " + subtotal);
        System.out.println("IVA (19%):                   $ " + (int) (subtotal * 0.19));
        System.out.println("TOTAL:                       $ " + (int) (subtotal * 1.19));

        // Calculos
        // int total1 = precio1 * cantidad1;
        // int total2 = precio2 * cantidad2;


        // int subtotal = total1 + total2;

        // double iva = subtotal * 0.19;

        // double totalFinal = subtotal + iva;
    }
}
