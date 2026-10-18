package dev.kekhermes.variance.compilation;

import static org.assertj.core.api.Assertions.assertThat;

import dev.kekhermes.variance.domaine.Document;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests de compilation : chaque snippet est compilé par javac.
 * {@code isNotEmpty()} signifie que le compilateur REFUSE le code ; {@code isEmpty()} qu'il l'accepte.
 * Ces tests prouvent les règles de variance plutôt que de les affirmer.
 */
class RefusCompilationTest {

    @Test
    void uneListeDeDocumentsNAccepteJamaisUneListeDeLivres(@TempDir Path sortie) {
        assertThat(erreursDeCompilation(sortie,
            "List<Document> docs = new ArrayList<Livre>();")).isNotEmpty();
    }

    @Test
    void uneListeDeLivresSeLitCommeListeExtendsDocument(@TempDir Path sortie) {
        assertThat(erreursDeCompilation(sortie,
            "List<? extends Document> docs = new ArrayList<Livre>();")).isEmpty();
    }

    @Test
    void unExtendsEstEnLectureSeule(@TempDir Path sortie) {
        assertThat(erreursDeCompilation(sortie,
            "List<? extends Document> docs = new ArrayList<Livre>(); docs.add(new Livre(\"x\", \"y\"));")).isNotEmpty();
    }

    @Test
    void unSuperAccepteLAjoutDUnLivre(@TempDir Path sortie) {
        assertThat(erreursDeCompilation(sortie,
            "List<? super Livre> docs = new ArrayList<Document>(); docs.add(new Livre(\"x\", \"y\"));")).isEmpty();
    }

    @Test
    void unSuperRefuseLAjoutDUnDocumentGenerique(@TempDir Path sortie) {
        assertThat(erreursDeCompilation(sortie,
            "List<? super Livre> docs = new ArrayList<Document>(); docs.add(new Document(\"x\"));")).isNotEmpty();
    }

    @Test
    void unSuperNePermetPasDeLireUnLivre(@TempDir Path sortie) {
        assertThat(erreursDeCompilation(sortie,
            "List<? super Livre> docs = new ArrayList<Document>(); Livre l = docs.get(0);")).isNotEmpty();
    }

    @Test
    void lesTableauxSontAcceptesParLeCompilateurMaisRefusesParLaJvm(@TempDir Path sortie) {
        assertThat(erreursDeCompilation(sortie,
            "Document[] docs = new Livre[1]; docs[0] = new Magazine(\"Pif\", 1);")).isEmpty();
    }

    @Test
    void unFunctionInvariantNeSeConvertitPasEntreTypesDeDocuments(@TempDir Path sortie) {
        assertThat(erreursDeCompilation(sortie,
            "Function<Document, String> f = Document::getTitre; Function<Livre, String> g = f;")).isNotEmpty();
    }

    @Test
    void unFunctionAvecSuperSeConvertitPourLesLivres(@TempDir Path sortie) {
        assertThat(erreursDeCompilation(sortie,
            "Function<Document, String> f = Document::getTitre; Function<? super Livre, String> g = f;")).isEmpty();
    }

    private static List<String> erreursDeCompilation(Path sortie, String instructions) {
        String source = """
            import java.util.*;
            import java.util.function.*;
            import dev.kekhermes.variance.domaine.*;

            class Essai {
                void essai() {
                    %s
                }
            }
            """.formatted(instructions);

        JavaCompiler compilateur = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaFileObject fichier = new SimpleJavaFileObject(URI.create("string:///Essai.java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return source;
            }
        };

        try (StandardJavaFileManager gestionnaire =
                 compilateur.getStandardFileManager(diagnostics, Locale.ROOT, StandardCharsets.UTF_8)) {
            Path classes = Path.of(Document.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            JavaCompiler.CompilationTask tache = compilateur.getTask(null, gestionnaire, diagnostics,
                List.of("-classpath", classes.toString(), "-d", sortie.toString()), null, List.of(fichier));
            tache.call();
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Compilation de test impossible", e);
        }

        return diagnostics.getDiagnostics().stream()
            .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
            .map(d -> d.getMessage(Locale.ROOT))
            .toList();
    }
}
