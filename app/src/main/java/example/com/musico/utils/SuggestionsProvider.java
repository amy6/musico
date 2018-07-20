package example.com.musico.utils;

import android.content.SearchRecentSuggestionsProvider;

public class SuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = "android.content.SearchRecentSuggestionsProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
