package dev.kekhermes.variance.pecs;

import static org.assertj.core.api.Assertions.assertThat;

import dev.kekhermes.variance.domaine.BandeDessinee;
import dev.kekhermes.variance.domaine.Document;
import dev.kekhermes.variance.domaine.Livre;
import dev.kekhermes.variance.domaine.Magazine;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class PecsTest {

    @Test
    void copierLitDesSousTypesEtEcritDansUnSuperType() {
        List<BandeDessinee> source = List.of(
            new BandeDessinee("Tintin", "978-2-203-00101-2", "Hergé"),
            new BandeDessinee("Astérix", "978-2-86497-000-1", "Uderzo"));
        List<Document> destination = new ArrayList<>(List.of(new Magazine("Pif", 1)));

        Transformations.copier(source, destination);

        assertThat(destination).extracting(Document::getTitre).containsExactly("Pif", "Tintin", "Astérix");
    }

    @Test
    void transformerAccepteUneFonctionDeSuperType() {
        List<Livre> livres = List.of(new Livre("Dune", "978-2-266-00000-0"));
        Function<Document, String> decrire = Document::decrire;

        List<String> resultat = Transformations.transformer(livres, decrire);

        assertThat(resultat).containsExactly("Livre « Dune » (ISBN 978-2-266-00000-0)");
    }
}
