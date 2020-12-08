package io.gameoftrades.student43.snelstepad;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;

import java.util.Arrays;
import java.util.Collections;

public class PadImpl implements Pad{

    private int totaleTijd;
    private Richting[] bewegingen;


    public PadImpl(int totaleTijd, Richting[] bewegingen){
        this.totaleTijd = totaleTijd;
        this.bewegingen = bewegingen;
    }

    @Override
    public int getTotaleTijd() {
        return this.totaleTijd;
    }

    @Override
    public Richting[] getBewegingen() {
        return this.bewegingen;
    }

    @Override
    public Pad omgekeerd() {

        Collections.reverse(Arrays.asList(this.bewegingen));

        for (int i = 0; i < this.bewegingen.length; i++){
            this.bewegingen[i] = this.bewegingen[i].omgekeerd();
        }

        return new PadImpl(this.totaleTijd, this.bewegingen);
    }


    @Override
    public Coordinaat volg(Coordinaat start) {
        Coordinaat huidig = start;

        for (Richting r : this.bewegingen) {
            huidig = huidig.naar(r);
        }

        return huidig;
    }

}
