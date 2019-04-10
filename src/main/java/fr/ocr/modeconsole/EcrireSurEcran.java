package fr.ocr.modeconsole;

import fr.ocr.utiles.Messages;

import static fr.ocr.utiles.Logs.logger;

/**
 * <p>
 *
 * @author laurent cordier
 * interface pour affichage ecran
 * </p>
 */
public interface EcrireSurEcran {
    /**
     * par default , display log une erreur
     */
    default void display() {
        logger.error(Messages.ErreurMessages.ERREUR_GENERIC);
    }
}
/*
 * ***************************************************************************************************************
 *  the end
 * ***************************************************************************************************************
 */