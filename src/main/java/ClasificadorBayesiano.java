import java.util.*;

public class ClasificadorBayesiano {

    private int totalEdible = 0;
    private int totalPoisonous = 0;
    private int totalRegistros = 0;
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

    public void entrenar(List<Hongo> datos) {
        System.out.println("Entrenando con " + datos.size() + " instancias...");
        totalRegistros = datos.size();

        // Limpieza previa
        totalEdible = 0;
        totalPoisonous = 0;
        for (Map<String, Integer> m : conteosEdible) m.clear();
        for (Map<String, Integer> m : conteosPoisonous) m.clear();

        for (Hongo h : datos) {
            String[] attrs = h.getAtributos();
            if (h.getClase().equals("e")) {
                totalEdible++;
                actualizarConteos(conteosEdible, attrs);
            } else {
                totalPoisonous++;
                actualizarConteos(conteosPoisonous, attrs);
            }
        }
        System.out.println("Modelo entrenado.");
    }

    private void actualizarConteos(List<Map<String, Integer>> mapas, String[] atributos) {
        for (int i = 0; i < atributos.length; i++) {
            mapas.get(i).put(atributos[i], mapas.get(i).getOrDefault(atributos[i], 0) + 1);
        }
    }

    public void imprimirReporteEntrenamiento() {
        System.out.println("\n=== PROBABILIDADES A PRIORI ===");
        System.out.printf("P(e) = %.4f, P(p) = %.4f\n", (double)totalEdible/totalRegistros, (double)totalPoisonous/totalRegistros);

        System.out.println("\n=== TABLAS DE FRECUENCIA COMPLETAS ===");

        // Recorremos los 22 atributos
        for (int i = 0; i < 22; i++) {
            System.out.println("\n--- Atributo " + (i + 1) + " ---");

            // Obtenemos todas las letras posibles que aparecieron en esa columna (unión de comestibles y venenosos)
            Set<String> valoresPosibles = new HashSet<>();
            valoresPosibles.addAll(conteosEdible.get(i).keySet());
            valoresPosibles.addAll(conteosPoisonous.get(i).keySet());

            System.out.println(" Valor | P(x|p) | P(x|e)");
            System.out.println("-------|--------|--------");

            for (String val : valoresPosibles) {
                // Usamos el método auxiliar que ya tenemos para calcular la probabilidad
                double p = obtenerProbabilidad(conteosPoisonous.get(i), val, totalPoisonous);
                double e = obtenerProbabilidad(conteosEdible.get(i), val, totalEdible);

                System.out.printf("   %s   | %.4f | %.4f\n", val, p, e);
            }
        }
    }

    public String clasificar(String[] atributos, boolean mostrarDetalle) {
        double scoreP = (double) totalPoisonous / totalRegistros;
        double scoreE = (double) totalEdible / totalRegistros;

        if (mostrarDetalle) {
            System.out.println("\n=== CÁLCULO PASO A PASO ===");
            System.out.printf("Prior P(p): %.6f\n", scoreP);
            System.out.printf("Prior P(e): %.6f\n", scoreE);
        }

        for (int i = 0; i < 22; i++) {
            double p = obtenerProbabilidad(conteosPoisonous.get(i), atributos[i], totalPoisonous);
            double e = obtenerProbabilidad(conteosEdible.get(i), atributos[i], totalEdible);

            scoreP *= p;
            scoreE *= e;

            if (mostrarDetalle) {
                System.out.printf("Attr[%d]=%s -> P(x|p):%.4f, P(x|e):%.4f\n", (i+1), atributos[i], p, e);
            }
        }

        if (mostrarDetalle) {
            double total = scoreP + scoreE;
            System.out.printf("\nScore Final P: %.10f (%.2f%%)\n", scoreP, (scoreP/total)*100);
            System.out.printf("Score Final E: %.10f (%.2f%%)\n", scoreE, (scoreE/total)*100);
            String ganador = scoreP > scoreE ? "VENENOSO" : "COMESTIBLE";
            System.out.println(">>> RESULTADO: " + ganador);
        }

        return scoreP > scoreE ? "p" : "e";
    }

    private double obtenerProbabilidad(Map<String, Integer> mapa, String valor, int totalClase) {
        int count = mapa.getOrDefault(valor, 0);
        if (count == 0) return 0.0001; // Suavizado simple para evitar multiplicación por cero
        return (double) count / totalClase;
    }
}