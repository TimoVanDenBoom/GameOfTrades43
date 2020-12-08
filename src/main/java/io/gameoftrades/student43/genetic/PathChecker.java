package io.gameoftrades.student43.genetic;

import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student43.snelstepad.SnelstePadAlgoritmeImpl;

import java.util.ArrayList;
import java.util.List;

public class PathChecker {

    private static List<Path> paths;
    private List<Stad> openList;
    private SnelstePadAlgoritme snelstePadAlgoritme;

    public PathChecker (List<Stad> steden, Kaart kaart){
        this.snelstePadAlgoritme = new SnelstePadAlgoritmeImpl();
        this.paths = new ArrayList<>();
        this.openList = new ArrayList<>(steden);
        checkPaths(steden, kaart);
    }

    //Berekent alle mogelijke paden op een kaart.
    private void checkPaths(List<Stad> steden, Kaart kaart){
        for (Stad s: steden){

            openList.remove(s);


            openList.stream().filter(stad -> !listContains(s.getCoordinaat(), stad.getCoordinaat())).forEach(stad -> {
                Pad pad = snelstePadAlgoritme.bereken(kaart, s.getCoordinaat(), stad.getCoordinaat());
                int pathLength = pad.getTotaleTijd();
                paths.add(new Path(s.getCoordinaat(), stad.getCoordinaat(), pathLength, pad));
                paths.add(new Path(stad.getCoordinaat(), s.getCoordinaat(), pathLength, pad));
            });

            openList.add(s);
        }
    }

    public int getPathLength (Coordinaat begin, Coordinaat eind){
        return paths.stream().filter(p -> p.getStart().equals(begin) && p.getEnd().equals(eind)).map(Path::getLength).reduce(0, (i, j) -> i + j);
    }

    public static Path getPath (Coordinaat begin, Coordinaat eind){
        for (Path p : paths){
            if (p.getStart().equals(begin) && p.getEnd().equals(eind)){
                return p;
            }
        }
        return null;
    }



    public static boolean listContains(Coordinaat begin, Coordinaat eind){
        for (Path p : paths){
            if (p.getEnd() == eind && p.getStart() == begin){
                return true;
            }
        }
        return false;
    }
}
