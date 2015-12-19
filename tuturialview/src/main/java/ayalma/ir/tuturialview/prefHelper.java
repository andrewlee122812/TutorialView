package ayalma.ir.tuturialview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by alimohammadi on 12/17/15.
 */
final class prefHelper {

    /**
     * for accessing tutorial show status
     *
     * @param context    for accessing application based preferences
     * @param tutorialId for accessing tutorial in preferences
     * @return return true if tutorial with this id is shown to user or false if tutorial don't exist
     */
    static boolean isShowed(Context context, String tutorialId) {

        return context.getSharedPreferences(TCons.PREFERENCES_ARG, context.MODE_PRIVATE).getBoolean(tutorialId, false);

    }

    /**
     * for setting tutorial show status
     *
     * @param context    for accessing application based preferences
     * @param tutorialId for accessing tutorial in preferences
     */
    static void setShowed(Context context, String tutorialId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(TCons.PREFERENCES_ARG, context.MODE_PRIVATE).edit().putBoolean(tutorialId, true);

        if (Build.VERSION.SDK_INT < 9)
            editor.commit();
        else editor.apply();
    }

    /**
     * for savaging theme in preferences.
     *
     * @param context for accessing application based preferences.
     * @param theme theme that will saved in preferences.
     */
    static void saveTheme(Context context , int theme)
    {

        SharedPreferences.Editor editor = context.getSharedPreferences(TCons.PREFERENCES_ARG, context.MODE_PRIVATE).edit().putInt(TCons.SELECTED_THEME, theme);

        if (Build.VERSION.SDK_INT < 9)
            editor.commit();
        else editor.apply();

    }

    /**
     * for getting theme
     *
     * @param context for accessing application based preferences.
     * @return saved theme if present or tutorial default theme
     */
    static int getTheme(Context context){

        return context.getSharedPreferences(TCons.PREFERENCES_ARG, context.MODE_PRIVATE).getInt(TCons.SELECTED_THEME, R.style.tutorialView_Theme_Light);
    }
}
