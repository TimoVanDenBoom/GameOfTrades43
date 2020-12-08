package io.gameoftrades.student43.handelsplan;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.algoritme.HandelsplanAlgoritme;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.Handelsplan;
import io.gameoftrades.model.markt.actie.*;
import io.gameoftrades.student43.genetic.PathChecker;

import java.util.ArrayList;
import java.util.List;

public class HandelsPlanAlgoritmeImpl implements HandelsplanAlgoritme {
    @Override
    public Handelsplan bereken(Wereld wereld, HandelsPositie positie) {
        List<Actie> acties = new ArrayList<>();

        PathChecker pathChecker = new PathChecker(wereld.getSteden(), wereld.getKaart());
//        for (Handel j: wereld.getMarkt().getHandel()) {
//            System.out.println(j.getStad().toString() + " " + j.getHandelType() + " " + j.getHandelswaar() + " " + j.getPrijs());
//        }
        while (positie.getTotaalActie() < positie.getMaxActie()){
            double highestEfficieny = 0;
            Handel mostEfficientBuy = null;
            Handel mostEfficientSell = null;

            for (Handel h : wereld.getMarkt().getAanbod()){

                for (Handel h2 : wereld.getMarkt().getVraag()) {
                    if (h.getHandelswaar().equals(h2.getHandelswaar())){

                        int pathLength = !h.getStad().getCoordinaat().equals(positie.getCoordinaat()) ?
                                pathChecker.getPathLength(positie.getCoordinaat(), h.getStad().getCoordinaat()) +
                                        pathChecker.getPathLength(h.getStad().getCoordinaat(), h2.getStad().getCoordinaat()) :
                                pathChecker.getPathLength(h.getStad().getCoordinaat(), h2.getStad().getCoordinaat());

                        if (pathLength + 2 > positie.getMaxActie() - positie.getTotaalActie()){
                            continue;
                        }

                        int maxBuys = Math.floorDiv(positie.getKapitaal(), h.getPrijs());

                        if (maxBuys > positie.getRuimte()){
                            maxBuys = positie.getRuimte();
                        }

                        double winst = h2.getPrijs() * maxBuys - h.getPrijs() * maxBuys;
                        double efficiency = winst / pathLength + 2;

                        if (efficiency > highestEfficieny){
                            highestEfficieny = efficiency;
                            mostEfficientBuy = h;
                            mostEfficientSell = h2;
                        }
                    }
                }
            }

            if (highestEfficieny == 0){
                break;
            }

            System.out.println("Buying: " + mostEfficientBuy.toString());
            System.out.println("Selling: " + mostEfficientSell.toString());

            BeweegActie naarBiedendeStad = new BeweegActie(wereld.getKaart(), positie.getStad(), mostEfficientBuy.getStad(),
                    PathChecker.getPath(positie.getCoordinaat(), mostEfficientBuy.getStad().getCoordinaat()).getPad());
            positie = naarBiedendeStad.voerUit(positie);
            KoopActie koop = new KoopActie(mostEfficientBuy);
            positie = koop.voerUit(positie);
            BeweegActie naarVragendeStad = new BeweegActie(wereld.getKaart(), positie.getStad(), mostEfficientSell.getStad(),
                    PathChecker.getPath(positie.getStad().getCoordinaat(), mostEfficientSell.getStad().getCoordinaat()).getPad());
            positie = naarVragendeStad.voerUit(positie);
            VerkoopActie verkoop = new VerkoopActie(mostEfficientSell);
            positie = verkoop.voerUit(positie);
            acties.add(naarBiedendeStad);
            acties.add(koop);
            acties.add(naarVragendeStad);
            acties.add(verkoop);
        }

        return new Handelsplan(acties);
    }

    public String toString(){
        return "Handelsplan Algoritme";
    }
}
