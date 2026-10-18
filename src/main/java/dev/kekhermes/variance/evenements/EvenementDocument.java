package dev.kekhermes.variance.evenements;

import dev.kekhermes.variance.domaine.Document;
import java.util.Objects;

/** Événement métier de base. Ses sous-types sont publiés via Spring. */
public class EvenementDocument {

    private final Document document;

    public EvenementDocument(Document document) {
        this.document = Objects.requireNonNull(document, "document");
    }

    public Document getDocument() {
        return document;
    }

    public String getNom() {
        return getClass().getSimpleName();
    }
}
