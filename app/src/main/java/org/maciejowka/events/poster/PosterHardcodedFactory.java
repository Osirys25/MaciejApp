package org.maciejowka.events.poster;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import org.maciejowka.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by maciej on 10.09.17.
 */
public class PosterHardcodedFactory {
    //TODO zahardcodować wszystkie tagi
    private static final String DINNER = "obiad";
    private static final String CLEANING = "sprzatanie";
    private static final String BOARD_GAMES = "planszowki";
    public static final List<String> posterTags = Arrays.asList(DINNER, CLEANING, BOARD_GAMES);

    public static Drawable getPoster(String name, Context context) {
        switch (name) {
            case DINNER: {
                return ContextCompat.getDrawable(context, R.drawable.obiad);
            }
            case CLEANING: {
                return ContextCompat.getDrawable(context, R.drawable.obiad);
            }
            case BOARD_GAMES: {
                return ContextCompat.getDrawable(context, R.drawable.planszowki);
            }
        }
        return null;
    }

    public static Drawable getBlank(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.blank);
    }

}
