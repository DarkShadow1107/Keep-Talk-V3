package game;

import java.util.HashMap;
import java.util.Map;

public class Localization {
    public enum Language {
        ENGLISH("English", "🇬🇧"),
        FRENCH("Français", "🇫🇷"),
        SPANISH("Español", "🇪🇸"),
        GERMAN("Deutsch", "🇩🇪"),
        ROMANIAN("Română", "🇷🇴"),
        DUTCH("Nederlands", "🇳🇱"),
        POLISH("Polski", "🇵🇱"),
        DANISH("Dansk", "🇩🇰"),
        RUSSIAN("Русский", "🇷🇺"),
        FINNISH("Suomi", "🇫🇮"),
        NORWEGIAN("Norsk", "🇳🇴"),
        SWEDISH("Svenska", "🇸🇪"),
        ICELANDIC("Íslenska", "🇮🇸");

        private final String displayName;
        private final String flag;

        Language(String displayName, String flag) {
            this.displayName = displayName;
            this.flag = flag;
        }

        public String getDisplayName() { return displayName; }
        public String getFlag() { return flag; }
        @Override
        public String toString() { return displayName; }
    }

    private static Language currentLanguage = Language.ENGLISH;
    private static Map<Language, Map<String, String>> dictionary = new HashMap<>();

    static {
        for (Language lang : Language.values()) {
            dictionary.put(lang, new HashMap<>());
        }
        initEnglish();
        initFrench();
        initSpanish();
        initGerman();
        initRomanian();
        initDutch();
        initPolish();
        initDanish();
        initRussian();
        initFinnish();
        initNorwegian();
        initSwedish();
        initIcelandic();
    }

    public static void setLanguage(Language lang) {
        currentLanguage = lang;
    }

    public static Language getLanguage() {
        return currentLanguage;
    }

    public static String get(String key) {
        String val = dictionary.get(currentLanguage).get(key);
        if (val == null) val = dictionary.get(Language.ENGLISH).get(key);
        if (val == null) return key;
        return val;
    }

    private static void initEnglish() {
        Map<String, String> m = dictionary.get(Language.ENGLISH);
        // Main Menu
        m.put("TITLE_MAIN", "KEEP TALKING");
        m.put("TITLE_SUB", "and Nobody Explodes");
        m.put("BTN_START", "START MISSION");
        m.put("BTN_FREEPLAY", "FREE PLAY");
        m.put("BTN_MANUAL", "TRAINING MANUAL");
        m.put("BTN_CREDITS", "CREDITS");
        m.put("BTN_EXIT", "EXIT GAME");
        m.put("BTN_RETURN", "RETURN TO MENU");
        m.put("LBL_LANGUAGE", "LANGUAGE");
        
        // Game UI
        m.put("GAME_ABORT", "ABORT");
        m.put("GAME_SERIAL", "SERIAL #");
        m.put("GAME_BATTERIES", "BATTERIES");
        m.put("GAME_PARALLEL", "PARALLEL PORT");
        m.put("GAME_INDICATORS", "INDICATORS");
        m.put("GAME_YES", "YES");
        m.put("GAME_NO", "NO");
        m.put("GAME_NONE", "NONE");
        m.put("GAME_WIN", "MISSION ACCOMPLISHED");
        m.put("GAME_LOSE", "MISSION FAILED");
        m.put("GAME_DEFUSED", "Bomb Defused!");
        m.put("GAME_ABORTED", "Mission Aborted");
        m.put("GAME_TIME", "Time ran out!");
        m.put("GAME_STRIKES", "Too many strikes!");
        m.put("LBL_TIME_REMAINING", "Time Remaining: ");
        
        // Credits
        m.put("CREDITS_TITLE", "CREDITS");
        m.put("CREDITS_ORIGINAL", "Original Game by Steel Crate Games");
        m.put("CREDITS_JAVA", "Java Implementation by:");
        m.put("CREDITS_PROGRAMMING", "Programming:");
        m.put("CREDITS_DESIGN", "Design:");
        m.put("CREDITS_TESTING", "Testing:");
        m.put("CREDITS_THANKS", "Thanks for playing!");
        m.put("BTN_BACK", "Back");
        
        // Manual
        m.put("MANUAL_TITLE", "BOMB DEFUSAL MANUAL");
        m.put("MANUAL_INTRO", "INTRODUCTION");
        m.put("MANUAL_INTRO_TEXT", "Welcome to the <b>Keep Talking and Nobody Explodes v3</b> Defusal Manual.");
        m.put("MANUAL_WARNING", "WARNING:");
        m.put("MANUAL_WARNING_TEXT", "Do not look at the bomb screen if you are the Expert. Communication is your only tool.");
        m.put("MANUAL_MODULES", "MODULES");
        m.put("MANUAL_MISSIONS", "MISSION DOSSIERS");
        
        // Module Names
        m.put("MOD_WIRES", "Wires");
        m.put("MOD_BUTTON", "The Button");
        m.put("MOD_KEYPAD", "Keypad");
        m.put("MOD_SIMON", "Simon Says");
        m.put("MOD_MAZE", "Maze");
        m.put("MOD_MEMORY", "Memory");
        m.put("MOD_MORSE", "Morse Code");
        m.put("MOD_COMPWIRES", "Complicated Wires");
        m.put("MOD_PASSWORD", "Password");
        m.put("MOD_WHOSONFIRST", "Who's on First");
        m.put("MOD_BINARY", "Binary");
        m.put("MOD_LOGIC", "Logic Gates");
        
        // Wires Module
        m.put("WIRES_DESC", "Wires are arranged horizontally. Cut the correct wire to defuse.");
        m.put("WIRES_RULE", "Always cut the last wire.");
        
        // Button Module
        m.put("BTN_STEP1", "Step 1: Determine whether to Tap or Hold");
        m.put("BTN_STEP2", "Step 2: Releasing a Held Button");
        m.put("BTN_BLUE_ABORT", "If the button is <b>Blue</b> and says <b>Abort</b>, <span class='highlight'>Hold</span> it.");
        m.put("BTN_DETONATE", "If there is more than 1 battery and the button says <b>Detonate</b>, <span class='highlight'>Tap</span> it.");
        m.put("BTN_WHITE_CAR", "If the button is <b>White</b> and there is a lit indicator <b>CAR</b>, <span class='highlight'>Hold</span> it.");
        m.put("BTN_FRK", "If there are more than 2 batteries and a lit indicator <b>FRK</b>, <span class='highlight'>Tap</span> it.");
        m.put("BTN_YELLOW", "If the button is <b>Yellow</b>, <span class='highlight'>Hold</span> it.");
        m.put("BTN_RED_HOLD", "If the button is <b>Red</b> and says <b>Hold</b>, <span class='highlight'>Tap</span> it.");
        m.put("BTN_OTHERWISE", "Otherwise, <span class='highlight'>Hold</span> it.");
        m.put("BTN_STRIP_INFO", "If you hold the button, a colored strip will light up. Release the button when the timer contains the corresponding digit:");
        m.put("BTN_STRIP_BLUE", "Blue Strip:");
        m.put("BTN_STRIP_WHITE", "White Strip:");
        m.put("BTN_STRIP_YELLOW", "Yellow Strip:");
        m.put("BTN_STRIP_OTHER", "Any other color:");
        
        // Colors
        m.put("COLOR_RED", "Red");
        m.put("COLOR_BLUE", "Blue");
        m.put("COLOR_GREEN", "Green");
        m.put("COLOR_YELLOW", "Yellow");
        m.put("COLOR_WHITE", "White");
        m.put("COLOR_BLACK", "Black");
        
        // Difficulty
        m.put("DIFF_EASY", "EASY");
        m.put("DIFF_MEDIUM", "MEDIUM");
        m.put("DIFF_HARD", "HARD");
        m.put("DIFF_EXPERT", "EXPERT");
        m.put("DIFF_INSANE", "INSANE");
        
        // Level Select
        m.put("LEVEL_SELECT", "SELECT MISSION");
        m.put("LBL_TIME", "Time");
        m.put("LBL_MODULES", "Modules");
        m.put("LBL_STRIKES", "Strikes");
        
        // Free Play
        m.put("FREEPLAY_TITLE", "FREE PLAY");
        m.put("FREEPLAY_TIME", "Time (seconds):");
        m.put("FREEPLAY_MODULES", "Number of Modules:");
        m.put("FREEPLAY_STRIKES", "Max Strikes:");
        m.put("FREEPLAY_START", "START");
    }

