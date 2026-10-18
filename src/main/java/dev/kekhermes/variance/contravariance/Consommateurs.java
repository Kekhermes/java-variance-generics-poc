package dev.kekhermes.variance.contravariance;

import dev.kekhermes.variance.domaine.Livre;
import java.util.List;
import java.util.function.Consumer;

/**
 * Combinaison classique : la source est un producteur ({@code ? extends Livre}),
 * l'action est un consommateur ({@code ? super Livre}).
 * Un {@code Consumer<Document>} sait traiter n'importe quel Livre : il peut donc remplacer un {@code Consumer<Livre>}.
 */
public final class Consommateurs {

    private Consommateurs() {
    }

    public static void appliquerATous(List<? extends Livre> livres, Consumer<? super Livre> action) {
        for (Livre livre : livres) {
            action.accept(livre);
        }
    }
}
