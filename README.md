# Projet : ocr-Projet03 (OpenClassRooms)
## Revision du périmètre
- Je pense avoir mieux cerné le fonctionnement du parcours en observant:
  - la révision de la cible(suppression du problème NPComplet) : arrêt immédiat de la mise en oeuvre du framework fork/join,  
  - l'évaluation du "comment ai-je pensé l'application" : inutile de tester autre que "je lance, ca marche".
 - Ce progrès m'offre un gain de temps significatif , il est superflux et hors périmètre de présenter des slides avec couverture fonctionnelle etc..
## Livrable (full stop)
- Fichier de Configuration :
  - se trouve  sous ./target/classes : params.properties
- Mode développeur: 
  - commande en ligne -d  ou --develo
- Génération de l"exécutable :
  - projet Maven
  - exécutable est sous ./target/projet03-1.0.jar
- jdk
  - jdk 8
- Lancement de l'application 
  - en mode développeur : java.exe -Dfile'.'encoding=windows-1252 -jar .\target\projet03-1.0.jar -d
  - en mode normal :  java.exe -Dfile'.'encoding=windows-1252 -jar .\target\projet03-1.0.jar
## Notes
- Paramètres :
  - Valeurs :
    - DOUBLON_AUTORISE=true  (Mastemrind - choix entre puissance(n,p) ou Arrangement(n,p))
    - NOMBRE_D_ESSAIS=12     (defaut 12, min 4, max 30)
    - NOMBRE_DE_COULEURS=10   (MasterMind:   (defaut 10, min 6, max = 10)
    - FRAUDE_AUTORISEE=true  (Mastermind : choix entre réponse conforme ou non à l'essai mode défenseur)
    - NOMBRE_DE_POSITIONS=4   (defaut 4, min 4, max 8)
    - MODE_DEVELOPPEUR=true
    - NOMBRE_MAXI_DE_BOUCLES_RANDOMIZE=100  (nombre de tentive de tirage aléatoires - si échec, choix par défaut)
  - fichier construit avec valeur par défaut si n'existe pas - si impossible, valeur par défaut.
  - si paramètre incorrect ou hors plage min-max, valeur par défaut
  - Contrôle cohérence : si nombre de couleurs est < nombre de position et Doublon pas autorisé, => valeur par défaut.
- Jeu +/-
  - un seul source pour le jeu lui-même
  - le jeu utilise les développements communs : accès au fichier paramètre, gestion exception, constantes du pgm
  - Le choix se fait par les menus commun aux deux jeux mastermind et Plus/Moins.
  - JUnit :   uniquement sur l'enchainement des menus 




      
 

 
