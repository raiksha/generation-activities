import java.util.Scanner;

public class Bucles {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int opcion;
        boolean juegoActivo = true; //  flag principal

        // Esto es previo, aun no lo veremos pero es necesario para el ejemplo "un Mini Arreglo"
        String[] recompensas = {
                "Espada de Madera",
                "Escudo Básico",
                "Casco de Hierro",
                "Poción de Energia"
        };

        // ==========
        // DO - WHILE
        // ==========

        // Generalmente usado en menus

        do {
            System.out.println("SISTEMA DE ENTRENAMIENTO");
            System.out.println("===== JAVALIMOS ========");
            System.out.println("1. Iniciar entrenamiento");
            System.out.println("2. Ver recompensas");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();

            switch (opcion){
                case 1 -> {
                    System.out.println("\nEntrenamiento iniciado....");

                    int energia = 100;
                    boolean personajePuedeContinuar = true; // Flag de control para el entrenamiento

                    //FOR : niveles del 1 al 5
                    // Primera columna de la izquierda, nos sirve para inicializar variable de inicio
                    // Columna central es nuestra condicion, hasta cuando va recorrer
                    // Tercera columna es nuestro contador aumentando de 1 en 1 por cada iteracion
                    for ( int nivel = 1; nivel <=5 ; nivel++){

                        if(!personajePuedeContinuar){
                            System.out.println("El personaje ya no puede continuar el entreamiento");
                            break;
                        }

                        System.out.println(" ---- Nivel ----" + nivel);
                        System.out.println("Energia inicial : " + energia);

                        //While : Para bajar la energia mientras todavia sea suficiente ante de llegar a 0
                        while ( energia > 0 && energia > 20){
                            energia -=15;
                            System.out.println("Entremiento... energia actual " + energia);
                        }

                        if (energia <= 20){
                            personajePuedeContinuar = false;
                            System.out.println("La energia es demaciado baja. Entrenamiento finalizado");
                        }else{
                            System.out.println("Nivel de entrenamiento completado con éxito");
                        }

                    }
                    
                }
                case 2 ->{
                    System.out.println(" RECOMPENSAS DISPONIBLES");

                    // EL FOR EACH : recorrer  el arreglo "recompensas" y va hacer es que busca cada elemento dentro de el
                    /*
                          String[] recompensas = {
                            "Espada de Madera",
                            "Escudo Básico",
                            "Casco de Hierro",
                            "Poción de Energia"
                    };
                     */
                    for(String nuevaVariableRecomensa: recompensas){
                        System.out.println(" - " + nuevaVariableRecomensa);
                    }
                    System.out.println();
                }

                case 3 -> {
                    juegoActivo = false;
                    System.out.println("\nSaliendo del programa");
                }

                default -> System.out.println("Opcion invalida: Intente nuevamente\n");
            }



        }while (juegoActivo);

        System.out.println("Programa Finalizado");
        scanner.close();

    }
}