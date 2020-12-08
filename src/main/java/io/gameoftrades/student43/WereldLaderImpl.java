package io.gameoftrades.student43;

import io.gameoftrades.model.Wereld;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import io.gameoftrades.model.kaart.*;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.HandelType;
import io.gameoftrades.model.markt.Handelswaar;
import io.gameoftrades.model.markt.Markt;

class WereldLaderImpl implements WereldLader {

    private Kaart kaart;
    private ArrayList<Stad> steden;
    private ArrayList<String> stadNamen;
    private Markt markt;
    private ArrayList<Handel> handel;
    private BufferedReader br;

    @Override
    public Wereld laad(String resource) {

        try {
            this.br = new BufferedReader(new FileReader(resource));
            kaartMaken();
            terreinMaken();
            stedenMaken();
            marktenMaken();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Wereld(this.kaart, this.steden, this.markt);
    }

    /**
     * Grootte van kaart uitlezen en kaart aanmaken
     * @throws IOException voor het gebruik van de filereader
     */
    private void kaartMaken() throws IOException{

        String[] mapGrootte = br.readLine().trim().split(",");
        int mapBreedte = Integer.parseInt(mapGrootte[0]);
        int mapLengte = Integer.parseInt(mapGrootte[1]);
        this.kaart = new Kaart(mapBreedte, mapLengte);
    }

    /**
     * Terreintypes uitlezen en toevoegen aan de kaart
     * @throws IOException voor het gebruik van de filereader
     */
    private void terreinMaken() throws IOException{

        for (int i = 0; i < kaart.getHoogte(); i++) {
            String terreinKarakters = br.readLine().trim();

            if (terreinKarakters.length() != kaart.getBreedte()){
                throw new IllegalArgumentException();
            }


                for (int j = 0; j < kaart.getBreedte(); j++) {

                    switch (terreinKarakters.charAt(j)) {
                        case 'Z':
                            new Terrein(kaart, Coordinaat.op(j, i), TerreinType.ZEE);
                            break;
                        case 'G':
                            new Terrein(kaart, Coordinaat.op(j, i), TerreinType.GRASLAND);
                            break;
                        case 'B':
                            new Terrein(kaart, Coordinaat.op(j, i), TerreinType.BOS);
                            break;
                        case 'R':
                            new Terrein(kaart, Coordinaat.op(j, i), TerreinType.BERG);
                            break;
                        case 'S':
                            new Terrein(kaart, Coordinaat.op(j, i), TerreinType.STAD);
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                }


        }
    }

    /**
     * Steden uitlezen en aanmaken
     * @throws IOException voor het gebruik van de filereader
     */
    private void stedenMaken() throws IOException{

        int aantalSteden = Integer.parseInt(br.readLine().trim());
        this.steden = new ArrayList<>();
        this.stadNamen = new ArrayList<>();

        for (int i = 0; i < aantalSteden; i++) {
            String[] split = br.readLine().trim().split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            if(x <= 0 || y <= 0){
                throw new IllegalArgumentException();
            }
            String naam = split[2];
            steden.add(new Stad(Coordinaat.op(x - 1, y - 1), naam));
            stadNamen.add(naam);
        }
    }

    /**
     * Markten uitlezen en aanmaken
     * @throws IOException voor het gebruik van de filereader
     */
    private void marktenMaken() throws IOException{

        int aantalMarkten = Integer.parseInt(br.readLine().trim());
        this.handel = new ArrayList<>();

        for(int i = 0; i < aantalMarkten; i++){
            String[] split = br.readLine().trim().split(",");
            String stad = split[0];
            String type = split[1];
            String waren = split[2];
            Handelswaar handelswaar = new Handelswaar(waren);
            int prijs = Integer.parseInt(split[3]);

            if(stadNamen.contains(stad)){
                switch (type){
                    case "BIEDT":
                        steden.stream()
                                .filter(e -> e.getNaam().equals(stad))
                                .forEach(e -> handel.add(new Handel(e, HandelType.BIEDT, handelswaar, prijs)));
                        break;
                    case "VRAAGT":
                        steden.stream()
                                .filter(e -> e.getNaam().equals(stad))
                                .forEach(e -> handel.add(new Handel(e, HandelType.VRAAGT, handelswaar, prijs)));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } else{
                throw new IllegalArgumentException();
            }
        }

        this.markt = new Markt(handel);
    }
}