    private static void initFrench() {
        Map<String, String> m = dictionary.get(Language.FRENCH);
        m.put("TITLE_MAIN", "CONTINUEZ À PARLER");
        m.put("TITLE_SUB", "et Personne N'Explose");
        m.put("BTN_START", "COMMENCER MISSION");
        m.put("BTN_FREEPLAY", "JEU LIBRE");
        m.put("BTN_MANUAL", "MANUEL DE FORMATION");
        m.put("BTN_CREDITS", "CRÉDITS");
        m.put("BTN_EXIT", "QUITTER");
        m.put("BTN_RETURN", "RETOUR AU MENU");
        m.put("LBL_LANGUAGE", "LANGUE");
        m.put("GAME_ABORT", "ABANDONNER");
        m.put("GAME_SERIAL", "N° SÉRIE");
        m.put("GAME_BATTERIES", "PILES");
        m.put("GAME_PARALLEL", "PORT PARALLÈLE");
        m.put("GAME_INDICATORS", "INDICATEURS");
        m.put("GAME_YES", "OUI");
        m.put("GAME_NO", "NON");
        m.put("GAME_NONE", "AUCUN");
        m.put("GAME_WIN", "MISSION ACCOMPLIE");
        m.put("GAME_LOSE", "MISSION ÉCHOUÉE");
        m.put("GAME_DEFUSED", "Bombe Désamorcée!");
        m.put("GAME_ABORTED", "Mission Abandonnée");
        m.put("GAME_TIME", "Temps Écoulé!");
        m.put("GAME_STRIKES", "Trop d'Erreurs!");
        m.put("LBL_TIME_REMAINING", "Temps Restant: ");
        m.put("CREDITS_TITLE", "CRÉDITS");
        m.put("CREDITS_ORIGINAL", "Jeu Original par Steel Crate Games");
        m.put("CREDITS_JAVA", "Implémentation Java par:");
        m.put("CREDITS_PROGRAMMING", "Programmation:");
        m.put("CREDITS_DESIGN", "Design:");
        m.put("CREDITS_TESTING", "Tests:");
        m.put("CREDITS_THANKS", "Merci d'avoir joué!");
        m.put("BTN_BACK", "Retour");
        m.put("MANUAL_TITLE", "MANUEL DE DÉSAMORÇAGE");
        m.put("MANUAL_INTRO", "INTRODUCTION");
        m.put("MANUAL_WARNING", "ATTENTION:");
        m.put("MANUAL_MODULES", "MODULES");
        m.put("MANUAL_MISSIONS", "DOSSIERS DE MISSION");
        m.put("MOD_WIRES", "Fils");
        m.put("MOD_BUTTON", "Le Bouton");
        m.put("MOD_KEYPAD", "Clavier");
        m.put("MOD_SIMON", "Jacques a Dit");
        m.put("MOD_MAZE", "Labyrinthe");
        m.put("MOD_MEMORY", "Mémoire");
        m.put("MOD_MORSE", "Code Morse");
        m.put("LEVEL_SELECT", "SÉLECTIONNER MISSION");
        m.put("LBL_TIME", "Temps");
        m.put("LBL_MODULES", "Modules");
        m.put("LBL_STRIKES", "Erreurs");
        m.put("FREEPLAY_TITLE", "JEU LIBRE");
        m.put("FREEPLAY_START", "DÉMARRER");
        m.put("COLOR_RED", "Rouge");
        m.put("COLOR_BLUE", "Bleu");
        m.put("COLOR_GREEN", "Vert");
        m.put("COLOR_YELLOW", "Jaune");
        m.put("COLOR_WHITE", "Blanc");
    }

