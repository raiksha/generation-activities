import java.util.Scanner;

public class Calculadora {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numero1;
        int numero2;

        System.out.println(" = CALCULADORA = ");

        System.out.print("Ingrese el primer numero: ");
        numero1 = scanner.nextInt();

        System.out.print("Ingrese el segundo numero: ");
        numero2 = scanner.nextInt();

        System.out.println("Suma: " + sumar(numero1,numero2));

        System.out.println("Resta: " + restar(numero1,numero2));

        System.out.println("Multiplicacion: " + multiplicar(numero1,numero2));

        if(numero2 != 0){
            System.out.println("Division: " + dividir(numero1,numero2));
        }else{
            System.out.println("Division: no se puede dividir por cero");
        }

        scanner.close();

    }

    public static int sumar(int a, int b){
        return a + b;
    }

    public static int restar(int a, int b){
        return a - b;
    }

    public static int multiplicar(int a, int b){
        return a * b;
    }

    public static int dividir(int a, int b){
        return a / b;
    }

}