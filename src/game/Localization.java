package game;

import java.util.HashMap;
import java.util.Map;

public class Localization {
    public enum Language {
        ENGLISH, FRENCH, SPANISH, GERMAN, ROMANIAN
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
    }

    public static void setLanguage(Language lang) {
        currentLanguage = lang;
    }

    public static Language getLanguage() {
        return currentLanguage;
    }

    public static String get(String key) {
        String val = dictionary.get(currentLanguage).get(key);
        if (val == null) return dictionary.get(Language.ENGLISH).get(key);
        if (val == null) return key;
        return val;
    }

    private static void initEnglish() {
        Map<String, String> m = dictionary.get(Language.ENGLISH);
        m.put("TITLE_MAIN", "KEEP TALKING");
        m.put("TITLE_SUB", "and Nobody Explodes");
        m.put("BTN_START", "START MISSION");
        m.put("BTN_FREEPLAY", "FREE PLAY");
        m.put("BTN_MANUAL", "TRAINING MANUAL");
        m.put("BTN_CREDITS", "CREDITS");
        m.put("BTN_EXIT", "EXIT GAME");
        m.put("GAME_ABORT", "ABORT");
        m.put("GAME_SERIAL", "SERIAL #");
        m.put("GAME_BATTERIES", "BATTERIES");
        m.put("GAME_PARALLEL", "PARALLEL");
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
        m.put("BTN_RETURN", "RETURN TO MENU");
        m.put("LBL_TIME_REMAINING", "Time Remaining: ");
    }

    private static void initFrench() {
        Map<String, String> m = dictionary.get(Language.FRENCH);
        m.put("TITLE_MAIN", "CONTINUEZ À PARLER");
        m.put("TITLE_SUB", "et Personne N'Explose");
        m.put("BTN_START", "COMMENCER MISSION");
        m.put("BTN_FREEPLAY", "JEU LIBRE");
        m.put("BTN_MANUAL", "MANUEL");
        m.put("BTN_CREDITS", "CRÉDITS");
        m.put("BTN_EXIT", "QUITTER");
        m.put("GAME_ABORT", "ABANDONNER");
        m.put("GAME_SERIAL", "N° SÉRIE");
        m.put("GAME_BATTERIES", "PILES");
        m.put("GAME_PARALLEL", "PARALLÈLE");
        m.put("GAME_INDICATORS", "INDICATEURS");
        m.put("GAME_YES", "OUI");
        m.put("GAME_NO", "NON");
        m.put("GAME_NONE", "AUCUN");
        m.put("GAME_WIN", "MISSION ACCOMPLIE");
        m.put("GAME_LOSE", "ÉCHEC DE LA MISSION");
        m.put("GAME_DEFUSED", "Bombe Désamorcée!");
        m.put("GAME_ABORTED", "Mission Abandonnée");
        m.put("GAME_TIME", "Temps Écoulé!");
        m.put("GAME_STRIKES", "Trop d'Erreurs!");
        m.put("BTN_RETURN", "RETOUR MENU");
        m.put("LBL_TIME_REMAINING", "Temps Restant: ");
    }

    private static void initSpanish() {
        Map<String, String> m = dictionary.get(Language.SPANISH);
        m.put("TITLE_MAIN", "SIGUE HABLANDO");
        m.put("TITLE_SUB", "y Nadie Explota");
        m.put("BTN_START", "INICIAR MISIÓN");
        m.put("BTN_FREEPLAY", "JUEGO LIBRE");
        m.put("BTN_MANUAL", "MANUAL");
        m.put("BTN_CREDITS", "CRÉDITOS");
        m.put("BTN_EXIT", "SALIR");
        m.put("GAME_ABORT", "ABORTAR");
        m.put("GAME_SERIAL", "N° SERIE");
        m.put("GAME_BATTERIES", "BATERÍAS");
        m.put("GAME_PARALLEL", "PARALELO");
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
        m.put("BTN_RETURN", "VOLVER AL MENÚ");
        m.put("LBL_TIME_REMAINING", "Tiempo Restante: ");
    }

    private static void initGerman() {
        Map<String, String> m = dictionary.get(Language.GERMAN);
        m.put("TITLE_MAIN", "WEITERREDEN");
        m.put("TITLE_SUB", "und Niemand Explodiert");
        m.put("BTN_START", "MISSION STARTEN");
        m.put("BTN_FREEPLAY", "FREIES SPIEL");
        m.put("BTN_MANUAL", "HANDBUCH");
        m.put("BTN_CREDITS", "CREDITS");
        m.put("BTN_EXIT", "BEENDEN");
        m.put("GAME_ABORT", "ABBRUCH");
        m.put("GAME_SERIAL", "SERIENNR.");
        m.put("GAME_BATTERIES", "BATTERIEN");
        m.put("GAME_PARALLEL", "PARALLEL");
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
        m.put("BTN_RETURN", "ZURÜCK ZUM MENÜ");
        m.put("LBL_TIME_REMAINING", "Verbleibende Zeit: ");
    }

    private static void initRomanian() {
        Map<String, String> m = dictionary.get(Language.ROMANIAN);
        m.put("TITLE_MAIN", "CONTINUĂ SĂ VORBEȘTI");
        m.put("TITLE_SUB", "și Nimeni Nu Explodează");
        m.put("BTN_START", "ÎNCEPE MISIUNEA");
        m.put("BTN_FREEPLAY", "JOC LIBER");
        m.put("BTN_MANUAL", "MANUAL");
        m.put("BTN_CREDITS", "CREDITE");
        m.put("BTN_EXIT", "IEȘIRE");
        m.put("GAME_ABORT", "ABANDONEAZĂ");
        m.put("GAME_SERIAL", "NR. SERIE");
        m.put("GAME_BATTERIES", "BATERII");
        m.put("GAME_PARALLEL", "PARALEL");
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
        m.put("BTN_RETURN", "ÎNAPOI LA MENIU");
        m.put("LBL_TIME_REMAINING", "Timp Rămas: ");
    }
}
