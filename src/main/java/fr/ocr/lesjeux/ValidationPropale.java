package fr.ocr.lesjeux;

import java.util.ArrayList;

@FunctionalInterface
public interface ValidationPropale {

     Boolean apply(ArrayList<Character> proposition,
                   ArrayList<Character> secret,
                   Integer nombreDePositions,
                   int[] zoneEvaluation);
}
