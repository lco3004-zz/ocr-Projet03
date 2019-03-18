package fr.ocr.utiles;

import java.util.ArrayList;

public interface ValiderProposition {
    Boolean apply(ArrayList<Character> proposition,
                  ArrayList<Character> secret,
                  Integer nombreDePositions,
                  int[] zoneEvaluation);
}
