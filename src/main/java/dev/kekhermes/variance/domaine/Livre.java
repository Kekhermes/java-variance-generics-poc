package dev.kekhermes.variance.domaine;

import java.util.Objects;

/** Sous-type de {@link Document} : une œuvre identifiée par un ISBN. */
public class Livre extends Document {

    private final String isbn;

    public Livre(String titre, String isbn) {
        super(titre);
        this.isbn = Objects.requireNonNull(isbn, "isbn");
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String decrire() {
        return "Livre « " + getTitre() + " » (ISBN " + isbn + ")";
    }
}
