package io.gameoftrades.student43.genetic;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Pad;

public class Path  {

    private Coordinaat start;
    private Coordinaat end;
    private int length;
    private Pad pad;

    public Path(Coordinaat start, Coordinaat end, int length, Pad pad){
        this.start = start;
        this.end = end;
        this.length = length;
        this.pad = pad;
    }

    public Coordinaat getStart(){
        return start;
    }

    public Coordinaat getEnd(){
        return end;
    }

    public Pad getPad(){ return pad; }

    public int getLength(){
        return length;
    }


}