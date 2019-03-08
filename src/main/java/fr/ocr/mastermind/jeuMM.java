package fr.ocr.mastermind;

import fr.ocr.params.CouleursMastermind;
import fr.ocr.utiles.AppExceptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.NOMBRE_DE_POSITIONS;
import static fr.ocr.params.Parametres.NOMBRE_D_ESSAIS;

public class jeuMM implements TypeDesLignes{
    private Integer nbEssais = (Integer) getParam(NOMBRE_D_ESSAIS);
    private Integer nbPositions = (Integer) getParam(NOMBRE_DE_POSITIONS);


    private LinkedList<LigneJeu> lignesDuJeu = new LinkedList<LigneJeu>();

    public jeuMM () throws AppExceptions {
        int rang = 0;
        ArrayList<Integer> arrayList = new ArrayList<>();

        int monCompteur =0;
        for (CouleursMastermind v: CouleursMastermind.values()) {
            arrayList.add(v.getValeurFacialeDeLaCouleur());
            if (monCompteur == nbPositions-1)
                break;
            monCompteur++;
        }


        FabricationSecret fabricationSecret = new FabricationSecret(arrayList);

        lignesDuJeu.add(new LigneJeu(fabricationSecret.getCouleursSecretes(),fabricationSecret.getChiffresSecrets(),"",LIGNE_SECRETE));

        for (int i= 0;i < nbEssais -1; i++) {
            lignesDuJeu.add(new LigneJeu(fabricationSecret.getCouleursSecretes(),fabricationSecret.getChiffresSecrets(),String.valueOf(i),TypeDesLignes.LIGNE_PROPOSITION));
        }

        lignesDuJeu.add(new LigneJeu(fabricationSecret.getCouleursSecretes(),fabricationSecret.getChiffresSecrets(),"",TypeDesLignes.LIGNE_TOUTES_COULEURS));


        lignesDuJeu.add(new LigneJeu(fabricationSecret.getCouleursSecretes(),fabricationSecret.getChiffresSecrets(),"",TypeDesLignes.LIGNE_DE_SAISIE));


        for (Iterator<LigneJeu> it = lignesDuJeu.descendingIterator(); it.hasNext(); ) {
            LigneJeu l = it.next();
            if (l.getTypeDeLigne() == LIGNE_TOUTES_COULEURS) {
                l.Reset();
                ArrayList<Character> listeToutesCol = new ArrayList<>();
                for (CouleursMastermind v : CouleursMastermind.values()) {
                    listeToutesCol.add(v.getLettreInitiale());
                }
                l.setZoneProposition(listeToutesCol);
                break;
            }
        }
        for (Iterator<LigneJeu> it = lignesDuJeu.listIterator(); it.hasNext(); ) {
            LigneJeu l = it.next();
            System.out.println(l.toString());
        }


    }
}
