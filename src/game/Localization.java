package game;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Gestioneaza suportul pentru mai multe limbi (Internationalizare).
 * Incarca textele dintr-un fisier de proprietati bazat pe limba selectata.
 */
public class Localization {
    /**
     * Enumeratie pentru limbile suportate de joc.
     */
    public enum Language {
        ENGLISH("English", "🇬🇧", "en"),
        FRENCH("Français", "🇫🇷", "fr"),
        SPANISH("Español", "🇪🇸", "es"),
        GERMAN("Deutsch", "🇩🇪", "de"),
        ROMANIAN("Română", "🇷🇴", "ro"),
        DUTCH("Nederlands", "🇳🇱", "nl"),
        POLISH("Polski", "🇵🇱", "pl"),
        DANISH("Dansk", "🇩🇰", "da"),
        RUSSIAN("Русский", "🇷🇺", "ru"),
        FINNISH("Suomi", "🇫🇮", "fi"),
        NORWEGIAN("Norsk", "🇳🇴", "no"),
        SWEDISH("Svenska", "🇸🇪", "sv"),
        ICELANDIC("Íslenska", "🇮🇸", "is");

        private final String displayName;
        private final String flag;
        private final String code;

        Language(String displayName, String flag, String code) {
            this.displayName = displayName;
            this.flag = flag;
            this.code = code;
        }

        public String getDisplayName() { return displayName; }
        public String getFlag() { return flag; }
        public String getCode() { return code; }
        @Override
        public String toString() { return displayName; }
    }

    private static Language currentLanguage = Language.ENGLISH; // Limba curent selectata
    private static ResourceBundle currentBundle; // Pachetul de resurse pentru limba curenta
    private static ResourceBundle englishBundle; // Pachetul de rezerva (fallback)

    static {
        // La incarcarea clasei, setam Engleza ca fiind limba implicita si de rezerva
        englishBundle = loadBundle(Language.ENGLISH);
        currentBundle = englishBundle;
    }

    /** Incarca fisierul .properties corespunzator limbii specificate. */
    private static ResourceBundle loadBundle(Language lang) {
        try {
            String resourcePath = "/resources/messages_" + lang.getCode() + ".properties";
            var stream = Localization.class.getResourceAsStream(resourcePath);
            if (stream != null) {
                // Incarcare in format UTF-8 pentru suportul caracterelor speciale (ex: diacritice)
                InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                return new PropertyResourceBundle(reader);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load language file for " + lang.getDisplayName() + ": " + e.getMessage());
        }
        return null;
    }

    /** Schimba limba interfetei jocului. */
    public static void setLanguage(Language lang) {
        currentLanguage = lang;
        ResourceBundle bundle = loadBundle(lang);
        if (bundle != null) {
            currentBundle = bundle;
        } else {
            // Daca nu gasim fisierul, ramanem pe Engleza
            currentBundle = englishBundle;
            System.out.println("Language file not found for " + lang.getDisplayName() + ", using English.");
        }
    }

    public static Language getLanguage() {
        return currentLanguage;
    }

    /**
     * Returneaza textul tradus pentru o anumita cheie.
     * @param key Cheia textului din fisierul .properties.
     * @return Textul tradus sau cheia insasi daca nu este gasita traducerarea.
     */
    public static String get(String key) {
        // Incercam in limba curenta
        if (currentBundle != null) {
            try {
                String value = currentBundle.getString(key);
                if (value != null && !value.isEmpty()) {
                    return value;
                }
            } catch (Exception ignored) {}
        }
        
        // Incercam in Engleza (fallback)
        if (englishBundle != null && currentBundle != englishBundle) {
            try {
                String value = englishBundle.getString(key);
                if (value != null && !value.isEmpty()) {
                    return value;
                }
            } catch (Exception ignored) {}
        }
        
        return key;
    }
}
