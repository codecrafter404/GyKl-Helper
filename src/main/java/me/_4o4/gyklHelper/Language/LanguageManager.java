package me._4o4.gyklHelper.Language;

import java.util.Arrays;
import java.util.List;

/**
 * A manager for the Language
 */
public class LanguageManager {
    private List<Language> languages = Arrays.asList(
            new English(),
            new German()
    );

    public Language getLang(String lang){
        Language result = null;
        for(Language language : languages){
            if(language.getName().equalsIgnoreCase(lang)){
                result = language;
            }
        }
        return result;
    }

    public List<Language> getAllLanguages() {
        return languages;
    }
}
