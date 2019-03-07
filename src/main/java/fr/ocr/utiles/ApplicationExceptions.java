package fr.ocr.utiles;

import java.io.FileNotFoundException;
import java.io.IOException;

import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
import static fr.ocr.utiles.Messages.ErreurMessages.SORTIE_PGM_SUR_ERREURNONGEREE;

/**
 *  selecteur d'exception de l'application
 *  peut sortir de l'application sur ERREUR_GENERIC avec coderetour au systeme = à -1
 */
public class ApplicationExceptions extends Exception {

    public ApplicationExceptions(IOException e) {
        logger.error(e.getMessage());
    }

    /**
     * l'exception reçoit une instance d'Enum qui signale une erreur non gérée
     * le libelle de ERREUR_GENERIC est "ne devrait pas se produire"
     *
     * Sortie pgm avec code retour  -1
     *
     * @param err est l'instance "ERREUR_GENERIC" de l'Enum Messages.ErreurMessages
     */
    public ApplicationExceptions(Messages.ErreurMessages err) {
        logger.error(err.getMessageErreur());
        if (err.equals(ERREUR_GENERIC)) {
            logger.error(SORTIE_PGM_SUR_ERREURNONGEREE.toString());
        }
        System.exit(-1);
    }

    public ApplicationExceptions(FileNotFoundException e) {
        logger.error(e.getMessage());
    }

    public ApplicationExceptions(Exception e) {
        logger.error(e.getMessage());
    }

}
