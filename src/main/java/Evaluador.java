import java.util.List;

public class Evaluador {

    public void evaluarModelo(ClasificadorBayesiano modelo, List<Hongo> datosPrueba) {
        int vp = 0, vn = 0, fp = 0, fn = 0;

        for (Hongo h : datosPrueba) {
            String real = h.getClase();
            // Pasamos 'false' para que NO imprima detalles por cada hongo
            String pred = modelo.clasificar(h.getAtributos(), false);

            if (real.equals("p")) {
                if (pred.equals("p")) vp++;
                else fn++;
            } else {
                if (pred.equals("e")) vn++;
                else fp++;
            }
        }

        imprimirResultados(vp, vn, fp, fn, datosPrueba.size());
    }

    private void imprimirResultados(int vp, int vn, int fp, int fn, int total) {
        double exactitud = (double) (vp + vn) / total;
        double precision = (vp + fp) == 0 ? 0 : (double) vp / (vp + fp);
        double sensibilidad = (vp + fn) == 0 ? 0 : (double) vp / (vp + fn);
        double especificidad = (vn + fp) == 0 ? 0 : (double) vn / (vn + fp);

        System.out.println("\n=== MATRIZ DE CONFUSIÓN ===");
        System.out.println("          Pred: P   Pred: E");
        System.out.printf("Real: P   %5d     %5d\n", vp, fn);
        System.out.printf("Real: E   %5d     %5d\n", fp, vn);

        System.out.println("\n=== MÉTRICAS DEL MODELO ===");
        System.out.printf("Exactitud:     %.2f%%\n", exactitud * 100);
        System.out.printf("Precisión:     %.2f%%\n", precision * 100);
        System.out.printf("Sensibilidad:  %.2f%%\n", sensibilidad * 100);
        System.out.printf("Especificidad: %.2f%%\n", especificidad * 100);

        if(fn > 0) System.out.println("ALERTA: " + fn + " hongos venenosos fueron clasificados como comestibles.");
    }
}