    private static void initSpanish() {
        Map<String, String> m = dictionary.get(Language.SPANISH);
        m.put("TITLE_MAIN", "SIGUE HABLANDO");
        m.put("TITLE_SUB", "y Nadie Explota");
        m.put("BTN_START", "INICIAR MISIÓN");
        m.put("BTN_FREEPLAY", "JUEGO LIBRE");
        m.put("BTN_MANUAL", "MANUAL DE ENTRENAMIENTO");
        m.put("BTN_CREDITS", "CRÉDITOS");
        m.put("BTN_EXIT", "SALIR");
        m.put("BTN_RETURN", "VOLVER AL MENÚ");
        m.put("LBL_LANGUAGE", "IDIOMA");
        m.put("GAME_ABORT", "ABORTAR");
        m.put("GAME_SERIAL", "N° SERIE");
        m.put("GAME_BATTERIES", "BATERÍAS");
        m.put("GAME_PARALLEL", "PUERTO PARALELO");
        m.put("GAME_INDICATORS", "INDICADORES");
        m.put("GAME_YES", "SÍ");
        m.put("GAME_NO", "NO");
        m.put("GAME_NONE", "NINGUNO");
        m.put("GAME_WIN", "MISIÓN CUMPLIDA");
        m.put("GAME_LOSE", "MISIÓN FALLIDA");
        m.put("GAME_DEFUSED", "¡Bomba Desactivada!");
        m.put("GAME_ABORTED", "Misión Abortada");
        m.put("GAME_TIME", "¡Se Acabó el Tiempo!");
        m.put("GAME_STRIKES", "¡Demasiados Fallos!");
        m.put("LBL_TIME_REMAINING", "Tiempo Restante: ");
        m.put("CREDITS_TITLE", "CRÉDITOS");
        m.put("CREDITS_ORIGINAL", "Juego Original por Steel Crate Games");
        m.put("CREDITS_JAVA", "Implementación Java por:");
        m.put("CREDITS_THANKS", "¡Gracias por jugar!");
        m.put("BTN_BACK", "Volver");
        m.put("MANUAL_TITLE", "MANUAL DE DESACTIVACIÓN");
        m.put("MANUAL_INTRO", "INTRODUCCIÓN");
        m.put("MANUAL_WARNING", "ADVERTENCIA:");
        m.put("MANUAL_MODULES", "MÓDULOS");
        m.put("MANUAL_MISSIONS", "EXPEDIENTES DE MISIÓN");
        m.put("MOD_WIRES", "Cables");
        m.put("MOD_BUTTON", "El Botón");
        m.put("LEVEL_SELECT", "SELECCIONAR MISIÓN");
        m.put("COLOR_RED", "Rojo");
        m.put("COLOR_BLUE", "Azul");
        m.put("COLOR_GREEN", "Verde");
        m.put("COLOR_YELLOW", "Amarillo");
        m.put("COLOR_WHITE", "Blanco");
    }

