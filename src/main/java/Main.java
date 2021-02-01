import java.util.Scanner;

public class Main {

    public static final short MAX_PTS_VIE = 100;
    public static final short PTS_BOUCLIER = 25;
    public static final short MAX_ATTAQUE_ENNEMI = 5;
    public static final short MAX_VIE_ENNEMI = 30;
    public static final short MAX_ATTAQUE_JOUEUR = 5;
    public static final short REGENERATION_BOUCLIER_PAR_TOUR = 10;

    public static String nomPersonnage;
    public static short ptsDeVie;
    public static short ptsBouclier;
    public static short nbEnnemisTues = 0;
    public static boolean bouclierActif = true;

    static void initPersonnage(){
        System.out.println("Saisir le nom de votre personnage");
        //Initialisation d'un scanner afin de lire la saisie utilisateur dans la console
        Scanner scanner = new Scanner(System.in);
        //Exécution interrompue tant que l'utilisateur ne saisit pas quelque chose et appuie sur Entrée
        nomPersonnage = scanner.nextLine();
        ptsDeVie = MAX_PTS_VIE;
        ptsBouclier = PTS_BOUCLIER;
        System.out.println("OK " + Util.color(nomPersonnage, Color.GREEN) + " ! C'est parti !");
        //Je n'ai plus besoin de récupérer la saisie utilisateur, on ferme le scanner.

    }



    static boolean hasard (double pourcentageDeChance) {
        return pourcentageDeChance < Math.random();
    }
    /**
     * Méthode renvoyant un nombre au hasard entre 0 et le paramètre
     * @param max valeur maxmale que peut prendre le nombre
     * @return le nombre au hasard
     */
    static short nombreAuHasard(short max){ return (short) Math.round(Math.random() * max);
    }
    /**
     * Méthode réalisant l'attaque du joueur sur un ennemi
     * @param ptsDeVieEnnemi Nombre de points de vie de l'ennemi
     * @return Les points de vie de l'enemi après l'attaque
     */
    static short attaqueJoueur(short ptsDeVieEnnemi) {
        short dommages = nombreAuHasard(MAX_ATTAQUE_JOUEUR);
        System.out.print(Util.color(nomPersonnage, Color.GREEN)
                + " attaque l'" + Util.color("ennemi", Color.YELLOW) + " ! ");
        System.out.println("Il lui fait perdre " + Util.color(dommages, Color.PURPLE)
                + " points de dommages");
        ptsDeVieEnnemi -= dommages;
        return ptsDeVieEnnemi;
    }
    /**
     * Méthode qui affiche le nom du joueur et ses points de vie en rouge
     * et bouclier en bleu
     */
    static void afficherPersonnage(){
        System.out.print(Util.color(nomPersonnage, Color.GREEN) +
                " (" + Util.color(ptsDeVie, Color.RED));
        if(bouclierActif){
            System.out.print(" " + Util.color(ptsBouclier, Color.BLUE));
        }
        System.out.print(")");
    }

    /**
     * Méthode réalisant l'attaque de l'ennemi sur le personnage
     */
    static void attaqueEnnemi(){

        if(ptsDeVie <= 0){
            return;
        }

        short dommages = nombreAuHasard(MAX_ATTAQUE_ENNEMI);
        System.out.print("L'" + Util.color("ennemi", Color.YELLOW) + " attaque " +
                Util.color(nomPersonnage, Color.GREEN) +" ! ");
        System.out.print("Il lui fait " + dommages + " points de dommages ! ");
        if (ptsBouclier > 0){
            short dommagesBouclier = (short) Math.min(ptsBouclier, dommages);
            System.out.print("Le bouclier perd " + Util.color(dommagesBouclier, Color.BLUE) + " points. ");
            ptsBouclier -= dommagesBouclier;
            dommages -= dommagesBouclier;
        }
        if(dommages > 0){
            ptsDeVie -= dommages;
            System.out.print(Util.color(nomPersonnage, Color.GREEN) + " perd " +
                    Util.color(dommages, Color.RED) + " points de vie ! ");
        }
        System.out.println();
    }



