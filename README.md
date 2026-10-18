# java-variance-generics-poc

**POC pédagogique : covariance, contravariance, `extends` et `super` dans les génériques**, en **Java 25** et **Spring Boot 4.1.1**.

Le projet vise une compréhension précise de ces notions et une réponse claire à la question : *où et quand les appliquer ?*

Il reprend le format du POC [library-owl-shacl-poc](https://github.com/Kekhermes/library-owl-shacl-poc) : un domaine familier (la médiathèque), un guide pas à pas, et des tests qui **prouvent** les règles au lieu de les affirmer.

---

## Ce que vous allez apprendre

- **Covariance** : quand un type « sort » des sous-types, et pourquoi `Livre[]` est accepté comme `Document[]`, mais dangereux.
- **Contravariance** : quand un type « reçoit » des sous-types, et pourquoi un `Consumer<Document>` remplace un `Consumer<Livre>`.
- **Invariance** : pourquoi `List<Livre>` n'est **pas** une `List<Document>`, et ce qui se casserait si Java l'acceptait.
- **`? extends` et `? super`** : la règle **PECS** (*Producer Extends, Consumer Super*) appliquée à des signatures réelles.
- **Spring** : listeners `@EventListener` contravariants, et `ResolvableType` qui montre que Spring suit les mêmes règles.

Le guide complet est dans [`docs/guide-pedagogique.md`](docs/guide-pedagogique.md).

---

## Prérequis

- **JDK 25** (testé avec Temurin 25.0.4)
- **Maven 3.9+**
- Aucun service externe, aucune base de données.

---

## Lancer le projet

```bash
# 1. Construire et lancer toute la suite de tests (24 tests)
mvn verify

# 2. Démarrer l'API
mvn spring-boot:run

# 3. Interroger les démonstrations
curl http://localhost:8080/api/variance/covariance
curl http://localhost:8080/api/variance/contravariance
curl http://localhost:8080/api/variance/pecs
curl http://localhost:8080/api/variance/spring/invariance
curl -X POST http://localhost:8080/api/variance/evenements/livre
curl -X POST http://localhost:8080/api/variance/evenements/magazine
curl -X DELETE http://localhost:8080/api/variance/evenements
```

### Endpoints

| Méthode | Chemin | Notion démontrée |
|---|---|---|
| GET | `/api/variance/covariance` | tableaux covariants, type de retour covariant, `? extends` en lecture |
| GET | `/api/variance/contravariance` | `Consumer<? super T>`, écriture dans `? super Livre`, `Comparable<? super T>` |
| GET | `/api/variance/pecs` | signature de `copier` (PECS) et de `transformer` (`Function<? super T, ? extends R>`) |
| GET | `/api/variance/spring/invariance` | `ResolvableType` : invariance, covariance et contravariance vues par Spring |
| POST | `/api/variance/evenements/{livre\|bd\|magazine}` | listeners contravariants `@EventListener` |
| GET / DELETE | `/api/variance/evenements` | lecture et vidage du journal des événements |

---

## Parcours d'apprentissage conseillé

1. **Lire** `docs/guide-pedagogique.md`, sections 1 à 3.
2. **Lire** `covariance/` puis `CovarianceTest`.
3. **Lire** `contravariance/` puis `ContravarianceTest`.
4. **Lire** `pecs/Transformations.java` et `PecsTest`.
5. **Ouvrir** `compilation/RefusCompilationTest` : chaque test compile un snippet avec `javac`. `isNotEmpty()` signifie que le compilateur **refuse** le code.
6. **Lancer** l'API et comparer les réponses JSON avec les tests.
7. **Faire** les exercices de la section 7 du guide.

---

## Structure du projet

```text
src/main/java/dev/kekhermes/variance/
├── domaine/          Document ← Livre ← BandeDessinee ; Document ← Magazine
├── covariance/       FabriqueDocument, FabriqueLivre, FabriqueBandeDessinee, TableauxCovariants, LectureDocuments
├── contravariance/   EcritureDocuments (? super), Consommateurs (Consumer<? super T>), Comparaisons (Comparable<? super T>)
├── pecs/             Transformations (copier, transformer)
├── evenements/       EvenementDocument, LivreAjoute, MagazineAjoute, EnregistreurEvenements (@EventListener)
└── api/              VarianceController, Demonstration
src/test/java/dev/kekhermes/variance/
├── covariance/       CovarianceTest
├── contravariance/   ContravarianceTest
├── pecs/             PecsTest
├── compilation/      RefusCompilationTest (javac sur snippets)
└── api/              VarianceControllerTest (MockMvc)
docs/
└── guide-pedagogique.md
```

---

## Règle à retenir

> **PECS** : si le paramètre **produit** des `T` (lecture), utilisez `? extends T`.
> Si le paramètre **consomme** des `T` (écriture ou callback), utilisez `? super T`.
> Si les deux, `T` exact. Pour un retour, un type concret.

Le guide détaille les cas réels du JDK (`Stream.map`, `Collections.sort`, `Collections.copy`, `Comparator`) et les pièges (tableaux, `@Override` absent, types bruts).

---

## Notes techniques

- **Spring Boot 4** a déplacé certaines annotations de test : `@AutoConfigureMockMvc` se trouve dans `org.springframework.boot.webmvc.test.autoconfigure`.
- Les tests de compilation utilisent `javax.tools.ToolProvider` : ils nécessitent un **JDK** (pas un simple JRE).
