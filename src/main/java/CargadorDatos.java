import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CargadorDatos {

    public List<Hongo> cargarDatos(String rutaArchivo) {
        List<Hongo> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] valores = linea.split(",");
                String clase = valores[0];
                String[] atributos = new String[22];
                System.arraycopy(valores, 1, atributos, 0, 22);
                lista.add(new Hongo(clase, atributos));
            }
            System.out.println("Dataset cargado: " + lista.size() + " instancias.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lista;
    }
}