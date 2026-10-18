package dev.kekhermes.variance.covariance;

import dev.kekhermes.variance.domaine.Document;

/** Contrat générique : une fabrique qui promet de renvoyer au moins un Document. */
public interface FabriqueDocument {

    Document creer();
}
