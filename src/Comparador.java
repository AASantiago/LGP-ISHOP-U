import java.util.Comparator;

public class Comparador implements Comparator<Solucion> {
    @Override
    public int compare(Solucion o1, Solucion o2) {
        return o1.objetivo1 < o2.objetivo1? -1 : o1.objetivo1 == o2.objetivo1 ? 0 : 1;
    }
}
