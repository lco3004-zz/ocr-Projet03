package fr.ocr.utiles;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Singleton : pour tracer , il suffit d'écrire logger.{info|debug|erro} ( {chaine de caractère non null} ) ;
 * l'istance doit être créer dans la méthode principale
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
