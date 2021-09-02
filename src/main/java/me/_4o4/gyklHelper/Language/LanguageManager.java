package me._4o4.gyklHelper.Language;

import java.util.Arrays;
import java.util.List;

public class LanguageManager {
    private List<Language> languages = Arrays.asList(
            new English()
    );

    public Language getLang(String lang){
        Language result = null;
        for(Language language : languages){
            if(language.getName().toLowerCase().equals(lang.toLowerCase())){
                result = language;
            }
        }
        return result;
    }

    public List<Language> getAllLanguages() {
        return languages;
    }
}
