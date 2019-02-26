package mastermind.partieMastermind;

import mastermind.constantesMastermind.CouleursMastermind;
import mastermind.constantesMastermind.ValeursConstantes;
import mastermind.exceptionMastermind.ExceptionMastermind;
import mastermind.logMastermind.TracesLog;
import mastermind.messagesTexteMastermind.ErreurMessages;
import mastermind.parametrageMastermind.GestionDesParametres;
import mastermind.parametrageMastermind.NomsDesParametresApplicatif;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Création du Code Secret comprenant X positions , X étant égal au paramètre "nombreDePositions"
 * Chaque position contient une couleur représentée sous forme de nombre pris parmi le segment 0..nombreDeCouleurs-1
 * Le choix de couleur est aléatoire
 */
public class ChoixCodeSecret  {
    /**
     *
     */
    private int nombreDePositions, nombreDeCouleurs;
    private ArrayList<Byte> codeSecret ;
    private ErreurMessages errMsg ;
    private CouleursMastermind [] ligneSecrete ;
    private GestionDesParametres gestionDesParametres = GestionDesParametres.getInstance();

    public ChoixCodeSecret() throws ExceptionMastermind {
            Object tmpRetour;
            Byte valeurAleatoire;
            Boolean doublonAutorise;

            codeSecret = new ArrayList<>();
            DecimalFormat df = new DecimalFormat("#");
            df.setRoundingMode(RoundingMode.HALF_UP);

            nombreDePositions = 0;
            tmpRetour=gestionDesParametres.getParam(NomsDesParametresApplicatif.NbDePositions);
            if ( tmpRetour instanceof Short)
                nombreDePositions = (Short)tmpRetour;
            else
                throw new ExceptionMastermind(ErreurMessages.TypeParamIncorrect);


            nombreDeCouleurs =  0;
            tmpRetour=gestionDesParametres.getParam(NomsDesParametresApplicatif.NbCouleurs);
            if ( tmpRetour instanceof Short)
                nombreDeCouleurs = (Short)tmpRetour;
            else
                throw new ExceptionMastermind(ErreurMessages.TypeParamIncorrect);

            tmpRetour = gestionDesParametres.getParam(NomsDesParametresApplicatif.DoublonAutorise);
            if ( tmpRetour instanceof Boolean)
                doublonAutorise = (Boolean) tmpRetour;

            else
                throw new ExceptionMastermind(ErreurMessages.TypeParamIncorrect);

            for (int placeOccupee =0,  nbreDeBoucles=0; placeOccupee< this.nombreDePositions && nbreDeBoucles < ValeursConstantes.nombreMaxDeBoucleCherhceCodeSecret  ; nbreDeBoucles++) {
                valeurAleatoire = (byte)(Byte.parseByte(df.format(Math.random()*100)) % nombreDeCouleurs );

                if (!doublonAutorise) {
                    if (!codeSecret.contains(valeurAleatoire)) {
                        codeSecret.add(valeurAleatoire);
                        placeOccupee++;
                    }
                }
                else {
                    codeSecret.add(valeurAleatoire);
                    placeOccupee++;
                }
            }
            //pas assez de positions remplies - le random n'a pas marché
            if (codeSecret.size() < nombreDePositions) {
                errMsg = ErreurMessages.ChoixRandomNonComplet;
                tracesLog.LogaMoi(errMsg.getMessageDuCodeErreur());

                CouleursMastermind[] couleursMastermind = CouleursMastermind.values();
                codeSecret.clear();
                for (int i =0; i<nombreDePositions;i++ ) {
                    codeSecret.add(couleursMastermind[i].getValeurFacialeDeLaCouleur());
                }

            }
            ligneSecrete  = new CouleursMastermind[this.nombreDePositions ];
            int i=0;
            for (Byte v: codeSecret) {
                ligneSecrete[i++] = CouleursMastermind.values()[(int)v];
            }
    }

    /**
     *
     */
    public ArrayList<Byte> getCodeSecret() {
        return codeSecret;
    }

    /**
     *
     */
    public CouleursMastermind[] getLigneSecrete() {
        return ligneSecrete;
    }
}