    private static void initGerman() {
        Map<String, String> m = dictionary.get(Language.GERMAN);
        m.put("TITLE_MAIN", "WEITERREDEN");
        m.put("TITLE_SUB", "und Niemand Explodiert");
        m.put("BTN_START", "MISSION STARTEN");
        m.put("BTN_FREEPLAY", "FREIES SPIEL");
        m.put("BTN_MANUAL", "TRAININGSHANDBUCH");
        m.put("BTN_CREDITS", "MITWIRKENDE");
        m.put("BTN_EXIT", "BEENDEN");
        m.put("BTN_RETURN", "ZURÜCK ZUM MENÜ");
        m.put("LBL_LANGUAGE", "SPRACHE");
        m.put("GAME_ABORT", "ABBRUCH");
        m.put("GAME_SERIAL", "SERIENNR.");
        m.put("GAME_BATTERIES", "BATTERIEN");
        m.put("GAME_PARALLEL", "PARALLELANSCHLUSS");
        m.put("GAME_INDICATORS", "INDIKATOREN");
        m.put("GAME_YES", "JA");
        m.put("GAME_NO", "NEIN");
        m.put("GAME_NONE", "KEINE");
        m.put("GAME_WIN", "MISSION ERFÜLLT");
        m.put("GAME_LOSE", "MISSION GESCHEITERT");
        m.put("GAME_DEFUSED", "Bombe Entschärft!");
        m.put("GAME_ABORTED", "Mission Abgebrochen");
        m.put("GAME_TIME", "Zeit Abgelaufen!");
        m.put("GAME_STRIKES", "Zu Viele Fehler!");
        m.put("LBL_TIME_REMAINING", "Verbleibende Zeit: ");
        m.put("CREDITS_TITLE", "MITWIRKENDE");
        m.put("CREDITS_ORIGINAL", "Originalspiel von Steel Crate Games");
        m.put("CREDITS_JAVA", "Java-Implementierung von:");
        m.put("CREDITS_THANKS", "Danke fürs Spielen!");
        m.put("BTN_BACK", "Zurück");
        m.put("MANUAL_TITLE", "BOMBEN-ENTSCHÄRFUNGSHANDBUCH");
        m.put("MANUAL_INTRO", "EINFÜHRUNG");
        m.put("MANUAL_WARNING", "WARNUNG:");
        m.put("MANUAL_MODULES", "MODULE");
        m.put("MANUAL_MISSIONS", "MISSIONSAKTEN");
        m.put("MOD_WIRES", "Drähte");
        m.put("MOD_BUTTON", "Der Knopf");
        m.put("LEVEL_SELECT", "MISSION AUSWÄHLEN");
        m.put("COLOR_RED", "Rot");
        m.put("COLOR_BLUE", "Blau");
        m.put("COLOR_GREEN", "Grün");
        m.put("COLOR_YELLOW", "Gelb");
        m.put("COLOR_WHITE", "Weiß");
    }

    private static void initRomanian() {
        Map<String, String> m = dictionary.get(Language.ROMANIAN);
        m.put("TITLE_MAIN", "CONTINUĂ SĂ VORBEȘTI");
        m.put("TITLE_SUB", "și Nimeni Nu Explodează");
        m.put("BTN_START", "ÎNCEPE MISIUNEA");
        m.put("BTN_FREEPLAY", "JOC LIBER");
        m.put("BTN_MANUAL", "MANUAL DE PREGĂTIRE");
        m.put("BTN_CREDITS", "CREDITE");
        m.put("BTN_EXIT", "IEȘIRE");
        m.put("BTN_RETURN", "ÎNAPOI LA MENIU");
        m.put("LBL_LANGUAGE", "LIMBĂ");
        m.put("GAME_ABORT", "ABANDONEAZĂ");
        m.put("GAME_SERIAL", "NR. SERIE");
        m.put("GAME_BATTERIES", "BATERII");
        m.put("GAME_PARALLEL", "PORT PARALEL");
        m.put("GAME_INDICATORS", "INDICATORI");
        m.put("GAME_YES", "DA");
        m.put("GAME_NO", "NU");
        m.put("GAME_NONE", "NICIUNUL");
        m.put("GAME_WIN", "MISIUNE ÎNDEPLINITĂ");
        m.put("GAME_LOSE", "MISIUNE EȘUATĂ");
        m.put("GAME_DEFUSED", "Bombă Dezamorsată!");
        m.put("GAME_ABORTED", "Misiune Abandonată");
        m.put("GAME_TIME", "Timpul a Expirat!");
        m.put("GAME_STRIKES", "Prea Multe Greșeli!");
        m.put("LBL_TIME_REMAINING", "Timp Rămas: ");
        m.put("CREDITS_TITLE", "CREDITE");
        m.put("CREDITS_ORIGINAL", "Joc Original de Steel Crate Games");
        m.put("CREDITS_JAVA", "Implementare Java de:");
        m.put("CREDITS_THANKS", "Mulțumim că ai jucat!");
        m.put("BTN_BACK", "Înapoi");
        m.put("MANUAL_TITLE", "MANUAL DE DEZAMORSARE");
        m.put("MANUAL_INTRO", "INTRODUCERE");
        m.put("MANUAL_WARNING", "ATENȚIE:");
        m.put("MANUAL_MODULES", "MODULE");
        m.put("MANUAL_MISSIONS", "DOSARE DE MISIUNI");
        m.put("MOD_WIRES", "Fire");
        m.put("MOD_BUTTON", "Butonul");
        m.put("LEVEL_SELECT", "SELECTEAZĂ MISIUNEA");
        m.put("COLOR_RED", "Roșu");
        m.put("COLOR_BLUE", "Albastru");
        m.put("COLOR_GREEN", "Verde");
        m.put("COLOR_YELLOW", "Galben");
        m.put("COLOR_WHITE", "Alb");
    }

