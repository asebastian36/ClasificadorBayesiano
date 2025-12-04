import java.util.Arrays;

public class Hongo {
    private String clase;
    private String[] atributos;

    public Hongo(String clase, String[] atributos) {
        this.clase = clase;
        this.atributos = atributos;
    }

    public String getClase() {
        return clase;
    }

    public String[] getAtributos() {
        return atributos;
    }

    @Override
    public String toString() {
        return "Hongo{" + "clase=" + clase + ", atributos=" + Arrays.toString(atributos) + '}';
    }
}