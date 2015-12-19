package ayalma.ir.tuturialview;

/**
 * this class contain all constant of library
 * Created by alimohammadi on 12/16/15.
 *
 * @author alimohammadi
 */
final class TCons {

    static final int INNER_CIRCLE_DEFAULT_ALPHA = 140;
    static final int DEFAULT_STROKE_WIDTH = 13;
    static final int DEFAULT_MARGIN = 5;

    static final int SHOW_DURATION = 3000;
    static final int SHOW_DELAY = 0;
    static final int HIDE_DURATION = 1000;
    static final int HIDE_DELAY = 0;


    /**
     * constant for tutorial arg in intent.
     */
    static final String TUTORIAL_ARG = "tutorial_arg";

    /**
     * constant for tutorial mode arg.
     */
    static final String TUTORIAL_MODE_ARG = "tutorial_mode";

    /**
     * Tutorial walk mode : in this mode list of tutorial will be shown to user.
     */
    static final int TUTORIAL_WALK_MODE = 0;

    /**
     * tutorial regular mode : in this mode 1 tutorial will be shown and tutorial end.
     */
    static final int TUTORIAL_REG_MODE = 1;

    /**
     * tutorial preferences name.
     */
    static final String PREFERENCES_ARG = "tutorial_preferences";

    /**
     * tutorial selected theme arg name in preferences.
     */
    static final String SELECTED_THEME = "tutorial_selected_theme";
}
