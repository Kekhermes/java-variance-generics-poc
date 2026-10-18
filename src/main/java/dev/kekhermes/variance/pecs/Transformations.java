package dev.kekhermes.variance.pecs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Règle PECS (Producer Extends, Consumer Super) appliquée à des signatures réelles.
 */
public final class Transformations {

    private Transformations() {
    }

    /**
     * Même signature que {@code java.util.Collections.copy} :
     * la source PRODUIT des T (extends), la destination CONSOMME des T (super).
     */
    public static <T> void copier(List<? extends T> source, List<? super T> destination) {
        for (T element : source) {
            destination.add(element);
        }
    }

    /**
     * La fonction CONSOMME des T ({@code ? super T}) et PRODUIT des R ({@code ? extends R}).
     * Une {@code Function<Document, String>} peut donc transformer une liste de Livre.
     */
    public static <T, R> List<R> transformer(List<? extends T> source, Function<? super T, ? extends R> fonction) {
        List<R> resultat = new ArrayList<>();
        for (T element : source) {
            resultat.add(fonction.apply(element));
        }
        return resultat;
    }
}
