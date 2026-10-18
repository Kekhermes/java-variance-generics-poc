package dev.kekhermes.variance.api;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class VarianceControllerTest {

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void viderLeJournal() throws Exception {
        mvc.perform(delete("/api/variance/evenements")).andExpect(status().isOk());
    }

    @Test
    void unListenerSurSuperTypeRecoitLesLivresEtLesSousTypes() throws Exception {
        mvc.perform(post("/api/variance/evenements/livre"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(containsInAnyOrder(
                "surTout a reçu LivreAjoute",
                "surLivres a reçu LivreAjoute")));
    }

    @Test
    void unListenerSurSousTypeNeRecoitPasLesMagazines() throws Exception {
        mvc.perform(post("/api/variance/evenements/magazine"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(containsInAnyOrder(
                "surTout a reçu MagazineAjoute")));
    }

    @Test
    void lesDemonstrationsDeCovarianceSontExposees() throws Exception {
        mvc.perform(get("/api/variance/covariance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].resultat").value(startsWith("ArrayStoreException")))
            .andExpect(jsonPath("$[1].resultat").value("BandeDessinee / BandeDessinee"));
    }

    @Test
    void lesDemonstrationsPecsEtContravarianceSontExposees() throws Exception {
        mvc.perform(get("/api/variance/contravariance")).andExpect(status().isOk());
        mvc.perform(get("/api/variance/pecs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[1].resultat[0]").value("Bande dessinée « Tintin au Tibet » de Hergé"));
    }

    @Test
    void laVarianceSpringResolvableTypeSuitLesMemesRegles() throws Exception {
        mvc.perform(get("/api/variance/spring/invariance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].resultat").value(false))
            .andExpect(jsonPath("$[1].resultat").value(true))
            .andExpect(jsonPath("$[2].resultat").value(true))
            .andExpect(jsonPath("$[3].resultat").value(false));
    }

    @Test
    void untypeDEvenementInconnuEstRefuse() throws Exception {
        mvc.perform(post("/api/variance/evenements/inconnu")).andExpect(status().isBadRequest());
    }
}
