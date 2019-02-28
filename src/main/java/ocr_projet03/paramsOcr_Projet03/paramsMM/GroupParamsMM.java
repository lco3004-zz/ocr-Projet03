package ocr_projet03.paramsOcr_Projet03.paramsMM;

import ocr_projet03.paramsOcr_Projet03.UnParam;

public enum GroupParamsMM {
        NbDePositions( 4,3,6),
        NbCouleurs(CouleursMastermind.values().length,6,CouleursMastermind.values().length),
        NbreMaxDeBoucleChercheCodeSecret(100,10,500),
        NbreEssais(12,8,12),
        DoublonAutorise(false),
        CaseVideAUtorise (false);


        private UnParam unParam;

        GroupParamsMM(Integer valdef, Integer valmin,Integer valmax) {
            unParam = new UnParam<>(valdef, Integer.class.getSimpleName(),valmin,valmax);
        }

        GroupParamsMM(Boolean valdef) {
            unParam = new UnParam<>(valdef,Boolean.class.getSimpleName());
        }

     public UnParam getUnParam() {
         return unParam;
     }


 }
