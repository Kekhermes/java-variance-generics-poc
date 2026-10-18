package dev.kekhermes.variance.contravariance;

import static org.assertj.core.api.Assertions.assertThat;

import dev.kekhermes.variance.domaine.Document;
import dev.kekhermes.variance.domaine.Livre;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class ContravarianceTest {

    @Test
    void unConsommateurDeDocumentsAccepteUnLivre() {
        List<String> journal = new ArrayList<>();
        Consumer<Document> journaliser = document -> journal.add(document.decrire());

        Consommateurs.appliquerATous(List.of(new Livre("Le Petit Prince", "978-2-07-061275-8")), journaliser);

        assertThat(journal).containsExactly("Livre « Le Petit Prince » (ISBN 978-2-07-061275-8)");
    }

    @Test
    void uneDestinationSuperTypeAccepteLesLivres() {
        List<Document> documents = new ArrayList<>();
        List<Object> objets = new ArrayList<>();

        EcritureDocuments.ajouterLivres(documents, 2);
        EcritureDocuments.ajouterLivres(objets, 1);

        assertThat(documents).hasSize(2).allMatch(Livre.class::isInstance);
        assertThat(objets).hasSize(1);
    }

    @Test
    void collectionsSortFonctionneSurLivreGrâceAComparableSuper() {
        Livre zazie = new Livre("Zazie", "978-0-00-000001-0");
        Livre asterix = new Livre("Astérix", "978-0-00-000002-0");

        List<Livre> tries = Comparaisons.trier(List.of(zazie, asterix));

        assertThat(tries).extracting(Document::getTitre).containsExactly("Astérix", "Zazie");
    }
}