    private static void initDutch() {
        Map<String, String> m = dictionary.get(Language.DUTCH);
        m.put("TITLE_MAIN", "BLIJF PRATEN");
        m.put("TITLE_SUB", "en Niemand Ontploft");
        m.put("BTN_START", "START MISSIE");
        m.put("BTN_FREEPLAY", "VRIJ SPEL");
        m.put("BTN_MANUAL", "TRAININGSHANDLEIDING");
        m.put("BTN_CREDITS", "CREDITS");
        m.put("BTN_EXIT", "AFSLUITEN");
        m.put("BTN_RETURN", "TERUG NAAR MENU");
        m.put("LBL_LANGUAGE", "TAAL");
        m.put("GAME_ABORT", "AFBREKEN");
        m.put("GAME_SERIAL", "SERIENR.");
        m.put("GAME_BATTERIES", "BATTERIJEN");
        m.put("GAME_PARALLEL", "PARALLELLE POORT");
        m.put("GAME_INDICATORS", "INDICATOREN");
        m.put("GAME_YES", "JA");
        m.put("GAME_NO", "NEE");
        m.put("GAME_NONE", "GEEN");
        m.put("GAME_WIN", "MISSIE VOLBRACHT");
        m.put("GAME_LOSE", "MISSIE MISLUKT");
        m.put("GAME_DEFUSED", "Bom Onschadelijk!");
        m.put("GAME_ABORTED", "Missie Afgebroken");
        m.put("GAME_TIME", "Tijd Op!");
        m.put("GAME_STRIKES", "Te Veel Fouten!");
        m.put("LBL_TIME_REMAINING", "Resterende Tijd: ");
        m.put("CREDITS_TITLE", "CREDITS");
        m.put("CREDITS_THANKS", "Bedankt voor het spelen!");
        m.put("BTN_BACK", "Terug");
        m.put("MANUAL_TITLE", "HANDLEIDING VOOR BOMMEN ONTMANTELEN");
        m.put("MANUAL_INTRO", "INTRODUCTIE");
        m.put("MANUAL_WARNING", "WAARSCHUWING:");
        m.put("MANUAL_MODULES", "MODULES");
        m.put("MANUAL_MISSIONS", "MISSIEDOSSIERS");
        m.put("MOD_WIRES", "Draden");
        m.put("MOD_BUTTON", "De Knop");
        m.put("LEVEL_SELECT", "SELECTEER MISSIE");
        m.put("COLOR_RED", "Rood");
        m.put("COLOR_BLUE", "Blauw");
        m.put("COLOR_GREEN", "Groen");
        m.put("COLOR_YELLOW", "Geel");
        m.put("COLOR_WHITE", "Wit");
    }

    private static void initPolish() {
        Map<String, String> m = dictionary.get(Language.POLISH);
        m.put("TITLE_MAIN", "MÓW DALEJ");
        m.put("TITLE_SUB", "a Nikt Nie Wybuchnie");
        m.put("BTN_START", "ROZPOCZNIJ MISJĘ");
        m.put("BTN_FREEPLAY", "GRA SWOBODNA");
        m.put("BTN_MANUAL", "PODRĘCZNIK SZKOLENIOWY");
        m.put("BTN_CREDITS", "TWÓRCY");
        m.put("BTN_EXIT", "WYJŚCIE");
        m.put("BTN_RETURN", "POWRÓT DO MENU");
        m.put("LBL_LANGUAGE", "JĘZYK");
        m.put("GAME_ABORT", "PRZERWIJ");
        m.put("GAME_SERIAL", "NR SERYJNY");
        m.put("GAME_BATTERIES", "BATERIE");
        m.put("GAME_PARALLEL", "PORT RÓWNOLEGŁY");
        m.put("GAME_INDICATORS", "WSKAŹNIKI");
        m.put("GAME_YES", "TAK");
        m.put("GAME_NO", "NIE");
        m.put("GAME_NONE", "BRAK");
        m.put("GAME_WIN", "MISJA WYKONANA");
        m.put("GAME_LOSE", "MISJA NIEUDANA");
        m.put("GAME_DEFUSED", "Bomba Rozbrojona!");
        m.put("GAME_ABORTED", "Misja Przerwana");
        m.put("GAME_TIME", "Czas Minął!");
        m.put("GAME_STRIKES", "Za Dużo Błędów!");
        m.put("LBL_TIME_REMAINING", "Pozostały Czas: ");
        m.put("CREDITS_TITLE", "TWÓRCY");
        m.put("CREDITS_THANKS", "Dziękujemy za grę!");
        m.put("BTN_BACK", "Wstecz");
        m.put("MANUAL_TITLE", "PODRĘCZNIK ROZBRAJANIA BOMB");
        m.put("MANUAL_INTRO", "WPROWADZENIE");
        m.put("MANUAL_WARNING", "OSTRZEŻENIE:");
        m.put("MANUAL_MODULES", "MODUŁY");
        m.put("MANUAL_MISSIONS", "MISJE");
        m.put("MOD_WIRES", "Przewody");
        m.put("MOD_BUTTON", "Przycisk");
        m.put("LEVEL_SELECT", "WYBIERZ MISJĘ");
        m.put("COLOR_RED", "Czerwony");
        m.put("COLOR_BLUE", "Niebieski");
        m.put("COLOR_GREEN", "Zielony");
        m.put("COLOR_YELLOW", "Żółty");
        m.put("COLOR_WHITE", "Biały");
    }

