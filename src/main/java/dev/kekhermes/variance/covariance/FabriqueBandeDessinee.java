package dev.kekhermes.variance.covariance;

import dev.kekhermes.variance.domaine.BandeDessinee;

/**
 * Deuxième niveau de covariance : {@code BandeDessinee} est un sous-type de {@code Livre},
 * lui-même sous-type de {@code Document}.
 */
public class FabriqueBandeDessinee extends FabriqueLivre {

    @Override
    public BandeDessinee creer() {
        return new BandeDessinee("Tintin au Tibet", "978-2-203-00101-2", "Hergé");
    }
}
