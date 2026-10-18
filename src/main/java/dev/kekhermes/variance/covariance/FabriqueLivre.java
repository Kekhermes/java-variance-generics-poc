package dev.kekhermes.variance.covariance;

import dev.kekhermes.variance.domaine.Livre;

/**
 * Covariance du type de retour : {@code Livre} est un sous-type de {@code Document},
 * donc la redéfinition peut retourner le type plus précis.
 */
public class FabriqueLivre implements FabriqueDocument {

    @Override
    public Livre creer() {
        return new Livre("Titre par défaut", "978-0-00-000000-0");
    }
}
