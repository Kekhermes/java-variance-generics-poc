package dev.kekhermes.variance.covariance;

import static org.assertj.core.api.Assertions.assertThat;

import dev.kekhermes.variance.domaine.BandeDessinee;
import dev.kekhermes.variance.domaine.Document;
import java.util.List;
import org.junit.jupiter.api.Test;

class CovarianceTest {

    @Test
    void lesTableauxSontCovariantsMaisLaJvmVerifieLesEcritures() {
        String resultat = TableauxCovariants.ecrireMagazineDansTableauDeLivres();

        assertThat(resultat).startsWith("ArrayStoreException").contains("Magazine");
    }

    @Test
    void untypeDeRetourCovariantDonneLeTypeLePlusPrecis() {
        BandeDessinee bande = new FabriqueBandeDessinee().creer();

        assertThat(bande.getDessinateur()).isEqualTo("Hergé");
    }

    @Test
    void lInterfaceNeVoitQueDocumentMaisLObjetEstUneBandeDessinee() {
        FabriqueDocument fabrique = new FabriqueBandeDessinee();

        Document document = fabrique.creer();

        assertThat(document).isInstanceOf(BandeDessinee.class);
    }

    @Test
    void unProducteurLitUneListeDeSousTypes() {
        List<BandeDessinee> bandes = List.of(new BandeDessinee("Tintin au Tibet", "978-2-203-00101-2", "Hergé"));

        assertThat(LectureDocuments.titres(bandes)).containsExactly("Tintin au Tibet");
    }
}
