package dev.max.timer.language;

public enum SupportedLanguages {

    ENGLISH("en"), GERMAN("de"), SPANISH("es"), FRENCH("fr");

    public String lang;

    SupportedLanguages(final String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public static SupportedLanguages getLanguageFromName(String name) {
        return SupportedLanguages.valueOf(name);
    }
}
