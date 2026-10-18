package dev.kekhermes.variance.api;

/** Résultat pédagogique d'une démonstration exposée par l'API. */
public record Demonstration(String notion, String regle, Object resultat) {
}
