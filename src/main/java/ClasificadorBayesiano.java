import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClasificadorBayesiano {

    private int totalEdible = 0;
    private int totalPoisonous = 0;
    private int totalRegistros = 0;

    // Listas de mapas para contar frecuencias por columna
    private List<Map<String, Integer>> conteosEdible;
    private List<Map<String, Integer>> conteosPoisonous;

    public ClasificadorBayesiano() {
        conteosEdible = new ArrayList<>();
        conteosPoisonous = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            conteosEdible.add(new HashMap<>());
            conteosPoisonous.add(new HashMap<>());
        }
    }

    public void entrenar(List<Hongo> datosEntrenamiento) {
        System.out.println("Entrenando con " + datosEntrenamiento.size() + " instancias...");
        System.out.println("Procesando 22 atributos...");

        // Reiniciar contadores
        totalEdible = 0;
        totalPoisonous = 0;
        totalRegistros = 0;
        for(Map<String, Integer> m : conteosEdible) m.clear();
        for(Map<String, Integer> m : conteosPoisonous) m.clear();

        int procesados = 0;
        for (Hongo h : datosEntrenamiento) {
            totalRegistros++;
            String[] attrs = h.getAtributos();

            if (h.getClase().equals("e")) {
                totalEdible++;
                for (int i = 0; i < attrs.length; i++) {
                    conteosEdible.get(i).put(attrs[i], conteosEdible.get(i).getOrDefault(attrs[i], 0) + 1);
                }
            } else {
                totalPoisonous++;
                for (int i = 0; i < attrs.length; i++) {
                    conteosPoisonous.get(i).put(attrs[i], conteosPoisonous.get(i).getOrDefault(attrs[i], 0) + 1);
                }
            }

            procesados++;
            if (procesados % 2000 == 0) {
                System.out.println("  Procesadas " + procesados + " instancias...");
            }
        }
        System.out.println("Modelo entrenado exitosamente");
    }

    // --- NUEVO: Método para imprimir el reporte de entrenamiento idéntico a la referencia ---
    public void imprimirReporteEntrenamiento() {
        System.out.println("\n==================================================");
        System.out.println("PROBABILIDADES A PRIORI P(C_k)");
        System.out.println("==================================================");
        System.out.println("C₁ = p (venenoso), C₂ = e (comestible)");
        double pP = (double) totalPoisonous / totalRegistros;
        double pE = (double) totalEdible / totalRegistros;
        System.out.printf("P(C₁) = P(p) = %d/%d = %.6f\n", totalPoisonous, totalRegistros, pP);
        System.out.printf("P(C₂) = P(e) = %d/%d = %.6f\n", totalEdible, totalRegistros, pE);

        System.out.println("\n================================================================================");
        System.out.println("TABLAS DE FRECUENCIA COMPLETAS PARA EL CÁLCULO BAYESIANO");
        System.out.println("================================================================================");

        for (int i = 0; i < 22; i++) {
            System.out.println("\nTABLA DE FRECUENCIA COMPLETA - A" + (i + 1) + " (a_" + (i + 1) + ")");
            System.out.println("+-------------------------------------------------------+");
            System.out.println("| a" + (i + 1) + "                    | C₁ (p) | C₂ (e) | Prob. C₁ | Prob. C₂ |");
            System.out.println("+-------------------------------------------------------+");

            // Obtener todos los valores únicos posibles para este atributo (unión de ambos mapas)
            Set<String> valoresUnicos = new HashSet<>();
            valoresUnicos.addAll(conteosEdible.get(i).keySet());
            valoresUnicos.addAll(conteosPoisonous.get(i).keySet());

            for (String val : valoresUnicos) {
                int countP = conteosPoisonous.get(i).getOrDefault(val, 0);
                int countE = conteosEdible.get(i).getOrDefault(val, 0);

                // Calcular prob condicional con suavizado (Laplace smoothing básico si es 0) para visualización
                double probP = (countP == 0) ? 0.0003 : (double) countP / totalPoisonous;
                double probE = (countE == 0) ? 0.0002 : (double) countE / totalEdible;

                System.out.printf("| %-20s | %-4d/%-4d | %-4d/%-4d |   %.4f |   %.4f |\n",
                        val, countP, totalPoisonous, countE, totalEdible, probP, probE);
            }
            System.out.println("+-------------------------------------------------------+");
        }
    }

    // Predicción simple para evaluación masiva
    public String predecir(String[] atributos) {
        double probEdible = Math.log((double) totalEdible / totalRegistros);
        double probPoisonous = Math.log((double) totalPoisonous / totalRegistros);

        for (int i = 0; i < atributos.length; i++) {
            String val = atributos[i];
            double countE = conteosEdible.get(i).getOrDefault(val, 0);
            if (countE == 0) countE = 0.5; // Suavizado leve
            probEdible += Math.log(countE / totalEdible);

            double countP = conteosPoisonous.get(i).getOrDefault(val, 0);
            if (countP == 0) countP = 0.5;
            probPoisonous += Math.log(countP / totalPoisonous);
        }

        return (probPoisonous > probEdible) ? "p" : "e";
    }

    // --- NUEVO: Predicción detallada paso a paso para el usuario ---
    public String predecirConDetalle(String[] atributos) {
        System.out.println("\n================================================================================");
        System.out.println("SUSTITUCIÓN COMPLETA DE LA FÓRMULA DE BAYES");
        System.out.println("================================================================================");

        double pP = (double) totalPoisonous / totalRegistros;
        double pE = (double) totalEdible / totalRegistros;

        // Usaremos arrays para guardar las probs individuales y mostrarlas
        double[] probsP = new double[22];
        double[] probsE = new double[22];

        // Cálculo de probabilidades condicionales
        for (int i = 0; i < 22; i++) {
            String val = atributos[i];

            double cP = conteosPoisonous.get(i).getOrDefault(val, 0);
            if (cP == 0) cP = 0.5; // Suavizado para evitar cero absoluto
            probsP[i] = cP / totalPoisonous;

            double cE = conteosEdible.get(i).getOrDefault(val, 0);
            if (cE == 0) cE = 0.5;
            probsE[i] = cE / totalEdible;
        }

        System.out.println("\n**************************** CÁLCULO DE NUMERADORES ****************************");
        System.out.println("\n🔹 Para C_k = 'p' (venenoso):");
        double numeradorP = pP;
        for(int i=0; i<22; i++) {
            System.out.printf("  P(a%d=%s|p) = %.6f\n", (i+1), atributos[i], probsP[i]);
            numeradorP *= probsP[i];
        }
        System.out.printf("\nNumerador_p = %.6f * Π P(a_i|p) = %.10f\n", pP, numeradorP);

        System.out.println("\n🔹 Para C_k = 'e' (comestible):");
        double numeradorE = pE;
        for(int i=0; i<22; i++) {
            System.out.printf("  P(a%d=%s|e) = %.6f\n", (i+1), atributos[i], probsE[i]);
            numeradorE *= probsE[i];
        }
        System.out.printf("\nNumerador_e = %.6f * Π P(a_i|e) = %.10f\n", pE, numeradorE);

        System.out.println("\n*************************** CÁLCULO DEL DENOMINADOR ****************************");
        double denominador = numeradorP + numeradorE;
        System.out.printf("Denominador = %.10f + %.10f = %.10f\n", numeradorP, numeradorE, denominador);

        System.out.println("\n**************************** PROBABILIDADES FINALES ****************************");
        double finalP = numeradorP / denominador;
        double finalE = numeradorE / denominador;

        System.out.printf("P(p | atributos) = %.8f (%.4f%%)\n", finalP, finalP*100);
        System.out.printf("P(e | atributos) = %.8f (%.4f%%)\n", finalE, finalE*100);

        String resultado = (finalP > finalE) ? "p" : "e";
        String etiqueta = resultado.equals("p") ? "VENENOSO (Poisonous) - ¡PELIGROSO!" : "COMESTIBLE (Edible) - Seguro";

        System.out.println("\nPREDICCIÓN FINAL: Clase = " + resultado);
        System.out.println("RECOMENDACIÓN: " + etiqueta);

        return resultado;
    }
}