import java.util.Scanner;

public class Cine {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        double precioBase;
        int edad;
        double porcentajeDescuento = 0;
        double montoDescuento = 0;
        double precioFinal = 0;
        String categoria = "";

        System.out.println("\n------------------------------");
        System.out.println("-*     Sistema de Ventas    *-");
        System.out.println("-*      Cine Javalimos      *-");
        System.out.println("------------------------------");

        System.out.print("\nIngrese el precio base de la\nentrada: $");
        precioBase = scanner.nextDouble();

        System.out.print("Ingrese la edad: ");
        edad = scanner.nextInt();

        //Validaciones:
        if (precioBase <= 0) {
            System.out.println("\n!!! El precio debe ser mayor a 0");
            System.exit(0);
        }
        else if (edad < 0) {
            System.out.println("\n!!! La edad no puede ser negativa");
            System.exit(0);
        }
        
        // Asignar descuentos según edad
        if (edad <= 5) {
            porcentajeDescuento = 50;
            categoria = "Niño";
        }
        else if (edad <= 17) {
            porcentajeDescuento = 25;
            categoria = "Menor de edad";
        }
        else if (edad <= 65) {
            porcentajeDescuento = 0;
            categoria = "Adulto";
        }
        else {
            porcentajeDescuento = 50;
            categoria = "Adulto Mayor";
        }

        // Cálculos de descuento
        montoDescuento = precioBase * (porcentajeDescuento / 100);
        precioFinal = precioBase - montoDescuento;

        System.out.println("\n------------------------------");
        System.out.println("            Boleta            ");
        System.out.println("-*                          *-");
        System.out.printf("  Categoría: %s", categoria);
        System.out.printf("\n  Edad: %d años%n", edad);

        System.out.printf("\n  Categoría: %s", categoria);
        System.out.printf("\n  Precio base: $%.0f", precioBase);
        System.out.printf("\n  Porcentaje descuento: %.0f%%", porcentajeDescuento);
        System.out.printf("\n  Descuento: $%.0f", montoDescuento);
        System.out.printf("\n  Precio final: $%.0f", precioFinal);
        System.out.println("\n------------------------------");

        scanner.close();
    }

}