    private static void initDanish() {
        Map<String, String> m = dictionary.get(Language.DANISH);
        m.put("TITLE_MAIN", "BLIV VED MED AT TALE");
        m.put("TITLE_SUB", "og Ingen Eksploderer");
        m.put("BTN_START", "START MISSION");
        m.put("BTN_FREEPLAY", "FRI LEG");
        m.put("BTN_MANUAL", "TRÆNNINGSMANUAL");
        m.put("BTN_CREDITS", "CREDITS");
        m.put("BTN_EXIT", "AFSLUT");
        m.put("BTN_RETURN", "TILBAGE TIL MENU");
        m.put("LBL_LANGUAGE", "SPROG");
        m.put("GAME_ABORT", "AFBRYD");
        m.put("GAME_SERIAL", "SERIENR.");
        m.put("GAME_BATTERIES", "BATTERIER");
        m.put("GAME_PARALLEL", "PARALLELPORT");
        m.put("GAME_INDICATORS", "INDIKATORER");
        m.put("GAME_YES", "JA");
        m.put("GAME_NO", "NEJ");
        m.put("GAME_NONE", "INGEN");
        m.put("GAME_WIN", "MISSION FULDFØRT");
        m.put("GAME_LOSE", "MISSION MISLYKKEDES");
        m.put("GAME_DEFUSED", "Bombe Desarmeret!");
        m.put("GAME_ABORTED", "Mission Afbrudt");
        m.put("GAME_TIME", "Tiden Er Udløbet!");
        m.put("GAME_STRIKES", "For Mange Fejl!");
        m.put("LBL_TIME_REMAINING", "Resterende Tid: ");
        m.put("CREDITS_TITLE", "CREDITS");
        m.put("CREDITS_THANKS", "Tak fordi du spillede!");
        m.put("BTN_BACK", "Tilbage");
        m.put("MANUAL_TITLE", "BOMBEDEMONTERINGS MANUAL");
        m.put("MANUAL_INTRO", "INTRODUKTION");
        m.put("MANUAL_WARNING", "ADVARSEL:");
        m.put("MANUAL_MODULES", "MODULER");
        m.put("MANUAL_MISSIONS", "MISSIONER");
        m.put("MOD_WIRES", "Ledninger");
        m.put("MOD_BUTTON", "Knappen");
        m.put("LEVEL_SELECT", "VÆLG MISSION");
        m.put("COLOR_RED", "Rød");
        m.put("COLOR_BLUE", "Blå");
        m.put("COLOR_GREEN", "Grøn");
        m.put("COLOR_YELLOW", "Gul");
        m.put("COLOR_WHITE", "Hvid");
    }

    private static void initRussian() {
        Map<String, String> m = dictionary.get(Language.RUSSIAN);
        m.put("TITLE_MAIN", "ПРОДОЛЖАЙ ГОВОРИТЬ");
        m.put("TITLE_SUB", "и Никто Не Взорвётся");
        m.put("BTN_START", "НАЧАТЬ МИССИЮ");
        m.put("BTN_FREEPLAY", "СВОБОДНАЯ ИГРА");
        m.put("BTN_MANUAL", "УЧЕБНОЕ РУКОВОДСТВО");
        m.put("BTN_CREDITS", "АВТОРЫ");
        m.put("BTN_EXIT", "ВЫХОД");
        m.put("BTN_RETURN", "ВЕРНУТЬСЯ В МЕНЮ");
        m.put("LBL_LANGUAGE", "ЯЗЫК");
        m.put("GAME_ABORT", "ОТМЕНА");
        m.put("GAME_SERIAL", "СЕРИЙНЫЙ №");
        m.put("GAME_BATTERIES", "БАТАРЕИ");
        m.put("GAME_PARALLEL", "ПАРАЛЛЕЛЬНЫЙ ПОРТ");
        m.put("GAME_INDICATORS", "ИНДИКАТОРЫ");
        m.put("GAME_YES", "ДА");
        m.put("GAME_NO", "НЕТ");
        m.put("GAME_NONE", "НЕТ");
        m.put("GAME_WIN", "МИССИЯ ВЫПОЛНЕНА");
        m.put("GAME_LOSE", "МИССИЯ ПРОВАЛЕНА");
        m.put("GAME_DEFUSED", "Бомба Обезврежена!");
        m.put("GAME_ABORTED", "Миссия Отменена");
        m.put("GAME_TIME", "Время Вышло!");
        m.put("GAME_STRIKES", "Слишком Много Ошибок!");
        m.put("LBL_TIME_REMAINING", "Оставшееся Время: ");
        m.put("CREDITS_TITLE", "АВТОРЫ");
        m.put("CREDITS_THANKS", "Спасибо за игру!");
        m.put("BTN_BACK", "Назад");
        m.put("MANUAL_TITLE", "РУКОВОДСТВО ПО ОБЕЗВРЕЖИВАНИЮ БОМБ");
        m.put("MANUAL_INTRO", "ВВЕДЕНИЕ");
        m.put("MANUAL_WARNING", "ВНИМАНИЕ:");
        m.put("MANUAL_MODULES", "МОДУЛИ");
        m.put("MANUAL_MISSIONS", "ДОСЬЕ МИССИЙ");
        m.put("MOD_WIRES", "Провода");
        m.put("MOD_BUTTON", "Кнопка");
        m.put("LEVEL_SELECT", "ВЫБРАТЬ МИССИЮ");
        m.put("COLOR_RED", "Красный");
        m.put("COLOR_BLUE", "Синий");
        m.put("COLOR_GREEN", "Зелёный");
        m.put("COLOR_YELLOW", "Жёлтый");
        m.put("COLOR_WHITE", "Белый");
    }

