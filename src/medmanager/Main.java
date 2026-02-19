package medmanager;

	import java.util.Scanner;

	public class Main {

	    //Constantes
	    static final int MAX_PATIENTS = 100;
	    static final int MAX_SERVICES = 10;

	    // Données patients (tableaux parallèles)
	    static String[] nomsPatients = new String[MAX_PATIENTS];
	    static String[] prenomsPatients = new String[MAX_PATIENTS];
	    static int[] anneesNaissance = new int[MAX_PATIENTS];
	    static int[] servicePatient = new int[MAX_PATIENTS]; // index du service pour chaque patient
	    static int nbPatients = 0;

	    //Données services
	    static String[] nomsServices = new String[MAX_SERVICES];
	    static int[] capacitesServices = new int[MAX_SERVICES];
	    static int[] nbPatientsService = new int[MAX_SERVICES];
	    static int nbServices = 0;

	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        int choix;

	        // Initialiser quelques services (ex 4)
	        initServices();

	        do {
	            afficherMenu();
	            choix = lireChoix(scanner);

	            switch (choix) {
	                case 1 -> ajouterPatient(scanner);
	                case 2 -> afficherPatients();
	                case 3 -> rechercherPatient(scanner);
	                case 4 -> afficherStatistiques();
	                case 5 -> afficherPatientsTriesParNom();
	                case 0 -> System.out.println("\n👋 Au revoir !");
	                default -> System.out.println("⚠ Choix invalide.");
	            }
	        } while (choix != 0);

	        scanner.close();
	    }

	    //Affichage du menu 
	    static void afficherMenu() {
	        System.out.println("\n══════ MedManager v0.1 ══════");
	        System.out.println("  1. ➕ Ajouter un patient");
	        System.out.println("  2. 📋 Afficher tous les patients");
	        System.out.println("  3. 🔍 Rechercher un patient");
	        System.out.println("  4. 📊 Statistiques");
	        System.out.println("  5. 🔤 Trier par nom (A→Z)");
	        System.out.println("  0. 🚪 Quitter");
	        System.out.print("Votre choix : ");
	    }

	   
	    static int lireChoix(Scanner scanner) {
	        while (!scanner.hasNextInt()) {
	            System.out.print("⚠ Entrez un nombre : ");
	            scanner.next(); 
	        }
	        int choix = scanner.nextInt();
	        scanner.nextLine(); 
	        return choix;
	    }

	    //Ex 4 : initialiser quelques services 
	    static void initServices() {
	        nomsServices[0] = "Urgences";
	        capacitesServices[0] = 4;
	        nbPatientsService[0] = 0;

	        nomsServices[1] = "Cardiologie";
	        capacitesServices[1] = 2;
	        nbPatientsService[1] = 0;

	        nomsServices[2] = "Pediatrie";
	        capacitesServices[2] = 3;
	        nbPatientsService[2] = 0;

	        nbServices = 3;
	    }

	    //Ex4 : choisir un service en respectant la capacité
	    static int choisirService(Scanner scanner) {
	        // vérifier s'il existe au moins un service avec de la place
	        boolean dispo = false;
	        for (int i = 0; i < nbServices; i++) {
	            if (nbPatientsService[i] < capacitesServices[i]) {
	                dispo = true;
	                break;
	            }
	        }
	        if (!dispo) return -1;

	        int choixService;
	        do {
	            System.out.println("\n--- Choisir un service ---");
	            for (int i = 0; i < nbServices; i++) {
	                int places = capacitesServices[i] - nbPatientsService[i];
	                System.out.println((i + 1) + ". " + nomsServices[i] + " (places: " + places + ")");
	            }
	            System.out.print("Votre choix (1-" + nbServices + ") : ");
	            choixService = lireChoix(scanner);

	            if (choixService < 1 || choixService > nbServices) {
	                System.out.println("⚠ Choix invalide.");
	                choixService = -1;
	                continue;
	            }

	            int idx = choixService - 1;
	            if (nbPatientsService[idx] >= capacitesServices[idx]) {
	                System.out.println("⚠ Service plein, choisissez un autre.");
	                choixService = -1;
	            }
	        } while (choixService < 1);

	        return choixService - 1;
	    }

	    //Ajouter un patient 
	    static void ajouterPatient(Scanner scanner) {
	        if (nbPatients >= MAX_PATIENTS) {
	            System.out.println("⚠ Capacité maximale atteinte !");
	            return;
	        }

	        System.out.println("\n--- Nouveau Patient ---");

	        System.out.print("Nom : ");
	        String nom = scanner.nextLine();
	        while (nom.trim().isEmpty()) { 
	            System.out.print("⚠ Nom obligatoire. Re-saisir : ");
	            nom = scanner.nextLine();
	        }
	        nomsPatients[nbPatients] = nom;

	        System.out.print("Prénom : ");
	        prenomsPatients[nbPatients] = scanner.nextLine();

	        // Ex1 : validation âge (0..150) via année de naissance
	        int annee;
	        int age;
	        do {
	            System.out.print("Année de naissance : ");
	            annee = lireChoix(scanner);
	            age = 2026 - annee;

	            if (age < 0 || age > 150) {
	                System.out.println("❌ Âge invalide (" + age + "). Réessayez.");
	            }
	        } while (age < 0 || age > 150);

	        anneesNaissance[nbPatients] = annee;

	        // Ex4 : choisir service + vérifier capacité
	        int idxService = choisirService(scanner);
	        if (idxService == -1) {
	            System.out.println("❌ Aucun service disponible (capacité pleine).");
	            return;
	        }
	        servicePatient[nbPatients] = idxService;
	        nbPatientsService[idxService]++;

	        nbPatients++;

	        System.out.println("✅ Patient enregistré (" + age + " ans) dans " + nomsServices[idxService]);
	    }

	    //Afficher tous les patients (Ex5)
	    static void afficherPatients() {
	        if (nbPatients == 0) {
	            System.out.println("\nAucun patient enregistré.");
	            return;
	        }

	        System.out.println("\n--- Liste des Patients ---");

	        System.out.println("+----+-----------------+-----------------+------+--------------+");
	        System.out.printf("| %-2s | %-15s | %-15s | %-4s | %-12s |\n", "#", "Nom", "Prénom", "Âge", "Service");
	        System.out.println("+----+-----------------+-----------------+------+--------------+");

	        for (int i = 0; i < nbPatients; i++) {
	            int age = 2026 - anneesNaissance[i];
	            String service = (nbServices > 0) ? nomsServices[servicePatient[i]] : "-";

	            System.out.printf("| %-2d | %-15s | %-15s | %-4d | %-12s |\n",
	                    (i + 1), nomsPatients[i], prenomsPatients[i], age, service);
	        }

	        System.out.println("+----+-----------------+-----------------+------+--------------+");
	        System.out.println("Total : " + nbPatients + " patient(s)");
	    }

	    //Rechercher un patient par le nom 
	    static void rechercherPatient(Scanner scanner) {
	        System.out.print("\nRechercher (nom) : ");
	        String recherche = scanner.nextLine().toLowerCase();
	        boolean trouve = false;

	        for (int i = 0; i < nbPatients; i++) {
	            if (nomsPatients[i].toLowerCase().contains(recherche)) {
	                int age = 2026 - anneesNaissance[i];
	                String service = nomsServices[servicePatient[i]];
	                System.out.println("→ " + prenomsPatients[i] + " " + nomsPatients[i]
	                        + " (" + age + " ans) - " + service);
	                trouve = true;
	            }
	        }

	        if (!trouve) {
	            System.out.println("Aucun résultat pour \"" + recherche + "\"");
	        }
	    }

	    //Ex2 : Statistiques Basiques
	    static void afficherStatistiques() {
	        if (nbPatients == 0) {
	            System.out.println("\nAucun patient enregistré.");
	            return;
	        }

	        int sommeAges = 0;
	        int ageMin = 2026 - anneesNaissance[0];
	        int ageMax = 2026 - anneesNaissance[0];

	        for (int i = 0; i < nbPatients; i++) {
	            int age = 2026 - anneesNaissance[i];
	            sommeAges += age;
	            if (age < ageMin) ageMin = age;
	            if (age > ageMax) ageMax = age;
	        }

	        double ageMoyen = (double) sommeAges / nbPatients;

	        System.out.println("\n--- 📊 Statistiques ---");
	        System.out.println("Nombre total de patients : " + nbPatients);
	        System.out.printf("Âge moyen : %.2f%n", ageMoyen);
	        System.out.println("Plus jeune : " + ageMin + " ans");
	        System.out.println("Plus vieux : " + ageMax + " ans");
	    }

	    //Ex3 : Tri par nom 
	    static void afficherPatientsTriesParNom() {
	        if (nbPatients == 0) {
	            System.out.println("\nAucun patient enregistré.");
	            return;
	        }
	        trierParNomBubble();
	        afficherPatients();
	    }

	    static void trierParNomBubble() {
	        for (int i = 0; i < nbPatients - 1; i++) {
	            for (int j = 0; j < nbPatients - 1 - i; j++) {

	                if (nomsPatients[j].compareToIgnoreCase(nomsPatients[j + 1]) > 0) {

	                   
	                    String tmpNom = nomsPatients[j];
	                    nomsPatients[j] = nomsPatients[j + 1];
	                    nomsPatients[j + 1] = tmpNom;

	                   
	                    String tmpPrenom = prenomsPatients[j];
	                    prenomsPatients[j] = prenomsPatients[j + 1];
	                    prenomsPatients[j + 1] = tmpPrenom;

	                  
	                    int tmpAnnee = anneesNaissance[j];
	                    anneesNaissance[j] = anneesNaissance[j + 1];
	                    anneesNaissance[j + 1] = tmpAnnee;

	                   
	                    int tmpService = servicePatient[j];
	                    servicePatient[j] = servicePatient[j + 1];
	                    servicePatient[j + 1] = tmpService;
	                }
	            }
	        }
	    }
	}