    /**
     * Méthode réalisant l'attaque du joueur sur l'ennemi ou
     * de l'ennemi sur le joueur.
     * @param ennemi Points de vie de l'ennemi
     * @param joueurAttaque Est-ce que c'est le joueur qui attaque ?
     * @return les points de vie restant de l'ennemi après l'attaque
     */
    static short attaque (short ennemi, boolean joueurAttaque){
        //Si un des deux combattants est mort,
        if(ennemi <= 0 || ptsDeVie <= 0){
            //L'attaque n'a pas lieu
            return ennemi;
        }
        //Les deux combattants sont vivants !
        //Si c'est au tour du joueur d'attaquer, il attaque
        if(joueurAttaque){
            ennemi = attaqueJoueur(ennemi);
        }
        //Sinon c'est l'ennemi qui attaque
        else {
            attaqueEnnemi();
        }
        //Renvoyer le nombre de points de vie de l'ennemi
        return ennemi;
    }



    /**
     * Méthode permettant d'initialiser un nombre d'ennemis saisi avec un nombre au hasard de points de vie
     * @return le tableau contenant les points de vie de chaque ennemis
     */
    static short[] initEnnemis(){
        //Demander le nombre d'ennemis à combattre, récupérer la saisie
        System.out.println("Combien souhaitez-vous combattre d'ennemis ?");
        Scanner scanner = new Scanner(System.in);
        short nbEnnemis = scanner.nextShort();
        System.out.println("Génération des ennemis...");
        short[] ennemis = new short[nbEnnemis];
        //Pour chacun de ces ennemis, je dois déterminer ses points de vie et l'ajouter dans le tableau.
        for (short i = 0; i < nbEnnemis; i++) {
            ennemis[i] = nombreAuHasard(MAX_VIE_ENNEMI);
            System.out.println("Ennemi numéro " + (i + 1) + " : " + Util.color(ennemis[i], Color.PURPLE));
        }
        //Renvoie mon tableau
        return ennemis;
    }

    public static void main(String[] args) {

        initPersonnage();
        short[] numAdv = initEnnemis();
        short p = 0;
        for (short listAdv : numAdv) {
            System.out.println("Combat avec un ennemi possédant " + listAdv + " points de vie !");
            afficherPersonnage();
            System.out.println(" vs ennemi (" + Util.color(listAdv, Color.PURPLE) + ")");
            boolean QuiT1 = hasard(0.5);

            while (ptsDeVie > 0  && listAdv > 0) {
                listAdv = attaque(listAdv, QuiT1);
                QuiT1 = !QuiT1;
                afficherPersonnage();
                System.out.println(" vs ennemi (" + Util.color(listAdv, Color.PURPLE) + ")");
            }

            if (ptsDeVie <= 0){
                System.out.println(Util.color(nomPersonnage, Color.GREEN) + " est mort mais a tué " + nbEnnemisTues +" ennemis");
                break;
            }
            else {
                nbEnnemisTues++;
                p++;
                if (p == numAdv.length){
                    System.out.println(Util.color(nomPersonnage, Color.GREEN) + "a tué tous les ennemis !");
                    break;
                }
                System.out.println("L'ennemi est mort ! Au suivant !");
                if (ptsBouclier > 0) {
                    System.out.println("Régénération du bouclier : +10");
                    ptsBouclier += REGENERATION_BOUCLIER_PAR_TOUR;
                    if (ptsBouclier > 25) {
                        ptsBouclier = 25;
                    }

                }
                System.out.println("Saisisser S pour passer au combat suivant ou n'importe quoi d'autre pour fuir...");
                Scanner variable = new Scanner(System.in);
                String le = variable.nextLine();
                if (!le.equals("S")){
                    System.out.println("Courage fuyons !");
                    System.out.println("Vous avez tué" + nbEnnemisTues + " ennemi(s), mais êtes parti lâchement avant la fin...");
                }
                else {
                    continue;
                }
            }

        }

    }




}