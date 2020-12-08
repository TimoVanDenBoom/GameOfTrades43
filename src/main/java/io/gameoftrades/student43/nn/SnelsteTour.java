package io.gameoftrades.student43.nn;

import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timo on 26-9-2016.
 */
public class SnelsteTour {

    private Stad beginStad;
    private ArrayList<Pad> padList;

    public SnelsteTour(List<Pad> padList, Stad beginStad){
        this.padList = new ArrayList<>(padList);
        this.beginStad = beginStad;
    }

    public Stad getBeginStad() {
        return beginStad;
    }

    public ArrayList<Pad> getPadList() {
        return padList;
    }

    public int getTotaleTijd(){
        return padList.stream().map(p -> p.getTotaleTijd()).reduce(0, (i, j) -> i + j);
    }
}
