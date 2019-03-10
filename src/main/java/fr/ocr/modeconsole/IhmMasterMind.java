package fr.ocr.modeconsole;


import fr.ocr.mastermind.ValidationPropale;
import fr.ocr.params.CouleursMastermind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import static fr.ocr.params.LireParametres.getParam;
import static fr.ocr.params.Parametres.*;

/**
 *
 */
public class IhmMasterMind implements ConstLignesMM {


    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);
    private Boolean modeDebug = (Boolean)getParam(MODE_DEBUG);
    private Integer nombreDeEssais =(Integer)getParam(NOMBRE_D_ESSAIS);


    private ArrayList<Integer> compositionChiffresSecrets;
    private  CouleursMastermind[]  compositionCouleursSecretes;



    // lignes MM affichees par l'ihm. +5 pour les lignes d'infos (titre, ...)
    LigneJeuMM [] lignesJeuMM = new LigneJeuMM[nombreDeEssais + 5];

    LigneJeuMM [] lignesProposition = new LigneJeuMM[nombreDeEssais] ;

    int indexLignesJeuMM = 0, indexLignesProposition =0;


    /**
     *
     * @param chiffresSecrets
     * @param couleursSecretes
     */
    public IhmMasterMind(ArrayList<Integer> chiffresSecrets,
                         CouleursMastermind[]  couleursSecretes, ValidationPropale fctValidePropale) {

        compositionCouleursSecretes = couleursSecretes;
        compositionChiffresSecrets = chiffresSecrets;

        lignesJeuMM[TITRE] = new LigneJeuMM(compositionCouleursSecretes,
                compositionChiffresSecrets,
                String.valueOf(indexLignesJeuMM),TITRE,null);

        lignesJeuMM[LIGNE_STATUS] = new LigneJeuMM(compositionCouleursSecretes,
                compositionChiffresSecrets,
                String.valueOf(indexLignesJeuMM),LIGNE_STATUS,null);

        lignesJeuMM[LIGNE_SECRETE] = new LigneJeuMM(compositionCouleursSecretes,
                compositionChiffresSecrets,
                String.valueOf(indexLignesJeuMM),LIGNE_SECRETE,null);


        for (int k= LIGNE_SECRETE +1 ; k < k + nombreDeEssais; k++,indexLignesProposition++) {

            lignesJeuMM[k] = new LigneJeuMM(compositionCouleursSecretes,
                    compositionChiffresSecrets,
                    String.valueOf(k),ConstLignesMM.LIGNE_PROPOSITION,fctValidePropale);

            lignesJeuMM[k].Clear();
            lignesProposition[indexLignesProposition]= lignesJeuMM[k];


        }

        lignesJeuMM[LIGNE_TOUTES_COULEURS] = new LigneJeuMM(compositionCouleursSecretes,
                compositionChiffresSecrets,
                String.valueOf(LIGNE_TOUTES_COULEURS),LIGNE_TOUTES_COULEURS,null);

        ArrayList<Character> listeToutesCol = new ArrayList<>();
        for (CouleursMastermind v : CouleursMastermind.values()) {
            listeToutesCol.add(v.getLettreInitiale());
        }
        lignesJeuMM[LIGNE_TOUTES_COULEURS].setZoneProposition(listeToutesCol);

        lignesJeuMM[LIGNE_DE_SAISIE] = new LigneJeuMM(compositionCouleursSecretes,
                compositionChiffresSecrets,
                String.valueOf(LIGNE_DE_SAISIE),LIGNE_DE_SAISIE,null);

         ArrayList<Character> affichageParDefaut = new ArrayList<Character>(256);
        affichageParDefaut.toArray(new String[]{"votre Proposition : "});
        lignesJeuMM[LIGNE_DE_SAISIE].setZoneProposition(affichageParDefaut);


    }
    public void runIhmMM() {
        for (int n =TITRE; n <=LIGNE_DE_SAISIE; n++) {
            System.out.println(lignesJeuMM[n].toString());
        }
    }
}


