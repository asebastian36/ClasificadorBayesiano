import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CargadorDatos cargador = new CargadorDatos();
        List<Hongo> datos = cargador.cargarDatos("agaricus-lepiota.data");

        if (datos.isEmpty()) return;

        Collections.shuffle(datos);
        int corte = (int) (datos.size() * 0.7);
        List<Hongo> train = datos.subList(0, corte);
        List<Hongo> test = datos.subList(corte, datos.size());

        ClasificadorBayesiano clasificador = new ClasificadorBayesiano();
        clasificador.entrenar(train);
        clasificador.imprimirReporteEntrenamiento();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Clasificar manual | 2. Evaluar | 3. Salir");
            String input = sc.nextLine();

            if (input.equals("1")) {
                clasificarManual(sc, clasificador);
            } else if (input.equals("2")) {
                new Evaluador().evaluarModelo(clasificador, test);
            } else if (input.equals("3")) {
                break;
            }
        }
        sc.close();
    }

    private static void clasificarManual(Scanner sc, ClasificadorBayesiano modelo) {
        System.out.println("Ingrese atributos (separados por coma):");
        String linea = sc.nextLine();
        String[] attrs = linea.split(",");

        if (attrs.length == 22) {
            for(int i=0; i<22; i++) attrs[i] = attrs[i].trim();
            modelo.clasificar(attrs, true);
        } else {
            System.out.println("Error: Se requieren 22 atributos.");
        }
    }
}