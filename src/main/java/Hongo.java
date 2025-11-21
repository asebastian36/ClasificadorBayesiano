import java.util.Arrays;

public class Hongo {
    private String clase; // 'e' (edible) o 'p' (poisonous)
    private String[] atributos; // Las 22 características

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