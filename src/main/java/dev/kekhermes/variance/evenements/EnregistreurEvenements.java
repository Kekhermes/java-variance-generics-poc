package dev.kekhermes.variance.evenements;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Démonstration contravariante côté Spring : un listener déclaré avec un SUPER-type
 * reçoit aussi les SOUS-types publiés.
 */
@Component
public class EnregistreurEvenements {

    private final List<String> journal = new CopyOnWriteArrayList<>();

    /** Paramètre = super-type : reçoit LivreAjoute ET MagazineAjoute. */
    @EventListener
    public void surTout(EvenementDocument evenement) {
        journal.add("surTout a reçu " + evenement.getNom());
    }

    /** Paramètre = sous-type : ne reçoit que les LivreAjoute. */
    @EventListener
    public void surLivres(LivreAjoute evenement) {
        journal.add("surLivres a reçu " + evenement.getNom());
    }

    public List<String> journal() {
        return List.copyOf(journal);
    }

    public void vider() {
        journal.clear();
    }
}
