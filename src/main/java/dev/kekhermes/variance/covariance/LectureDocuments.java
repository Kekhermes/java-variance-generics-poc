package dev.kekhermes.variance.covariance;

import dev.kekhermes.variance.domaine.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Producteur : la méthode LIT des Document dans une liste de sous-types.
 * Le paramètre {@code List<? extends Document>} accepte donc {@code List<Livre>},
 * {@code List<BandeDessinee>}, etc. En contrepartie, rien ne peut y être ajouté.
 */
public final class LectureDocuments {

    private LectureDocuments() {
    }

    public static List<String> titres(List<? extends Document> documents) {
        List<String> titres = new ArrayList<>();
        for (Document document : documents) {
            titres.add(document.getTitre());
        }
        return titres;
    }
}
