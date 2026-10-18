package dev.kekhermes.variance.domaine;

/** Autre sous-type de {@link Document}, sans ISBN : un numéro de périodique. */
public class Magazine extends Document {

    private final int numero;

    public Magazine(String titre, int numero) {
        super(titre);
        if (numero <= 0) {
            throw new IllegalArgumentException("numero doit être strictement positif");
        }
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public String decrire() {
        return "Magazine « " + getTitre() + " » n°" + numero;
    }
}
