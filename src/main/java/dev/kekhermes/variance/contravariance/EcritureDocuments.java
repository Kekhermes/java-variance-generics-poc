package dev.kekhermes.variance.contravariance;

import dev.kekhermes.variance.domaine.Livre;
import java.util.List;

/**
 * Consommateur : la méthode ÉCRIT des Livre dans une liste de super-types.
 * {@code List<? super Livre>} accepte {@code List<Livre>}, {@code List<Document>} et {@code List<Object>}.
 */
public final class EcritureDocuments {

    private EcritureDocuments() {
    }

    public static void ajouterLivres(List<? super Livre> cible, int nombre) {
        for (int i = 1; i <= nombre; i++) {
            cible.add(new Livre("Livre n°" + i, String.format("978-0-00-%06d-0", i)));
        }
    }
}
