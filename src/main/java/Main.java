import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CargadorDatos cargador = new CargadorDatos();
        // Asegúrate de que el archivo esté en la carpeta raíz del proyecto
        List<Hongo> datos = cargador.cargarDatos("agaricus-lepiota.data");

        if (datos.isEmpty()) {
            System.out.println("No se encontraron datos. Verifica el archivo.");
            return;
        }

        // Barajamos los datos para que el entrenamiento sea aleatorio
        Collections.shuffle(datos);

        // Dividir datos: 70% entrenamiento, 30% prueba
        int puntoCorte = (int) (datos.size() * 0.7);
        List<Hongo> setEntrenamiento = datos.subList(0, puntoCorte);
        List<Hongo> setPrueba = datos.subList(puntoCorte, datos.size());

        // Entrenar modelo
        ClasificadorBayesiano clasificador = new ClasificadorBayesiano();
        clasificador.entrenar(setEntrenamiento);

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- SISTEMA DE CLASIFICACIÓN DE HONGOS (NAIVE BAYES) ---");
            System.out.println("1. Ingresar un hongo manualmente");
            System.out.println("2. Evaluar desempeño del algoritmo (Dataset de prueba)");
            System.out.println("3. Salir");
            System.out.print("Elige una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            switch (opcion) {
                case 1:
                    predecirManual(scanner, clasificador);
                    break;
                case 2:
                    Evaluador evaluador = new Evaluador();
                    evaluador.evaluarModelo(clasificador, setPrueba);
                    break;
                case 3:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }

    private static void predecirManual(Scanner scanner, ClasificadorBayesiano clasificador) {
        System.out.println("Ingresa los 22 atributos separados por coma (ej: x,s,n,t,p,f,c,n,k,e,e,s,s,w,w,p,w,o,p,k,s,u):");
        System.out.println("O copia y pega una línea del archivo (sin la primera letra de la clase):");
        String entrada = scanner.nextLine();
        String[] atributos = entrada.split(",");

        if (atributos.length != 22) {
            System.out.println("ERROR: Debes ingresar exactamente 22 atributos. Ingresaste: " + atributos.length);
            return;
        }

        // Limpiar espacios en blanco por si acaso
        for(int i=0; i<atributos.length; i++) atributos[i] = atributos[i].trim();

        String resultado = clasificador.predecir(atributos);
        String nombre = resultado.equals("p") ? "VENENOSO (Poisonous)" : "COMESTIBLE (Edible)";
        System.out.println(">>> Predicción del sistema: " + nombre);
    }
}