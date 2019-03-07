/**
 * Lecture /ecriture d'un fichier Property dont le nom est passé en parametres du constructeur
 * Si le fichier n'existe pas, il est créé avec les valeurs par défaut des parametres.
 * IOParamsMM traite les parametres du jeu MasterMind
 */
package fr.ocr.params.paramsMM;

import java.util.Properties;

import fr.ocr.params.IOParams;

public class IOParamsMM extends IOParams {

    /**
     *
     * @param nomDuFichierParam nom du fichier des parametres du mastermind
     */
    public IOParamsMM(String nomDuFichierParam) {
        super(nomDuFichierParam);
    }

    /**
     *implemente la méthode abstraite de la classe dont hérite cette classe
     * Fournit les valeurs par défaut du jeu MastermInd dans la collection Properties
     * listparam
     * @param listeParam : properties
     */
    @Override
    protected void getParamDefaut(Properties listeParam) {
        for (GroupParamsMM v: GroupParamsMM.values()) {
            listeParam.setProperty(v.name(), String.valueOf(v.getUnParam().getValeurDefaut()));
            }
        }
}

