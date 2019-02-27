package mastermind.parametrageApplication;

import mastermind.constantes.CouleursMastermind;

 public enum ParametresDuMasterMind {
        NbDePositions( (Integer)4),
        NbCouleurs((Integer)CouleursMastermind.values().length),
        NbreMaxDeBoucleCherhceCodeSecret((Integer)100),
        DoublonAutorise((Boolean)false),
        CaseVideAUtorise ((Boolean)false);

        private Boolean aBoolean;
        private Integer aInteger;

        ParametresDuMasterMind(int i) {
                aInteger=i;
        }

        ParametresDuMasterMind(boolean b) {
                aBoolean=b;
        }

     public Boolean getaBoolean() {
         return aBoolean;
     }

     public Integer getaInteger() {
         return aInteger;
     }
 }
