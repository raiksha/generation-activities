import java.util.Scanner;

public class LoopCommerce {
    // Clase con "throws InterruptedException", para poder utilizar la función "Thread.sleep();" y simular
    // tiempo de procesamiento :)
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        int opcion;
        boolean sesionActiva = true;

        int subtotal = 0;
        int productosCarrito = 0;
        String[] productosNombres = {
            "Monitor 24\"", 
            "Teclado Mecánico", 
            "Mouse Gamer", 
            "Auriculares BT", 
            "Memoria RAM 16GB", 
            "Disco SSD 1TB", 
            "Procesador i7", 
            "Placa Madre B550", 
            "Fuente 750W", 
            "Gabinete ATX"
        };
        Integer[] productosPrecios = {
            180000,  // Monitor
            85000,   // Teclado
            45000,   // Mouse
            60000,   // Auriculares
            55000,   // RAM
            90000,   // SSD
            320000,  // Procesador
            130000,  // Placa
            110000,  // Fuente
            75000    // Gabinete
        };

        System.out.println("\n------------------------------");
        System.out.println("-*        LoopCommerce      *-");
        System.out.println("-*          Javalimos       *-");
        System.out.println("------------------------------");

        do {
            System.out.println("\n-*            Menú          *-");
            System.out.println("------------------------------");
            System.out.println("1. Ver productos disponibles");
            System.out.println("2. Agregar productos al carrito");
            System.out.println("3. Ver subtotal actual");
            System.out.println("4. Realizar checkout");
            System.out.println("5. Salir");
            System.out.print("\nSeleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer de scanner

            switch ( opcion ){
                case 1 -> {
                    int i = 0;

                    System.out.println("\n------------------------------");
                    System.out.println("-*   Listado de Productos   *-");

                    for (String producto : productosNombres) {
                        System.out.println((i+1) + ". " + producto + " > $ " + productosPrecios[i]);
                        i++;
                    }

                    System.out.println("------------------------------");
                    Thread.sleep(1500);
                }
                case 2 -> {
                    int opcionAgregar = -1;

                    while (opcionAgregar != 0) {
                        System.out.print("\nAgregar un producto al carrito (ingrese 0 para salir): ");
                        opcionAgregar = scanner.nextInt();
                        scanner.nextLine(); // Limpiar buffer de scanner

                        if (opcionAgregar == 0){
                            break;
                        }
                        else if (opcionAgregar < 1 || opcionAgregar > productosNombres.length){
                            System.out.println("\n!!! Opción inválida. Intente nuevamente");
                            continue;
                        }

                        subtotal += productosPrecios[opcionAgregar - 1];
                        productosCarrito++;
                        System.out.println("\n+ " + productosNombres[opcionAgregar - 1] + " agregado con éxito al carrito");
                    }
                }
                case 3 -> {
                    System.out.println("\n> Cantidad de productos en carrito: " + productosCarrito);
                    System.out.println("> Subtotal hasta ahora: $" + subtotal);
                    Thread.sleep(1500);
                }
                case 4 -> {
                    char continuar = '0';
                    System.out.println("\n------------------------------");
                    System.out.println("-*   Realizando Check-out   *-");
                    System.out.println(">>> Validando stock");
                    Thread.sleep(1000);
                    if (subtotal == 0){
                        System.out.println("\n!!! No hay productos en el carrito. Volviendo al menú principal...");
                        Thread.sleep(1000);
                        break;
                    }
                    System.out.println("     Productos en carrito: " + productosCarrito);
                    System.out.println("     Subtotal: $" + subtotal);

                    do {
                        System.out.print(">>> Continuar con el pago? [s/n]: ");
                        continuar = Character.toLowerCase(scanner.next().charAt(0));
                        scanner.nextLine(); // Limpiar buffer de scanner
                        if (continuar != 's' && continuar != 'n'){
                            System.out.println("!!! Opción inválida. Responda 'S' para 'Sí' o 'N' para 'no'");
                        }
                    } while (continuar != 's' && continuar != 'n');

                    if (continuar == 'n') {
                        System.out.println("\nXXX Checkout cancelado por el usuario\n");
                        System.out.println("Volviendo al menú principal...");
                        Thread.sleep(3000);
                        break;
                    }

                    System.out.print("Ingrese los 4 últimos dígitos de su número de tarjeta: ");
                    int tarjeta = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    System.out.println(">>> Validando tarjeta terminada en ****" + tarjeta);
                    System.out.println("... Procesando pago");
                    Thread.sleep(1500);
                    System.out.println("... Esperando aprobación del banco");
                    Thread.sleep(1500);
                    System.out.println(">>> TRANSACCIÓN APROBADA [ID: OK-200]");
                    Thread.sleep(1000);
                    System.out.println(">>> Imprimiendo boleta...");
                    Thread.sleep(2000);

                    System.out.println("\n------------------------------");
                    System.out.println("    LoopCommerce Javalimos    ");
                    System.out.println("            Boleta            ");
                    System.out.println("-*                          *-");
                    System.out.println("  Medio de pago: Débito");
                    System.out.println("  Nro. Tarjeta: ****"+tarjeta);
                    System.out.println("  Nro de transacción: 342582");
                    System.out.println("");
                    System.out.println("  Total: $"+ subtotal);
                    System.out.println("------------------------------");
                    Thread.sleep(3000);
                    System.out.println("\n>>> Generando orden de envío");
                    System.out.println("Ingrese dirección para el envío:");
                    System.out.print("      Calle: ");
                    String dirCalle = scanner.nextLine();
                    System.out.print("      Número: ");
                    String dirNumero = scanner.nextLine();
                    System.out.print("      Comuna: ");
                    String dirComuna = scanner.nextLine();
                    System.out.println(">>> Creando la orden");
                    Thread.sleep(3000);
                    System.out.println(">>> Orden creada con éxito!");
                    Thread.sleep(1500);
                    System.out.println("----------------------------------");
                    System.out.println("-*        Orden de envío        *-");
                    System.out.println("  Dirección: "+ dirCalle + " N° " + dirNumero);
                    System.out.println("  Comuna: " + dirComuna);
                    System.out.println("  Courier: Starken");
                    System.out.println("  Código de seguimiento: A562354");
                    System.out.println("  Fecha de llegada: 08/04/2026");
                    System.out.println("----------------------------------");
                    Thread.sleep(1000);
                    System.out.println("\nGracias por comprar en Javalimos LoopCommerce!");
                    Thread.sleep(1000);
                    System.out.println("\nSaliendo del programa...");
                    Thread.sleep(1000);
                    sesionActiva = false;
                }
                case 5 -> {
                    sesionActiva = false;
                    System.out.println("\nSaliendo del programa...");
                }
                default -> System.out.println("\n!!! Opción inválida. Intente nuevamente");
            }
        } while ( sesionActiva );

        System.out.println("\nHasta pronto!");
        scanner.close();
    }
}
