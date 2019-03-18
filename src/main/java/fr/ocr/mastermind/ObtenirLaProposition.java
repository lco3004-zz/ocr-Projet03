package fr.ocr.mastermind;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

@FunctionalInterface
public interface ObtenirLaProposition {

    Function<ParamsFunc, ArrayList<Character>> apply = (ParamsFunc paramsFunc) -> paramsFunc.getPropostion(paramsFunc);

    ArrayList<Character> getPropale(Function<ParamsFunc, ArrayList<Character>> funcArrayListFunction);

    class ParamsFunc {
        public Scanner scanner = null;
        public String pattern = null;
        public Character escChar = null;

        ArrayList<Character> getPropostion(ParamsFunc paramsFunc) {
            return null;
        }
    }

}
