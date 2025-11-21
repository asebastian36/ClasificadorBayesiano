import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("CLASIFICADOR BAYESIANO PARA HONGOS - INICIANDO SISTEMA");
        System.out.println("================================================================================");

        CargadorDatos cargador = new CargadorDatos();
        List<Hongo> datos = cargador.cargarDatos("agaricus-lepiota.data");

        if (datos.isEmpty()) {
            System.out.println("Error: No hay datos.");
            return;
        }

        // Mezclar datos
        Collections.shuffle(datos);

        // Dividir 70/30
        int puntoCorte = (int) (datos.size() * 0.7);
        List<Hongo> setEntrenamiento = datos.subList(0, puntoCorte);
        List<Hongo> setPrueba = datos.subList(puntoCorte, datos.size());

        // Entrenar
        ClasificadorBayesiano clasificador = new ClasificadorBayesiano();
        clasificador.entrenar(setEntrenamiento);

        // MOSTRAR TABLAS DE FRECUENCIA AUTOMÁTICAMENTE AL INICIO
        clasificador.imprimirReporteEntrenamiento();

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n\n================== MENÚ PRINCIPAL ==================");
            System.out.println("1. Clasificar un nuevo hongo (Ver fórmula paso a paso)");
            System.out.println("2. Evaluar rendimiento del modelo (Matriz de confusión)");
            System.out.println("3. Salir");
            System.out.print("Seleccione opción: ");

            int opcion = 0;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                continue;
            }

            switch (opcion) {
                case 1:
                    clasificarManual(scanner, clasificador);
                    break;
                case 2:
                    Evaluador evaluador = new Evaluador();
                    evaluador.evaluarModelo(clasificador, setPrueba);
                    break;
                case 3:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        scanner.close();
    }

    private static void clasificarManual(Scanner scanner, ClasificadorBayesiano clasificador) {
        System.out.println("\n--- CLASIFICACIÓN MANUAL ---");
        System.out.println("Ingrese los 22 atributos separados por comas (ej: x,s,n,t,p,f,c,n,k,e,e,s,s,w,w,p,w,o,p,k,s,u):");
        System.out.println("(O pegue una línea del archivo dataset quitando la primera letra)");

        String linea = scanner.nextLine();
        String[] atributos = linea.split(",");

        // Limpieza de espacios
        for(int i=0; i<atributos.length; i++) atributos[i] = atributos[i].trim();

        if (atributos.length != 22) {
            System.out.println("ERROR: Se esperaban 22 atributos, se recibieron " + atributos.length);
            return;
        }

        clasificador.predecirConDetalle(atributos);
    }
}