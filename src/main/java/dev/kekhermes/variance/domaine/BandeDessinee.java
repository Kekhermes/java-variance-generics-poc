package dev.kekhermes.variance.domaine;

import java.util.Objects;

/** Sous-type de {@link Livre} : un livre illustré par un dessinateur. */
public class BandeDessinee extends Livre {

    private final String dessinateur;

    public BandeDessinee(String titre, String isbn, String dessinateur) {
        super(titre, isbn);
        this.dessinateur = Objects.requireNonNull(dessinateur, "dessinateur");
    }

    public String getDessinateur() {
        return dessinateur;
    }

    @Override
    public String decrire() {
        return "Bande dessinée « " + getTitre() + " » de " + dessinateur;
    }
}
