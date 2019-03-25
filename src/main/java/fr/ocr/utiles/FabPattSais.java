package fr.ocr.utiles;

import java.util.Locale;

import static fr.ocr.utiles.Constantes.ConstTailleStringBuilder.TAIILE_INITIALE;

/**
 * <p>
 *
 * @author laurent cordier
 * </p>
 */

public final class FabPattSais {

    static private String ConstruitPatternSaisie(Constantes.CouleursMastermind[] colMM,
                                                 Character escapeChar) {
        StringBuilder patternSaisie = new StringBuilder(TAIILE_INITIALE);
        String s;
        patternSaisie.append('[');
        for (Constantes.CouleursMastermind v : colMM) {
            patternSaisie.append(v.getLettreInitiale());
            patternSaisie.append(' ');
            s = String.valueOf(v.getLettreInitiale()).toLowerCase(Locale.forLanguageTag("fr"));
            patternSaisie.append(s.toCharArray()[0]);
            patternSaisie.append(' ');
        }
        patternSaisie.append(escapeChar);
        patternSaisie.append(' ');
        s = String.valueOf(escapeChar).toLowerCase(Locale.forLanguageTag("fr"));
        patternSaisie.append(s.toCharArray()[0]);
        patternSaisie.append(']');
        return patternSaisie.toString();
    }

    /**
     * @param escapeChar le pattern de saisie ne contient que le caractère d'échappement
     * @return String listeInitialesColor (pattern de saisie
     */
    static public String ConstruitPatternSaisie(Character escapeChar) {
        StringBuilder patternSaisie = new StringBuilder(TAIILE_INITIALE);
        String s;
        patternSaisie.append('[');
        patternSaisie.append(' ');
        patternSaisie.append(escapeChar);
        patternSaisie.append(' ');
        s = String.valueOf(escapeChar).toLowerCase(Locale.forLanguageTag("fr"));
        patternSaisie.append(s.toCharArray()[0]);
        patternSaisie.append(']');
        return patternSaisie.toString();
    }


    public static String ConstruitPatternSaisie(Constantes.CouleursMastermind[] values, Integer nombreDeCouleurs, Character charactersEscape) {
        Constantes.CouleursMastermind[] y = new Constantes.CouleursMastermind[nombreDeCouleurs];

        System.arraycopy(values, 0, y, 0, nombreDeCouleurs);

        return ConstruitPatternSaisie(y, charactersEscape);
    }
}
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */