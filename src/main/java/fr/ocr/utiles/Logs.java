package fr.ocr.utiles;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * pour verificaiton commit github (a disparudu repo) 03/03/02019
 */
public class Logs {
    public static Logger logger;
    private static Logs ourInstance = null;

    private Logs(String nomAppelant) {
        logger = LogManager.getRootLogger();
    }

    public static Logs getInstance(String nomAppelant) {
        if (ourInstance == null)
            ourInstance = new Logs(nomAppelant);
        return ourInstance;
    }

}
