package fr.ocr.utiles;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * pour verificaiton commit github (a disparudu repo) 03/03/02019
 */
public class Logs {
    private static Logs ourInstance = null;

    public static Logger logger;

    public static Logs getInstance(String nomAppelant) {
        if (ourInstance==null)
            ourInstance = new Logs(nomAppelant);
        return ourInstance;
    }

    private Logs(String nomAppelant) {
        logger = LogManager.getRootLogger();
    }

}
