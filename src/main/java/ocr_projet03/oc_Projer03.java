package ocr_projet03;


import ocr_projet03.modeConsole.LibellesMenu_PM;
import ocr_projet03.modeConsole.LibellesMenu_Principal;
import ocr_projet03.modeConsole.LibellesMenu_MM;
import ocr_projet03.paramsOcr_Projet03.paramsMM.CouleursMastermind;
import ocr_projet03.exceptionOcr_Projet03.ExceptionMastermind;

import ocr_projet03.partieMastermind.ChoixCodeSecret;

import java.util.ArrayList;

import static ocr_projet03.logsOcr_Projet03.logApplicatif.getInstance;
import static ocr_projet03.logsOcr_Projet03.logApplicatif.logger;
import static ocr_projet03.messagesTexteOcr_Projet03.InfosMessages.FinNormale_Application;
import static ocr_projet03.messagesTexteOcr_Projet03.InfosMessages.Lancement_Application;

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

            logger.info(Lancement_Application.getMessageInfos());

            for (LibellesMenu_Principal v : LibellesMenu_Principal.values()) {
                System.out.println(v.getLibelle());
            }

            for (LibellesMenu_MM t : LibellesMenu_MM.values()) {
                System.out.println(t.getLibelle());
            }

            for (LibellesMenu_PM t : LibellesMenu_PM.values()) {
                System.out.println(t.getLibelle());
            }

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


            logger.info(FinNormale_Application.getMessageInfos());
        }
        catch (ExceptionMastermind e) {
            logger.fatal(e);
            System.exit(-1);
        }
    }
}
