package com.ala.ala.services;


import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BadWordService {
    private List<String> badWords = Arrays.asList("bad", "words", "list","esprit");

    public boolean isWordForbidden(String text) {

        text = text.toLowerCase();
        for (String word: badWords) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }

}


     /*   private static final String[] BAD_WORDS = {"f***", "s***", "a******"};

        public String filter(String description) {
            String filteredContent = description;
            for (String badWord : BAD_WORDS) {
                filteredContent = filteredContent.replaceAll("(?i)" + badWord, "*");
            }
            return filteredContent;
        }
    }*/
/*@Service
public class BadWordService {
    private List<String> badWords = Arrays.asList("badword1", "badword2", "badword3");

    public String filterBadWords(String text) {
        for (String badWord : badWords) {
            text = text.replaceAll(badWord, "***");
        }
        return text;
    }
}*/

