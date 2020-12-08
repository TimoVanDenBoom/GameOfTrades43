package io.gameoftrades.student43.nn;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student43.snelstepad.SnelstePadAlgoritmeImpl;

import java.util.*;

public class StedenTourAlgoritmeImpl implements StedenTourAlgoritme, Debuggable {

    private List<Stad> stedenTour;
    private List<Pad> nearestNeighbourPaden;
    private List<SnelsteTour> snelsteTours;
    private Comparator<Pad> padSorter;
    private Comparator<SnelsteTour> padListSorter;
    private SnelstePadAlgoritmeImpl snel;
    private Debugger debugger;

    public StedenTourAlgoritmeImpl(){
        this.stedenTour = new ArrayList<>();
        this.nearestNeighbourPaden = new ArrayList<>();
        this.snelsteTours = new ArrayList<>();

        this.padSorter = (pad1, pad2) -> pad1.getTotaleTijd() > pad2.getTotaleTijd() ? 1 : -1;

        this.padListSorter = (p1, p2) -> p1.getTotaleTijd() == p2.getTotaleTijd()
                ? 0 : (p1.getTotaleTijd() > p2.getTotaleTijd() ? 1 : -1);


        this.snel = new SnelstePadAlgoritmeImpl();


    }

    @Override
    public List<Stad> bereken(Kaart kaart, List<Stad> list) {

        stedenTour.clear();

        List<Stad> steden = new ArrayList<>(list);
        List<Stad> openList = new ArrayList<>(list);

        for (Stad s: steden){


            Stad beginStad = s;
            System.out.println(beginStad);
            openList.remove(beginStad);


            while(!openList.isEmpty()){

                nearestNeighbourPaden.add(berekenNearestNeighbour(kaart, beginStad, openList));
                Stad next = getNextStadInList(nearestNeighbourPaden.get(nearestNeighbourPaden.size() - 1),
                        openList, beginStad);
                openList.remove(next);
                beginStad = next;
            }

            snelsteTours.add(new SnelsteTour(nearestNeighbourPaden, s));
            nearestNeighbourPaden.clear();

            for (int j = 0; j < steden.size(); j++) {
                openList.add(steden.get(j));
            }
        }

        Collections.sort(snelsteTours, padListSorter);
        System.out.println(snelsteTours.get(0).getTotaleTijd() + " + " + snelsteTours.get(0).getBeginStad());
        List<Pad> route = snelsteTours.get(0).getPadList();
        Stad beginStad = snelsteTours.get(0).getBeginStad();
        stedenTour.add(beginStad);

        for(Pad p : route){
            beginStad = getNextStadInList(p, openList, beginStad);
            stedenTour.add(beginStad);
        }

        debugger.debugSteden(kaart, stedenTour);

        clearLists();

        return stedenTour;
    }



    private Pad berekenNearestNeighbour (Kaart kaart, Stad beginStad, List<Stad> list){
        List<Pad> snelstePad = new ArrayList<>();
        List<Stad> stadList = new ArrayList<>(list);

        while(!stadList.isEmpty()){
            snelstePad.add(snel.bereken(kaart, beginStad.getCoordinaat(), stadList.remove(0).getCoordinaat()));
        }

        Collections.sort(snelstePad, padSorter);

        return snelstePad.get(0);
    }

    private Stad getNextStadInList(Pad pad, List<Stad> list, Stad beginStad){
        Coordinaat stadCoordinaat = pad.volg(beginStad.getCoordinaat());

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getCoordinaat().equals(stadCoordinaat)){
                return list.get(i);
            }
        }
        return null;
    }

    private void clearLists(){

        nearestNeighbourPaden.clear();
        snelsteTours.clear();
    }

    public String toString(){
        return "Nearest Neighbour Algoritme";
    }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }
}
