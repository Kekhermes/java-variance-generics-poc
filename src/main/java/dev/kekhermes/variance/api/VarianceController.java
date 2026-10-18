package dev.kekhermes.variance.api;

import dev.kekhermes.variance.contravariance.Comparaisons;
import dev.kekhermes.variance.contravariance.Consommateurs;
import dev.kekhermes.variance.contravariance.EcritureDocuments;
import dev.kekhermes.variance.covariance.FabriqueBandeDessinee;
import dev.kekhermes.variance.covariance.FabriqueDocument;
import dev.kekhermes.variance.covariance.LectureDocuments;
import dev.kekhermes.variance.covariance.TableauxCovariants;
import dev.kekhermes.variance.domaine.BandeDessinee;
import dev.kekhermes.variance.domaine.Document;
import dev.kekhermes.variance.domaine.Livre;
import dev.kekhermes.variance.domaine.Magazine;
import dev.kekhermes.variance.evenements.EnregistreurEvenements;
import dev.kekhermes.variance.evenements.EvenementDocument;
import dev.kekhermes.variance.evenements.LivreAjoute;
import dev.kekhermes.variance.evenements.MagazineAjoute;
import dev.kekhermes.variance.pecs.Transformations;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/variance")
public class VarianceController {

    private final ApplicationEventPublisher publicateur;
    private final EnregistreurEvenements enregistreur;

    public VarianceController(ApplicationEventPublisher publicateur, EnregistreurEvenements enregistreur) {
        this.publicateur = publicateur;
        this.enregistreur = enregistreur;
    }

    @GetMapping("/covariance")
    public List<Demonstration> covariance() {
        FabriqueDocument fabrique = new FabriqueBandeDessinee();
        Document produit = fabrique.creer();
        BandeDessinee bande = new FabriqueBandeDessinee().creer();
        return List.of(
            new Demonstration("Covariance des tableaux",
                "Livre[] est accepté comme Document[] ; la JVM vérifie chaque écriture",
                TableauxCovariants.ecrireMagazineDansTableauDeLivres()),
            new Demonstration("Covariance du type de retour",
                "FabriqueBandeDessinee.creer() renvoie BandeDessinee, sous-type de Document",
                bande.getClass().getSimpleName() + " / " + produit.getClass().getSimpleName()),
            new Demonstration("Covariance en lecture (? extends)",
                "Une List<BandeDessinee> se lit comme une List<? extends Document>",
                LectureDocuments.titres(List.of(bande))));
    }

    @GetMapping("/contravariance")
    public List<Demonstration> contravariance() {
        List<String> journal = new ArrayList<>();
        Consumer<Document> journaliser = document -> journal.add(document.decrire());
        Consommateurs.appliquerATous(List.of(new Livre("Le Petit Prince", "978-2-07-061275-8")), journaliser);

        List<Document> documents = new ArrayList<>();
        EcritureDocuments.ajouterLivres(documents, 2);
        List<Object> objets = new ArrayList<>();
        EcritureDocuments.ajouterLivres(objets, 1);

        List<String> tries = Comparaisons.trier(List.of(
                new Livre("Zazie", "978-0-00-000001-0"),
                new Livre("Astérix", "978-0-00-000002-0")))
            .stream().map(Document::getTitre).toList();

        return List.of(
            new Demonstration("Contravariance d'un consommateur (? super)",
                "Un Consumer<Document> traite un Livre : il remplace un Consumer<Livre>",
                journal),
            new Demonstration("Écriture dans ? super Livre",
                "List<Document> et List<Object> acceptent l'ajout de Livre",
                List.of("List<Document> : " + documents.size() + " élément(s)",
                        "List<Object> : " + objets.size() + " élément(s)")),
            new Demonstration("Comparable<? super T>",
                "Livre implémente Comparable<Document> : la borne de Collections.sort est satisfaite",
                tries));
    }

    @GetMapping("/pecs")
    public List<Demonstration> pecs() {
        List<BandeDessinee> bandes = List.of(new BandeDessinee("Tintin au Tibet", "978-2-203-00101-2", "Hergé"));
        List<Document> copie = new ArrayList<>(List.of(new Magazine("Pif Gadget", 12)));
        Transformations.copier(bandes, copie);
        List<String> descriptions = Transformations.transformer(bandes, Document::decrire);
        return List.of(
            new Demonstration("PECS : copier",
                "source = ? extends (on lit), destination = ? super (on écrit)",
                copie.stream().map(Document::getTitre).toList()),
            new Demonstration("PECS : transformer",
                "Function<? super T, ? extends R> : entrée contravariante, sortie covariante",
                descriptions));
    }

    @GetMapping("/spring/invariance")
    public List<Demonstration> invariance() {
        ResolvableType listeDeLivres = ResolvableType.forType(new ParameterizedTypeReference<List<Livre>>() {});
        ResolvableType listeDeDocuments = ResolvableType.forType(new ParameterizedTypeReference<List<Document>>() {});
        ResolvableType listeExtendsDocument = ResolvableType.forType(new ParameterizedTypeReference<List<? extends Document>>() {});
        ResolvableType listeSuperLivre = ResolvableType.forType(new ParameterizedTypeReference<List<? super Livre>>() {});
        return List.of(
            new Demonstration("Invariance",
                "List<Livre> n'est pas assignable à List<Document>",
                listeDeDocuments.isAssignableFrom(listeDeLivres)),
            new Demonstration("Covariance (? extends)",
                "List<Livre> est assignable à List<? extends Document>",
                listeExtendsDocument.isAssignableFrom(listeDeLivres)),
            new Demonstration("Contravariance (? super)",
                "List<Document> est assignable à List<? super Livre>",
                listeSuperLivre.isAssignableFrom(listeDeDocuments)),
            new Demonstration("Invariance (sens inverse)",
                "List<Document> n'est pas assignable à List<Livre>",
                listeDeLivres.isAssignableFrom(listeDeDocuments)));
    }

    @PostMapping("/evenements/{type}")
    public List<String> publier(@PathVariable String type) {
        EvenementDocument evenement = switch (type) {
            case "livre" -> new LivreAjoute(new Livre("Astérix le Gaulois", "978-2-86497-000-1"));
            case "bd" -> new LivreAjoute(new BandeDessinee("Astérix le Gaulois", "978-2-86497-000-1", "Uderzo"));
            case "magazine" -> new MagazineAjoute(new Magazine("Pif Gadget", 12));
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type inconnu : " + type);
        };
        publicateur.publishEvent(evenement);
        return enregistreur.journal();
    }

    @GetMapping("/evenements")
    public List<String> journal() {
        return enregistreur.journal();
    }

    @DeleteMapping("/evenements")
    public List<String> viderJournal() {
        enregistreur.vider();
        return enregistreur.journal();
    }
}
