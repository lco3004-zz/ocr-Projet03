package fr.ocr.utiles;

import java.util.Locale;

public final class FabPattSais {

    static public String ConstruitPatternSaisie(Constantes.CouleursMastermind[] colMM,
                                                Character escapeChar) {
        StringBuilder patternSaisie = new StringBuilder(256);
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
        StringBuilder patternSaisie = new StringBuilder(256);
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


}
