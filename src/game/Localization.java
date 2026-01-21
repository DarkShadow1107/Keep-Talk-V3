package game;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Localization {
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

    private static Language currentLanguage = Language.ENGLISH;
    private static ResourceBundle currentBundle;
    private static ResourceBundle englishBundle; // Fallback

    static {
        // Load English as fallback
        englishBundle = loadBundle(Language.ENGLISH);
        currentBundle = englishBundle;
    }

    private static ResourceBundle loadBundle(Language lang) {
        try {
            String resourcePath = "/resources/messages_" + lang.getCode() + ".properties";
            var stream = Localization.class.getResourceAsStream(resourcePath);
            if (stream != null) {
                InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                return new PropertyResourceBundle(reader);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load language file for " + lang.getDisplayName() + ": " + e.getMessage());
        }
        return null;
    }

    public static void setLanguage(Language lang) {
        currentLanguage = lang;
        ResourceBundle bundle = loadBundle(lang);
        if (bundle != null) {
            currentBundle = bundle;
        } else {
            // Fall back to English if language file not found
            currentBundle = englishBundle;
            System.out.println("Language file not found for " + lang.getDisplayName() + ", using English.");
        }
    }

    public static Language getLanguage() {
        return currentLanguage;
    }

    public static String get(String key) {
        // Try current language bundle first
        if (currentBundle != null) {
            try {
                String value = currentBundle.getString(key);
                if (value != null && !value.isEmpty()) {
                    return value;
                }
            } catch (Exception ignored) {}
        }
        
        // Fall back to English bundle
        if (englishBundle != null && currentBundle != englishBundle) {
            try {
                String value = englishBundle.getString(key);
                if (value != null && !value.isEmpty()) {
                    return value;
                }
            } catch (Exception ignored) {}
        }
        
        // If key not found anywhere, return the key itself
        return key;
    }
}
