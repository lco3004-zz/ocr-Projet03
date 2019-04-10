# Projet : ocr-Projet03 - JeuPlusMoins
## Revision du périmètre
- Je pense avoir mieux cerné le fonctionnement du parcours en observant le changement de périmètre du projet 3 (abandon Mastermind).
- Le prochain projet sera, je me le souhaite,  mieux réalisé , promis. 
   - lecture complète des sections avec évaluations de la pertinence des renvois vers d'autre cours.
   - prévision du temps et annonce des cours à suivre => calendrier 
   - évaluation de la pertinence du projet à réaliser au regard des cours proposé dans le parcours.
   - choix d'autre cours en relation avec le projet si necessaire et abandon des cours du parcours sans lien avec le projet à réaliser
   - si écart trop important entre cours proposés dans le parcours et cible du projet, discussion avec Mentor pour éviter la perte de temps vraiment gigantesque du projet  3
   - Merci pour votre compréhension et soutien    
## Livrable
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
- Couverture technique et fonctionnelle
  - mise en oeuvre de l'héritage, de l'interfacage, de l'abstraction, de lambda et methode de classe
  - mise en oeuvre des classes fonctionnelles (function, provider)
  - création de package par fonctionnalité
  - utilisation simple de stream 
  - Utilisation de méthode statique
  - Utilisation de EnumSet (Enum)
  - Utilisation de collection (ArrayList,...)
  - utilisation de la portée (private, package private, protected, public).
  - commentaires du code, camelcase, javadoc
  - gestionnaire d'exception
  - utilisation de la classe scanner.
  - non utilisé : Bloc de code , NIO, Regex (très limité pour Scanner) , Concurrence, Event Handling etc...
- Sources Documentaire:
  - "Oracle , Java the complete reference eleventh edition , Herbert Schildt, Oracle Press"
  - "Eyrolles , Apprenez à programmer en Java - 2ième et 3ieme éditions, OpenClassRooms / Site du zéro, Cyrille Herby - Eyrolles edition"
  - "Dossier Informatique et Sciences du Numérique , APMEP no 503 Mastermind : Des preuves par ordinateur Bernard Langer(*)"




      
 

 