    private static void initFinnish() {
        Map<String, String> m = dictionary.get(Language.FINNISH);
        m.put("TITLE_MAIN", "JATKA PUHUMISTA");
        m.put("TITLE_SUB", "eikä Kukaan Räjähdä");
        m.put("BTN_START", "ALOITA TEHTÄVÄ");
        m.put("BTN_FREEPLAY", "VAPAA PELI");
        m.put("BTN_MANUAL", "KOULUTUSOPAS");
        m.put("BTN_CREDITS", "TEKIJÄT");
        m.put("BTN_EXIT", "POISTU");
        m.put("BTN_RETURN", "TAKAISIN VALIKKOON");
        m.put("LBL_LANGUAGE", "KIELI");
        m.put("GAME_ABORT", "KESKEYTÄ");
        m.put("GAME_SERIAL", "SARJANRO");
        m.put("GAME_BATTERIES", "PARISTOT");
        m.put("GAME_PARALLEL", "RINNAKKAISPORTTI");
        m.put("GAME_INDICATORS", "INDIKAATTORIT");
        m.put("GAME_YES", "KYLLÄ");
        m.put("GAME_NO", "EI");
        m.put("GAME_NONE", "EI MITÄÄN");
        m.put("GAME_WIN", "TEHTÄVÄ SUORITETTU");
        m.put("GAME_LOSE", "TEHTÄVÄ EPÄONNISTUI");
        m.put("GAME_DEFUSED", "Pommi Purettu!");
        m.put("GAME_ABORTED", "Tehtävä Keskeytetty");
        m.put("GAME_TIME", "Aika Loppui!");
        m.put("GAME_STRIKES", "Liikaa Virheitä!");
        m.put("LBL_TIME_REMAINING", "Aikaa Jäljellä: ");
        m.put("CREDITS_TITLE", "TEKIJÄT");
        m.put("CREDITS_THANKS", "Kiitos pelaamisesta!");
        m.put("BTN_BACK", "Takaisin");
        m.put("MANUAL_TITLE", "POMMIN PURKUOPAS");
        m.put("MANUAL_INTRO", "JOHDANTO");
        m.put("MANUAL_WARNING", "VAROITUS:");
        m.put("MANUAL_MODULES", "MODUULIT");
        m.put("MANUAL_MISSIONS", "TEHTÄVÄT");
        m.put("MOD_WIRES", "Johdot");
        m.put("MOD_BUTTON", "Nappi");
        m.put("LEVEL_SELECT", "VALITSE TEHTÄVÄ");
        m.put("COLOR_RED", "Punainen");
        m.put("COLOR_BLUE", "Sininen");
        m.put("COLOR_GREEN", "Vihreä");
        m.put("COLOR_YELLOW", "Keltainen");
        m.put("COLOR_WHITE", "Valkoinen");
    }

    private static void initNorwegian() {
        Map<String, String> m = dictionary.get(Language.NORWEGIAN);
        m.put("TITLE_MAIN", "FORTSETT Å SNAKKE");
        m.put("TITLE_SUB", "og Ingen Eksploderer");
        m.put("BTN_START", "START OPPDRAG");
        m.put("BTN_FREEPLAY", "FRITT SPILL");
        m.put("BTN_MANUAL", "TRENINGSMANUAL");
        m.put("BTN_CREDITS", "CREDITS");
        m.put("BTN_EXIT", "AVSLUTT");
        m.put("BTN_RETURN", "TILBAKE TIL MENY");
        m.put("LBL_LANGUAGE", "SPRÅK");
        m.put("GAME_ABORT", "AVBRYT");
        m.put("GAME_SERIAL", "SERIENR.");
        m.put("GAME_BATTERIES", "BATTERIER");
        m.put("GAME_PARALLEL", "PARALLELLPORT");
        m.put("GAME_INDICATORS", "INDIKATORER");
        m.put("GAME_YES", "JA");
        m.put("GAME_NO", "NEI");
        m.put("GAME_NONE", "INGEN");
        m.put("GAME_WIN", "OPPDRAG FULLFØRT");
        m.put("GAME_LOSE", "OPPDRAG MISLYKKET");
        m.put("GAME_DEFUSED", "Bombe Desarmert!");
        m.put("GAME_ABORTED", "Oppdrag Avbrutt");
        m.put("GAME_TIME", "Tiden Er Ute!");
        m.put("GAME_STRIKES", "For Mange Feil!");
        m.put("LBL_TIME_REMAINING", "Gjenstående Tid: ");
        m.put("CREDITS_TITLE", "CREDITS");
        m.put("CREDITS_THANKS", "Takk for at du spilte!");
        m.put("BTN_BACK", "Tilbake");
        m.put("MANUAL_TITLE", "BOMBEDEAKTIVERINGSMANUAL");
        m.put("MANUAL_INTRO", "INTRODUKSJON");
        m.put("MANUAL_WARNING", "ADVARSEL:");
        m.put("MANUAL_MODULES", "MODULER");
        m.put("MANUAL_MISSIONS", "OPPDRAG");
        m.put("MOD_WIRES", "Ledninger");
        m.put("MOD_BUTTON", "Knappen");
        m.put("LEVEL_SELECT", "VELG OPPDRAG");
        m.put("COLOR_RED", "Rød");
        m.put("COLOR_BLUE", "Blå");
        m.put("COLOR_GREEN", "Grønn");
        m.put("COLOR_YELLOW", "Gul");
        m.put("COLOR_WHITE", "Hvit");
    }

