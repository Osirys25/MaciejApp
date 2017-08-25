package org.maciejowka.maciejapp.events.poster;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import org.maciejowka.maciejapp.R;

/**
 * Created by maciej on 10.09.17.
 */
public class PosterHardcodedFactory {
    private static final String DINNER = "obiad";
    private static final String CLEANING = "sprzatanie";
    private static final String BOARD_GAMES = "planszowki";

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

    public static Drawable getBlank(Context context){
        return ContextCompat.getDrawable(context, R.drawable.blank);
    }

}
