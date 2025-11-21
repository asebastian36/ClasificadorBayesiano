import java.util.List;

public class Evaluador {

    public void evaluarModelo(ClasificadorBayesiano modelo, List<Hongo> datosPrueba) {
        int vp = 0; // Real p, Predicho p
        int vn = 0; // Real e, Predicho e
        int fp = 0; // Real e, Predicho p
        int fn = 0; // Real p, Predicho e

        System.out.println("\n====================================================================================================");
        System.out.println("EVALUACIÓN COMPLETA DEL MODELO BAYESIANO");
        System.out.println("====================================================================================================");
        System.out.println("🧪 Evaluando " + datosPrueba.size() + " instancias de prueba...");

        for (Hongo real : datosPrueba) {
            String prediccion = modelo.predecir(real.getAtributos());
            String claseReal = real.getClase();

            if (claseReal.equals("p")) {
                if (prediccion.equals("p")) vp++;
                else fn++;
            } else {
                if (prediccion.equals("e")) vn++;
                else fp++;
            }
        }

        // Cálculos
        double total = vp + vn + fp + fn;
        double exactitud = (vp + vn) / total;
        double precision = (vp + fp) == 0 ? 0 : (double) vp / (vp + fp);
        double sensibilidad = (vp + fn) == 0 ? 0 : (double) vp / (vp + fn);
        double especificidad = (vn + fp) == 0 ? 0 : (double) vn / (vn + fp);

        // Imprimir Matriz Gráfica
        System.out.println("\n********************************** MATRIZ DE CONFUSIÓN DETALLADA ***********************************");
        System.out.println("\n                 PREDICCIÓN DEL MODELO");
        System.out.println("                ╔═════════════════════════════════════════════╗");
        System.out.println("                ║             p             e                 ║");
        System.out.println("╔════════════════════╬═════════════════════════════════════════════╣");
        System.out.printf("║REALIDAD p          ║   VP = %5d       │   FN = %5d        ║\n", vp, fn);
        System.out.println("║                    ║                             │                         ║");
        System.out.printf("║REALIDAD e          ║   FP = %5d       │   VN = %5d        ║\n", fp, vn);
        System.out.println("╚════════════════════╩═════════════════════════════════════════════╝");

        System.out.println("\nEXPLICACIÓN:");
        System.out.println("   VP: Venenosos detectados correctamente.");
        System.out.println("   VN: Comestibles detectados correctamente.");
        System.out.println("   FP: Comestibles confundidos como venenosos (Falsa alarma).");
        System.out.println("   FN: Venenosos confundidos como comestibles (¡PELIGRO MORTAL!).");

        System.out.println("\n******************************** MÉTRICAS DE EVALUACIÓN DETALLADAS *********************************");

        System.out.printf("EXACTITUD (Accuracy)    = %.6f (%.2f%%)\n", exactitud, exactitud*100);
        System.out.println("   Fórmula: (VP + VN) / Total");

        System.out.printf("PRECISIÓN (Precision)   = %.6f (%.2f%%)\n", precision, precision*100);
        System.out.println("   Fórmula: VP / (VP + FP) -> Confiabilidad cuando dice 'venenoso'");

        System.out.printf("🔍 SENSIBILIDAD (Recall)= %.6f (%.2f%%)\n", sensibilidad, sensibilidad*100);
        System.out.println("   Fórmula: VP / (VP + FN) -> Capacidad de encontrar todo el veneno");

        System.out.printf("ESPECIFICIDAD           = %.6f (%.2f%%)\n", especificidad, especificidad*100);
        System.out.println("   Fórmula: VN / (VN + FP)");

        System.out.println("\n*************************************** CONCLUSIÓN ****************************************");
        if (exactitud > 0.9) {
            System.out.println("RESULTADO: **EXCELENTE**");
            System.out.println("El clasificador Bayesiano es una BUENA metodología para este dataset.");
        } else {
            System.out.println("RESULTADO: REGULAR");
        }

        System.out.println("\n*** ANÁLISIS DE SEGURIDAD ***");
        if (fn > 0) {
            System.out.println("PELIGRO: Se encontraron " + fn + " hongos venenosos clasificados como comestibles.");
            System.out.println("No se recomienda confiar ciegamente para consumo humano sin revisión experta.");
        } else {
            System.out.println("SEGURIDAD ALTA: El modelo no dejó pasar ningún hongo venenoso en esta prueba.");
        }
    }
}