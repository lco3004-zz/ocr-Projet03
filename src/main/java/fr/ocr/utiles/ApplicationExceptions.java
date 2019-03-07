package fr.ocr.utiles;

import java.io.FileNotFoundException;
import java.io.IOException;

import static fr.ocr.utiles.Logs.logger;
import static fr.ocr.utiles.Messages.ErreurMessages.ERREUR_GENERIC;
import static fr.ocr.utiles.Messages.ErreurMessages.SORTIE_PGM_SUR_ERREURNONGEREE;

public class ApplicationExceptions extends Exception {

    public ApplicationExceptions(IOException e) {
        logger.error(e.getMessage());
    }

    public ApplicationExceptions(Messages.ErreurMessages err) {
        logger.error(err.getMessageErreur());
        if (err.equals(ERREUR_GENERIC)) {
            logger.error(SORTIE_PGM_SUR_ERREURNONGEREE.toString());
        }
    }

    public ApplicationExceptions(FileNotFoundException e) {
        logger.error(e.getMessage());
    }


    public ApplicationExceptions(Exception e) {
        logger.error(e.getMessage());
    }

}
