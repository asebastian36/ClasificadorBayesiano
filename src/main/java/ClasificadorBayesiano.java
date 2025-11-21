import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClasificadorBayesiano {

    // Conteos para P(Clase)
    private int totalEdible = 0;
    private int totalPoisonous = 0;
    private int totalRegistros = 0;

    // Conteos para P(Atributo | Clase)
    // Lista de 22 mapas (uno por columna). Cada mapa guarda: "Letra" -> Cantidad de veces vista
    private List<Map<String, Integer>> conteosEdible;
    private List<Map<String, Integer>> conteosPoisonous;

    public ClasificadorBayesiano() {
        conteosEdible = new ArrayList<>();
        conteosPoisonous = new ArrayList<>();

        // Inicializamos los mapas para las 22 columnas
        for (int i = 0; i < 22; i++) {
            conteosEdible.add(new HashMap<>());
            conteosPoisonous.add(new HashMap<>());
        }
    }

    public void entrenar(List<Hongo> datosEntrenamiento) {
        System.out.println("Entrenando modelo...");

        for (Hongo h : datosEntrenamiento) {
            totalRegistros++;
            String[] attrs = h.getAtributos();

            if (h.getClase().equals("e")) {
                totalEdible++;
                // Registrar cada atributo en las tablas de comestibles
                for (int i = 0; i < attrs.length; i++) {
                    Map<String, Integer> columnaMap = conteosEdible.get(i);
                    columnaMap.put(attrs[i], columnaMap.getOrDefault(attrs[i], 0) + 1);
                }
            } else {
                totalPoisonous++;
                // Registrar cada atributo en las tablas de venenosos
                for (int i = 0; i < attrs.length; i++) {
                    Map<String, Integer> columnaMap = conteosPoisonous.get(i);
                    columnaMap.put(attrs[i], columnaMap.getOrDefault(attrs[i], 0) + 1);
                }
            }
        }
    }

    public String predecir(String[] atributos) {
        // 1. Calcular Probabilidad A Priori: P(e) y P(p)
        double probEdible = (double) totalEdible / totalRegistros;
        double probPoisonous = (double) totalPoisonous / totalRegistros;

        // 2. Calcular Probabilidad Condicional (Likelihood): P(Atributos | Clase)
        // Multiplicamos la probabilidad de cada atributo

        for (int i = 0; i < atributos.length; i++) {
            String valorAtributo = atributos[i];

            // P(xi | Edible)
            double countE = conteosEdible.get(i).getOrDefault(valorAtributo, 0);
            // Usamos suavizado simple (si es 0, usamos un valor muy pequeño para no anular la multiplicación)
            if (countE == 0) countE = 0.001;
            probEdible *= (countE / totalEdible);

            // P(xi | Poisonous)
            double countP = conteosPoisonous.get(i).getOrDefault(valorAtributo, 0);
            if (countP == 0) countP = 0.001;
            probPoisonous *= (countP / totalPoisonous);
        }

        // 3. Retornar la clase con mayor probabilidad
        if (probPoisonous > probEdible) {
            return "p";
        } else {
            return "e";
        }
    }
}