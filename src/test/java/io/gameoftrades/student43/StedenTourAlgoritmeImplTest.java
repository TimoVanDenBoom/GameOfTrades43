package io.gameoftrades.student43;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.student43.genetic.StedenTourAlgoritmeImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StedenTourAlgoritmeImplTest {

    private WereldLader lader;
    private StedenTourAlgoritmeImpl tour;

    @Before
    public void init() {
        lader = new WereldLaderImpl();
    }

    //Controleert wat er gebeurt als de aangevoerde stedenlijst geen steden bevat.
    @Test
    public void berekenMetLegeStedenLijstParameter(){
        Wereld wereld = lader.laad("src/test/resources/kaarten/voorbeeld-kaart.txt");
        tour = new StedenTourAlgoritmeImpl();
        List<Stad> leeg = new ArrayList<>();
        assertTrue(tour.bereken(wereld.getKaart(), leeg).isEmpty());
    }

    //Controleert of de eindoplossing geen dubbele steden bevat.
    @Test
    public void uitkomstBevatNietDezelfdeSteden(){
        Wereld wereld = lader.laad("src/test/resources/kaarten/voorbeeld-kaart.txt");
        tour = new StedenTourAlgoritmeImpl();
        List<Stad> uitkomst = tour.bereken(wereld.getKaart(), wereld.getSteden());
        boolean check = false;
        for (int i = 0; i < uitkomst.size(); i++) {
            for (int j = 0; j < uitkomst.size(); j++) {

                if(i != j && uitkomst.get(i).getCoordinaat().equals(uitkomst.get(j).getCoordinaat())){
                    check = true;
                }
            }
        }

        assertFalse(check);
    }

    //Controleert of de eindoplossing alle steden op de kaart bevat.
    @Test
    public void uitkomstBevatEvenveelStedenAlsDeKaartBevat(){
        Wereld wereld = lader.laad("src/test/resources/kaarten/voorbeeld-kaart.txt");
        tour = new StedenTourAlgoritmeImpl();
        List<Stad> uitkomst = tour.bereken(wereld.getKaart(), wereld.getSteden());
        boolean check = false;
        if(wereld.getSteden().size() == uitkomst.size()){
            check = true;
        }

        assertTrue(check);
    }
}