/**
 *
 */
interface ConstLignesMM {
    int TITRE =0;
    int LIGNE_STATUS = TITRE+1;
    int LIGNE_SECRETE = LIGNE_STATUS +1;
    int LIGNE_PROPOSITION = LIGNE_SECRETE+1;
    int LIGNE_TOUTES_COULEURS = LIGNE_PROPOSITION+(Integer)getParam(NOMBRE_D_ESSAIS);
    int LIGNE_DE_SAISIE = LIGNE_TOUTES_COULEURS+1;

     static int [] values() {
         int [] locP = {TITRE,LIGNE_STATUS,LIGNE_SECRETE,LIGNE_PROPOSITION,LIGNE_TOUTES_COULEURS,LIGNE_DE_SAISIE};
        return locP;
    }
}

/**
 *
 */
class LigneJeuMM implements ConstLignesMM {

    private ArrayList<Character> zoneProposition = new ArrayList(256);
    private ArrayList<Integer> ZoneEvaluation = new ArrayList(256);

    private boolean disponible = true;
    private boolean visible = true;
    private String rangDansTableJeu;

    private int typeDeLigne;


    private CouleursMastermind[] combinaisonCouleursSecretes;
    private ArrayList<Integer> combinaisonChiffresSecrets;

    private Boolean doublonAutorise = (Boolean) getParam(DOUBLON_AUTORISE);
    private Integer nombreDePositions = (Integer) getParam(NOMBRE_DE_POSITIONS);
    private Integer nombreDeCouleurs = (Integer) getParam(NOMBRE_DE_COULEURS);


    public LigneJeuMM(CouleursMastermind[] secretCouleurs,
                      ArrayList<Integer> secretChiffres,
                      String rang,
                      int typeligne,ValidationPropale fctValideProposition) {
        combinaisonChiffresSecrets = secretChiffres;
        combinaisonCouleursSecretes = secretCouleurs;
        rangDansTableJeu = rang;
        typeDeLigne = typeligne;



    }

    ArrayList<Character> getZoneProposition() {
        return zoneProposition;
    }

    void setZoneProposition(ArrayList<Character> valPropale) {
        zoneProposition.clear();
        zoneProposition.add('[');
        zoneProposition.add(' ');
        for (int i = 0; i < valPropale.size() - 1; i++) {
            zoneProposition.add(valPropale.get(i));
            zoneProposition.add(',');
            zoneProposition.add(' ');
        }
        zoneProposition.add(' ');
        zoneProposition.add(']');
    }

    ArrayList<Integer> getZoneEvaluation() {
        return ZoneEvaluation;
    }

    void setZoneEvaluation(ArrayList<Integer> zoneEvaluation) {
        ZoneEvaluation = zoneEvaluation;
    }


    int getTypeDeLigne() {
        return typeDeLigne;
    }

    void setTypeDeLigne(int typeDeLigne) {
        this.typeDeLigne = typeDeLigne;
    }

    Boolean isDisponible() {
        return disponible;
    }

    void isDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    Boolean isVisible() {
        return visible;
    }

    void isVisible(Boolean visible) {
        this.visible = visible;
    }

    private void EvalProposition(int i) {
    }

    public String toString() {
        return String.format("| %d | %s", rangDansTableJeu, zoneProposition.toArray().toString());
    }

    public void Clear() {
        zoneProposition.clear();
        ZoneEvaluation.clear();

        zoneProposition.add('[');
        zoneProposition.add(' ');
        for (int i = 0; i < nombreDePositions; i++) {
            zoneProposition.add('-');
            zoneProposition.add(',');
            zoneProposition.add(' ');
        }
        zoneProposition.add(' ');
        zoneProposition.add(']');
    }

    public void Reset() {
        zoneProposition.clear();
        ZoneEvaluation.clear();
    }

}
