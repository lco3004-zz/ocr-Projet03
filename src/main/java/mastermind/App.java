package mastermind;

import mastermind.constantes.CouleursMastermind;
import mastermind.exceptionMastermind.ExceptionMastermind;
import mastermind.messagesTexteMastermind.InfosMessages;

import mastermind.partieMastermind.ChoixCodeSecret;

import java.util.ArrayList;

import static mastermind.logMastermind.logApplicatif.getInstance;
import static mastermind.logMastermind.logApplicatif.logger;

/**
 * Hello world!
 *
 */

public class App 
{
    public static void main( String[] args )
    {
        try {
            //Creation du Singleton qui gere les logs (log4j2)
            getInstance(App.class.getSimpleName());

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
