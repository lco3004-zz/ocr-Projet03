package ocr_projet03;

import ocr_projet03.constantes.CouleursMastermind;
import ocr_projet03.exceptionMastermind.ExceptionMastermind;
import ocr_projet03.messagesTexteMastermind.InfosMessages;

import ocr_projet03.partieMastermind.ChoixCodeSecret;

import java.util.ArrayList;

import static ocr_projet03.logMastermind.logApplicatif.getInstance;
import static ocr_projet03.logMastermind.logApplicatif.logger;

/**
 * Hello world!
 *
 */

public class oc_Projer03
{
    public static void main( String[] args )
    {
        try {
            //Creation du Singleton qui gere les logs (log4j2)
            getInstance(oc_Projer03.class.getSimpleName());

            logger.info(InfosMessages.Lancement_Application);

            ChoixCodeSecret choixCodeSecret = new ChoixCodeSecret();
            CouleursMastermind [] toutes = CouleursMastermind.values();
            String s = "";
            for (CouleursMastermind x: toutes) {
                s += x.toString() +", ";
            }
            logger.info("Toutes le couleurs = "+ s.substring(0,s.lastIndexOf(',')));

            ArrayList <Byte> arrayList = choixCodeSecret.getCodeSecret();
            CouleursMastermind[] ligneATrouver  = choixCodeSecret.getLigneSecrete();

             s = "";
            for (CouleursMastermind x: ligneATrouver) {
                s += x.toString() +", ";
            }
            logger.info("Combinaison secrete = "+s.substring(0,s.lastIndexOf(',')));


            logger.info(InfosMessages.FinNormale_Applicaiton);
        }
        catch (ExceptionMastermind e) {
            logger.fatal(e);
            System.exit(-1);
        }
    }
}
