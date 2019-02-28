package ocr_projet03.paramsOcr_Projet03.paramsMM;

import ocr_projet03.paramsOcr_Projet03.UnParam;

public enum GroupParamsMM {
        NbDePositions( (Integer)4,(Integer)3,(Integer)6),
        NbCouleurs((Integer)CouleursMastermind.values().length,(Integer)6,(Integer)CouleursMastermind.values().length),
        NbreMaxDeBoucleChercheCodeSecret((Integer)100,(Integer)10,(Integer)500),
        NbreEssais((Integer)12,(Integer)8,(Integer)12),
        DoublonAutorise((Boolean)false),
        CaseVideAUtorise ((Boolean)false);


        private UnParam unParam;

        GroupParamsMM(Integer valdef, Integer valmin,Integer valmax) {
            unParam = new UnParam<Integer>(valdef, Integer.class.getSimpleName(),0,7);
        }

        GroupParamsMM(Boolean valdef) {
            unParam = new UnParam<Boolean>(valdef,Boolean.class.getSimpleName());
        }

     public UnParam getUnParam() {
         return unParam;
     }


 }
