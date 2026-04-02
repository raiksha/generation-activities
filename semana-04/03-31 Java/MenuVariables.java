import java.util.Scanner;

public class MenuVariables {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== MENÚ DE VARIABLES ===");
            System.out.println("1. Tipos Primitivos");
            System.out.println("2. Wrapper Classes");
            System.out.println("3. Ingresar datos por teclado");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    mostrarPrimitivos();
                    break;
                case 2:
                    mostrarWrappers();
                    break;
                case 3:
                    ingresarDatos(scanner);
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }

        } while (opcion != 4);

        scanner.close();
    }

    public static void mostrarPrimitivos() {
        System.out.println("\n--- TIPOS PRIMITIVOS ---");

        byte b = 127;
        short s = 32000;
        int i = 100000;
        long l = 10000000000L;

        float f = 3.14f;
        double d = 3.1415926535;

        char c = 'A';
        boolean bool = true;

        System.out.println("byte: " + b);
        System.out.println("short: " + s);
        System.out.println("int: " + i);
        System.out.println("long: " + l);

        System.out.println("float: " + f);
        System.out.println("double: " + d);

        System.out.println("char: " + c);
        System.out.println("boolean: " + bool);

        // Explicación
        System.out.println("\nExplicación:");
        System.out.println("byte, short, int, long → números enteros");
        System.out.println("float, double → números decimales");
        System.out.println("char → un solo carácter");
        System.out.println("boolean → true o false");
    }

    public static void mostrarWrappers() {
        System.out.println("\n--- WRAPPER CLASSES ---");

        Byte b = 127;
        Short s = 32000;
        Integer i = 100000;
        Long l = 10000000000L;

        Float f = 3.14f;
        Double d = 3.1415926535;

        Character c = 'A';
        Boolean bool = true;

        System.out.println("Byte: " + b);
        System.out.println("Short: " + s);
        System.out.println("Integer: " + i);
        System.out.println("Long: " + l);

        System.out.println("Float: " + f);
        System.out.println("Double: " + d);

        System.out.println("Character: " + c);
        System.out.println("Boolean: " + bool);

        // Explicación
        System.out.println("\nExplicación:");
        System.out.println("Los Wrapper son versiones OBJETO de los tipos primitivos");
        System.out.println("Permiten usar métodos y trabajar con colecciones");
        System.out.println("Ejemplo: Integer.parseInt(\"123\")");
    }

    public static void ingresarDatos(Scanner scanner) {
        System.out.print("\nIngrese un número entero: ");
        int numero = scanner.nextInt();
        System.out.println("Número ingresado: " + numero);

        System.out.print("\nIngrese un número decimal: ");
        double decimal = scanner.nextDouble();
        System.out.println("Número decimal ingresado: " + decimal);

        System.out.print("\nIngrese un carácter: ");
        char caracter = scanner.next().charAt(0);
        System.out.println("Carácter ingresado: " + caracter);
    }
}