    private static void initSwedish() {
        Map<String, String> m = dictionary.get(Language.SWEDISH);
        m.put("TITLE_MAIN", "FORTSÄTT PRATA");
        m.put("TITLE_SUB", "och Ingen Exploderar");
        m.put("BTN_START", "STARTA UPPDRAG");
        m.put("BTN_FREEPLAY", "FRITT SPEL");
        m.put("BTN_MANUAL", "TRÄNINGSMANUAL");
        m.put("BTN_CREDITS", "CREDITS");
        m.put("BTN_EXIT", "AVSLUTA");
        m.put("BTN_RETURN", "TILLBAKA TILL MENY");
        m.put("LBL_LANGUAGE", "SPRÅK");
        m.put("GAME_ABORT", "AVBRYT");
        m.put("GAME_SERIAL", "SERIENR.");
        m.put("GAME_BATTERIES", "BATTERIER");
        m.put("GAME_PARALLEL", "PARALLELLPORT");
        m.put("GAME_INDICATORS", "INDIKATORER");
        m.put("GAME_YES", "JA");
        m.put("GAME_NO", "NEJ");
        m.put("GAME_NONE", "INGA");
        m.put("GAME_WIN", "UPPDRAG SLUTFÖRT");
        m.put("GAME_LOSE", "UPPDRAG MISSLYCKAT");
        m.put("GAME_DEFUSED", "Bomb Desarmerad!");
        m.put("GAME_ABORTED", "Uppdrag Avbrutet");
        m.put("GAME_TIME", "Tiden Är Slut!");
        m.put("GAME_STRIKES", "För Många Fel!");
        m.put("LBL_TIME_REMAINING", "Återstående Tid: ");
        m.put("CREDITS_TITLE", "CREDITS");
        m.put("CREDITS_THANKS", "Tack för att du spelade!");
        m.put("BTN_BACK", "Tillbaka");
        m.put("MANUAL_TITLE", "BOMBDESARMERINGSMANUAL");
        m.put("MANUAL_INTRO", "INTRODUKTION");
        m.put("MANUAL_WARNING", "VARNING:");
        m.put("MANUAL_MODULES", "MODULER");
        m.put("MANUAL_MISSIONS", "UPPDRAG");
        m.put("MOD_WIRES", "Kablar");
        m.put("MOD_BUTTON", "Knappen");
        m.put("LEVEL_SELECT", "VÄLJ UPPDRAG");
        m.put("COLOR_RED", "Röd");
        m.put("COLOR_BLUE", "Blå");
        m.put("COLOR_GREEN", "Grön");
        m.put("COLOR_YELLOW", "Gul");
        m.put("COLOR_WHITE", "Vit");
    }

    private static void initIcelandic() {
        Map<String, String> m = dictionary.get(Language.ICELANDIC);
        m.put("TITLE_MAIN", "HALTU ÁFRAM AÐ TALA");
        m.put("TITLE_SUB", "og Enginn Springur");
        m.put("BTN_START", "HEFJA VERKEFNI");
        m.put("BTN_FREEPLAY", "FRJÁLS LEIKUR");
        m.put("BTN_MANUAL", "ÞJÁLFUNARHANDBÓK");
        m.put("BTN_CREDITS", "HÖFUNDAR");
        m.put("BTN_EXIT", "HÆTTA");
        m.put("BTN_RETURN", "AFTUR Í VALMYND");
        m.put("LBL_LANGUAGE", "TUNGUMÁL");
        m.put("GAME_ABORT", "HÆTTA VIÐ");
        m.put("GAME_SERIAL", "RAÐNR.");
        m.put("GAME_BATTERIES", "RAFHLÖÐUR");
        m.put("GAME_PARALLEL", "SAMHLIÐA TENGI");
        m.put("GAME_INDICATORS", "VÍSAR");
        m.put("GAME_YES", "JÁ");
        m.put("GAME_NO", "NEI");
        m.put("GAME_NONE", "EKKERT");
        m.put("GAME_WIN", "VERKEFNI LOKIÐ");
        m.put("GAME_LOSE", "VERKEFNI MISTÓKST");
        m.put("GAME_DEFUSED", "Sprengja Afvopnuð!");
        m.put("GAME_ABORTED", "Verkefni Hætt");
        m.put("GAME_TIME", "Tíminn Er Búinn!");
        m.put("GAME_STRIKES", "Of Margar Villur!");
        m.put("LBL_TIME_REMAINING", "Tími Eftir: ");
        m.put("CREDITS_TITLE", "HÖFUNDAR");
        m.put("CREDITS_THANKS", "Takk fyrir að spila!");
        m.put("BTN_BACK", "Til baka");
        m.put("MANUAL_TITLE", "HANDBÓK UM AFVOPNUN SPRENGJU");
        m.put("MANUAL_INTRO", "INNGANGUR");
        m.put("MANUAL_WARNING", "VIÐVÖRUN:");
        m.put("MANUAL_MODULES", "EININGAR");
        m.put("MANUAL_MISSIONS", "VERKEFNI");
        m.put("MOD_WIRES", "Vírar");
        m.put("MOD_BUTTON", "Hnappur");
        m.put("LEVEL_SELECT", "VELDU VERKEFNI");
        m.put("COLOR_RED", "Rauður");
        m.put("COLOR_BLUE", "Blár");
        m.put("COLOR_GREEN", "Grænn");
        m.put("COLOR_YELLOW", "Gulur");
        m.put("COLOR_WHITE", "Hvítur");
    }
}
