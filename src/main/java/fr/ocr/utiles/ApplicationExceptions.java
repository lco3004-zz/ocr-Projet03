package fr.ocr.utiles;

import java.io.FileNotFoundException;
import java.io.IOException;

import static fr.ocr.utiles.Logs.logger;

public class ApplicationExceptions extends Exception {

    public ApplicationExceptions(IOException e) {
        logger.error(e.getMessage());
    }

    public ApplicationExceptions(Messages.ErreurMessages err) {
        logger.error(err.getMessageErreur());
    }

    public ApplicationExceptions(FileNotFoundException e) {
        logger.error(e.getMessage());
    }


    public ApplicationExceptions(Exception e) {
        logger.error(e.getMessage());
    }

}
