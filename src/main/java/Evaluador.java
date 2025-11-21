import java.util.List;

public class Evaluador {

    public void evaluarModelo(ClasificadorBayesiano modelo, List<Hongo> datosPrueba) {
        int vp = 0; // Verdaderos Positivos (Era venenosos y predijo venenoso)
        int vn = 0; // Verdaderos Negativos (Era comestible y predijo comestible)
        int fp = 0; // Falsos Positivos (Era comestible y predijo venenoso)
        int fn = 0; // Falsos Negativos (Era venenoso y predijo comestible - ¡PELIGROSO!)

        for (Hongo real : datosPrueba) {
            String prediccion = modelo.predecir(real.getAtributos());
            String claseReal = real.getClase();

            if (claseReal.equals("p")) { // Caso Positivo: Es Venenoso
                if (prediccion.equals("p")) {
                    vp++;
                } else {
                    fn++;
                }
            } else { // Caso Negativo: Es Comestible
                if (prediccion.equals("e")) {
                    vn++;
                } else {
                    fp++;
                }
            }
        }

        // Cálculos
        double exactitud = (double) (vp + vn) / (vp + vn + fp + fn);
        // Precisión: De los que dije que eran venenosos, ¿cuántos lo eran?
        double precision = (vp + fp) == 0 ? 0 : (double) vp / (vp + fp);
        // Sensibilidad: De todos los venenosos que había, ¿cuántos detecté?
        double sensibilidad = (vp + fn) == 0 ? 0 : (double) vp / (vp + fn);

        System.out.println("---- REPORTE DE EVALUACIÓN ----");
        System.out.println("Total datos probados: " + datosPrueba.size());
        System.out.println("Verdaderos Venenosos (VP): " + vp);
        System.out.println("Falsos Venenosos (FP): " + fp);
        System.out.println("Verdaderos Comestibles (VN): " + vn);
        System.out.println("Falsos Comestibles (FN): " + fn);
        System.out.println("-------------------------------");
        System.out.printf("Exactitud (Accuracy):   %.4f  (%.2f%%)\n", exactitud, exactitud * 100);
        System.out.printf("Precisión:              %.4f  (%.2f%%)\n", precision, precision * 100);
        System.out.printf("Sensibilidad (Recall):  %.4f  (%.2f%%)\n", sensibilidad, sensibilidad * 100);

        if (exactitud > 0.9) {
            System.out.println("CONCLUSIÓN: El algoritmo Bayesiano ES una buena metodología para este set de datos.");
        } else {
            System.out.println("CONCLUSIÓN: El algoritmo Bayesiano NO tuvo un desempeño óptimo.");
        }
    }
}