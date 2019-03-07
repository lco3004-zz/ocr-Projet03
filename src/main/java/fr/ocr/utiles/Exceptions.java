package fr.ocr.utiles;

import java.io.FileNotFoundException;
import java.io.IOException;

import static fr.ocr.utiles.Logs.logger;

public class Exceptions extends Exception {

    public Exceptions(IOException e) {
        logger.error(e.getMessage());
    }

    public Exceptions(Messages.ErreurMessages err) {
        logger.error(err.getMessageErreur());
    }

    public Exceptions(FileNotFoundException e) {
        logger.error(e.getMessage());
    }


    public Exceptions(Exception e) {
        logger.error(e.getMessage());
    }

}
