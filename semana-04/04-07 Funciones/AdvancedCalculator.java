import java.util.Scanner;

public class AdvancedCalculator {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        int opcion;
        double a = 0;
        double b = 0; 
        boolean sesionActiva = true;

        mostrarBienvenida();

        do {
            mostrarMenu();

            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer de scanner

            switch ( opcion ) {
                case 1 -> { // Sumar
                    System.out.print("\nPrimer sumando: ");
                    a = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    System.out.print("Segundo sumando: ");
                    b = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    double resultado = sumar(a, b);
                    System.out.printf("\n> %.2f + %.2f = %.2f", a, b, resultado);
                    Thread.sleep(2000);
                }
                case 2 -> { // Restar
                    System.out.print("\nMinuendo: ");
                    a = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    System.out.print("Sustraendo: ");
                    b = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner  

                    double resultado = restar(a, b);
                    System.out.printf("\n> %.2f - %.2f = %.2f", a, b, resultado);
                    Thread.sleep(2000);
                }
                case 3 -> { // Multiplicar
                    System.out.print("\nMultiplicando: ");
                    a = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    System.out.print("Multiplicador: ");
                    b = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    double resultado = multiplicar(a, b);
                    System.out.printf("\n> %.2f * %.2f = %.2f", a, b, resultado);
                    Thread.sleep(2000);
                }
                case 4 -> { // Dividir
                    System.out.print("\nDividendo: ");
                    a = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    System.out.print("Divisor: ");
                    b = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    if (b == 0)
                    {
                        System.out.println("\n!!! No se puede dividir por 0. Volviendo al menú...");
                        break;
                    }

                    double resultado = dividir(a, b);
                    System.out.printf("\n> %.2f / %.2f = %.2f", a, b, resultado);
                    Thread.sleep(2000);
                }
                case 5 -> { // Potencia
                    System.out.print("\nBase: ");
                    a = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    System.out.print("Exponente: ");
                    b = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    double resultado = potencia(a, b);
                    System.out.printf("\n> %.2f ^ %.2f = %.2f", a, b, resultado);
                    Thread.sleep(2000);
                }
                case 6 -> { // Raíz
                    System.out.print("\nRadicando: ");
                    a = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    if (a < 0)
                    {
                        System.out.println("\n!!! No se puede calcular la raíz de un número negativo. Volviendo al menú...");
                        break;
                    }
                    double resultado = raizCuadrada(a);
                    System.out.printf("\nRaíz de %.2f = %.2f", a, b, resultado);
                    Thread.sleep(2000);
                }
                case 7 -> { // Módulo
                    System.out.print("\nDividendo: ");
                    a = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    System.out.print("Divisor: ");
                    b = scanner.nextDouble();
                    scanner.nextLine(); // Limpiar buffer de scanner

                    if (b == 0)
                    {
                        System.out.println("\n!!! No se puede dividir por 0. Volviendo al menú...");
                        break;
                    }
                    double resultado = modulo(a, b);
                    System.out.printf("\n%.2f % %.2f = %.2f", a, b, resultado);
                    Thread.sleep(2000);
                }
                case 8 -> { // Salir
                    sesionActiva = false;
                    System.out.println("\nSaliendo del programa...");
                    Thread.sleep(700);
                }
                default -> {
                    System.out.println("\n!!! Opción inválida. Intente nuevamente");
                    Thread.sleep(2000);
                }
            }

        }
        while (sesionActiva);

        mostrarDespedida();
        scanner.close();
    }

    static void mostrarBienvenida()
    {
        System.out.println("\n------------------------------");
        System.out.println("-*         Calculadora      *-");
        System.out.println("-*          Javalimos       *-");
        System.out.println("------------------------------");
    }

    static void mostrarMenu()
    {
        System.out.println("\n-*            Menú          *-");
        System.out.println("------------------------------");
        System.out.println("1. Sumar");
        System.out.println("2. Restar");
        System.out.println("3. Multiplicar");
        System.out.println("4. Dividir");
        System.out.println("5. Potencia");
        System.out.println("6. Raíz cuadrada");
        System.out.println("7. Módulo/resto");
        System.out.println("8. Salir");
        System.out.print("\nSeleccione una opción: ");
    }

    static void mostrarDespedida()
    {
        System.out.println("\nHasta pronto!");
    }

    static double sumar(double a, double b)
    {
        return a + b;
    }

    static double restar(double a, double b)
    {
        return a - b;
    }

    static double multiplicar(double a, double b)
    {
        return a * b;
    }

    static double dividir(double a, double b)
    {
        return a / b;
    }

    static double potencia(double base, double exponente)
    {
        double resultado = 1;
        boolean exponenteNegativo = false;
        
        //Exponente 0
        if (exponente == 0) { return resultado; }

        // Valor absoluto del exponente en caso de exponente negativo
        if (exponente < 0) {
            exponenteNegativo = true;
            exponente = exponente * -1; 
        }

        // Cálculo de potencia
        for (int i = 0; i < exponente; i++)
        {
            resultado = resultado * base;
        }

        // Devolver resultado acorde si exponente positivo o negativo
        if (exponenteNegativo) { return 1 / resultado; }
        return resultado;
    }


    static double raizCuadrada(double numero)
    {
        return Math.sqrt(numero);
    }

    static double modulo(double a, double b)
    {
        return a % b;
    }
}
