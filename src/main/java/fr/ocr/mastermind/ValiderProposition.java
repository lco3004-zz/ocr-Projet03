package fr.ocr.mastermind;

import java.util.ArrayList;


public interface ValiderProposition {
    default Boolean apply(ArrayList<Character> proposition,
                          ArrayList<Character> secret,
                          Integer nombreDePositions,
                          int[] zoneEvaluation) {
        return null;
    }
}
