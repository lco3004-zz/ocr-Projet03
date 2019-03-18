package fr.ocr.modeconsole;

import fr.ocr.utiles.Messages;

import static fr.ocr.utiles.Logs.logger;

public interface EcrireSurEcran {
    default void Display() {
        logger.error(Messages.ErreurMessages.ERREUR_GENERIC);
    }
}
