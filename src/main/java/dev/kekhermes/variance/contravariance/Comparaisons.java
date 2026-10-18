package dev.kekhermes.variance.contravariance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Tri générique avec la borne {@code T extends Comparable<? super T>}.
 * Pour {@code T = Livre} : {@code Livre} implémente {@code Comparable<Document>}, et {@code Document}
 * est un super-type de {@code Livre}. La borne est donc satisfaite grâce à {@code ? super}.
 */
public final class Comparaisons {

    private Comparaisons() {
    }

    public static <T extends Comparable<? super T>> List<T> trier(Collection<? extends T> elements) {
        List<T> copie = new ArrayList<>(elements);
        Collections.sort(copie);
        return copie;
    }
}
