package dev.kekhermes.variance.covariance;

import dev.kekhermes.variance.domaine.Document;
import dev.kekhermes.variance.domaine.Livre;
import dev.kekhermes.variance.domaine.Magazine;

/**
 * Les tableaux Java sont covariants : {@code Livre[]} est accepté comme {@code Document[]}.
 * Ce choix de conception est dangereux : la vérification se fait à l'exécution, pas à la compilation.
 */
public final class TableauxCovariants {

    private TableauxCovariants() {
    }

    public static String ecrireMagazineDansTableauDeLivres() {
        Document[] documents = new Livre[1];   // accepté par le compilateur
        try {
            documents[0] = new Magazine("Pif Gadget", 12);   // refusé par la JVM
            return "écriture acceptée";
        } catch (ArrayStoreException e) {
            return "ArrayStoreException : le tableau réel est Livre[], il refuse " + e.getMessage();
        }
    }
}
