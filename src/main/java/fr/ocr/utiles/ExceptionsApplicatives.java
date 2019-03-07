package fr.ocr.utiles;

import static fr.ocr.utiles.LogApplicatifs.logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionsApplicatives extends Exception {

    public ExceptionsApplicatives(IOException e) {
        logger.error(e.getMessage());
    }

    public ExceptionsApplicatives(Messages.ErreurMessages err) {
        logger.error(err.getMessageErreur());
    }

    public ExceptionsApplicatives(FileNotFoundException e) {
        logger.error(e.getMessage());
    }


    public ExceptionsApplicatives(Exception e) {
        logger.error(e.getMessage());
    }

}
