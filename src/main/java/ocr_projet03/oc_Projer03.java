package ocr_projet03;


import ocr_projet03.modeConsole.LibellesJeux;
import ocr_projet03.modeConsole.Menu_Principal;
import ocr_projet03.modeConsole.Menu_Secondaire;
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

            Menu_Principal menu_principal = new Menu_Principal();
            Menu_Secondaire menu_secondaireMM = new Menu_Secondaire(LibellesJeux.MASTERMIND.toString());
            Menu_Secondaire menu_secondairePM = new Menu_Secondaire(LibellesJeux.PLUSMOINS.toString());
            menu_principal.RunMenuPrincipal();
            menu_secondaireMM.RunMenuSecondaire();
            menu_secondairePM.RunMenuSecondaire();


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
