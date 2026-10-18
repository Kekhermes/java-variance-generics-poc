package dev.kekhermes.variance.domaine;

import java.util.Objects;

/**
 * Super-type du domaine : tout document de la médiathèque.
 *
 * <p>Il implémente {@code Comparable<Document>}. Cette décision n'est pas anodine :
 * elle permet de comprendre plus tard pourquoi {@code Collections.sort(List<Livre>)}
 * compile grâce à {@code Comparable<? super T>}.
 */
public class Document implements Comparable<Document> {

    private final String titre;

    public Document(String titre) {
        this.titre = Objects.requireNonNull(titre, "titre");
    }

    public String getTitre() {
        return titre;
    }

    public String decrire() {
        return "Document « " + titre + " »";
    }

    @Override
    public int compareTo(Document autre) {
        return titre.compareTo(autre.titre);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + titre + ")";
    }
}
