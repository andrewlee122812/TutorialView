package ayalma.ir.tuturialview;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * class will be manage tutorial
 * Created by alimohammadi on 12/16/15.
 *
 * @author alimohammadi
 */
public class TutorialManager {

    private static TutorialManager sManager;
    private Context mContext;
    protected Intent mIntent;

    private HashMap<Integer, Tutorial> tutorialHashMap;

    /**
     * for getting instance of TutorialManager
     *
     * @return TutorialManager instance as singleton
     */
    public static TutorialManager getInstance(Context context) {
        if (sManager == null) {
            sManager = new TutorialManager(context);
        }
        return sManager;
    }

    /**
     * private constructor for creating Manager
     */
    private TutorialManager(Context context) {
        mContext = context;
        mIntent = new Intent(context, TutorialActivity.class);
        tutorialHashMap = new HashMap<>();
    }

    /**
     * this method will update or  add tutorial for view .
     *
     * @param surroundedView view that will surrounded with Tutorial Page
     * @param title          title of tutorial
     * @param description    description of tutorial
     */
    public void addTutorial(View surroundedView, String title, String description) {
        //// TODO: 12/18/15  create method for creatin of tutorial with action bar or not
        Tutorial tutorial = new Tutorial(surroundedView, title, description, true);
        tutorialHashMap.put(surroundedView.getId(), tutorial);
    }

    /**
     * @param theme theme with parent of tutorialView.Theme.Light will used in tutorial
     */
    public void setTheme(int theme) {

        prefHelper.saveTheme(mContext, theme);

    }

    public boolean showTutorial(View surruondedView) {

        if (tutorialHashMap != null && tutorialHashMap.containsKey(surruondedView.getId()) && !prefHelper.isShowed(mContext, String.valueOf(surruondedView.getId()))) {

            mIntent.putExtra(TCons.TUTORIAL_MODE_ARG, TCons.TUTORIAL_REG_MODE);
            mIntent.putExtra(TCons.TUTORIAL_ARG, tutorialHashMap.get(surruondedView.getId()));
            mContext.startActivity(mIntent);

            return true;
        }
        return false;
    }

    public boolean showAllTutorials() {

        HashMap<String, Tutorial> tempTutorials = new HashMap<>();
        Tutorial tempTutorial;
        for (Iterator<Tutorial> iterator = tutorialHashMap.values().iterator(); iterator.hasNext(); ) {
            tempTutorial = iterator.next();
            if (!prefHelper.isShowed(mContext, String.valueOf(tempTutorial.getTutorialId()))) {
                tempTutorials.put(String.valueOf(tempTutorial.getTutorialId()), tempTutorial);
            }
        }

        if (tempTutorials != null && tempTutorials.size() > 0)

        {

            mIntent.putExtra(TCons.TUTORIAL_MODE_ARG, TCons.TUTORIAL_WALK_MODE);
            mIntent.putParcelableArrayListExtra(TCons.TUTORIAL_ARG, new ArrayList<>(tempTutorials.values())); // convert hash MAp to array list cuz i use array list in tutorial activity
            mContext.startActivity(mIntent);

            return true;

        } else return false;
    }


    public void hasToolbar(boolean hasToolbar) {

    }
}
