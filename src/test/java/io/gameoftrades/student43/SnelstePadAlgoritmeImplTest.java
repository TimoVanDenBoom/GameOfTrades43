package io.gameoftrades.student43;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.student43.snelstepad.Node;
import io.gameoftrades.student43.snelstepad.SnelstePadAlgoritmeImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Martijn on 26-Oct-16.
 */
public class SnelstePadAlgoritmeImplTest {

    private WereldLader lader;
    private SnelstePadAlgoritmeImpl snel;

    @Before
    public void init() {
        lader = new WereldLaderImpl();
    }

    //Controleert of een nieuwe node niet dubbel wordt toegevoegd aan een list.
    @Test
    public void checkOfNodeInListZit(){
        Wereld wereld = lader.laad("src/test/resources/kaarten/voorbeeld-kaart.txt");
        Node node1 = new Node(null, wereld.getKaart().getTerreinOp(Coordinaat.op(1,2)));

        List<Node> openAndClosedList = new ArrayList<>();
        openAndClosedList.add(node1);
        boolean check = false;

        for(int i = 0; i < openAndClosedList.size(); i++){
            if(openAndClosedList.get(i).getCoordinaat().equals(node1.getCoordinaat())){
                check = true;

            }
        }
        assertTrue(check);
    }

    //Controleert of een nieuwe node wel wordt toegevoegd aan een list, mits deze nog niet in een list zit.
    @Test
    public void checkOfNodeNietInListZit(){
        Wereld wereld = lader.laad("src/test/resources/kaarten/voorbeeld-kaart.txt");
        Node node1 = new Node(null, wereld.getKaart().getTerreinOp(Coordinaat.op(1,2)));
        Node node2 = new Node(null, wereld.getKaart().getTerreinOp(Coordinaat.op(1,3)));

        List<Node> openAndClosedList = new ArrayList<>();
        openAndClosedList.add(node1);
        boolean check = false;

        for(int i = 0; i < openAndClosedList.size(); i++){
            if(openAndClosedList.get(i).getCoordinaat().equals(node2.getCoordinaat())){
                check = true;
            }
        }

        assertFalse(check);
    }

    //Controleert wat er gebeurt als start- en eindcoördinaat hetzelfde zijn.
    @Test
    public void algoritmeAanroepenMetDezelfdeParameters(){
        Wereld wereld = lader.laad("src/test/resources/kaarten/voorbeeld-kaart.txt");
        snel = new SnelstePadAlgoritmeImpl();
        Pad pad = snel.bereken(wereld.getKaart(), Coordinaat.op(1,1), Coordinaat.op(1,1));

        assertEquals(pad.getBewegingen().length, 0);
    }

    //Controleert wat er gebeurt als er geen begaanbaar pad berekend kan worden.
    @Test
    public void stedenOmringdMetZee(){
        Wereld wereld = lader.laad("src/test/resources/kaarten/testcases/eiland-kaart.txt");
        snel = new SnelstePadAlgoritmeImpl();
        Pad pad = snel.bereken(wereld.getKaart(), Coordinaat.op(0,0), Coordinaat.op(1,2));

        assertNull(pad);
    }
}
