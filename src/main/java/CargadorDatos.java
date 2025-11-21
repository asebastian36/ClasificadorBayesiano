import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CargadorDatos {

    public List<Hongo> cargarDatos(String rutaArchivo) {
        List<Hongo> listaHongos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue; // Saltar líneas vacías

                String[] valores = linea.split(",");

                // El primer valor es la clase (columna 0)
                String clase = valores[0];

                // El resto (1 al 22) son atributos. Los copiamos a un nuevo arreglo.
                String[] atributos = new String[22];
                System.arraycopy(valores, 1, atributos, 0, 22);

                listaHongos.add(new Hongo(clase, atributos));
            }
            System.out.println("Datos cargados correctamente: " + listaHongos.size() + " registros.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        return listaHongos;
    }
}