package fr.ocr.mastermind;

import java.util.ArrayList;

@FunctionalInterface
public interface ValiderProposition {

     Boolean apply(ArrayList<Character> proposition,
                   ArrayList<Character> secret,
                   Integer nombreDePositions,
                   int[] zoneEvaluation);
}
