package ayalma.ir.tuturialview;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity implements TutorialListener {

    private List<Tutorial> tutorials;
    private int tutorialMode;
    private int tutorialIndex;
    ayalma.ir.tuturialview.TutorialView tutorialView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_activity_test);

        setTheme(prefHelper.getTheme(this));

        tutorialView = (TutorialView) findViewById(R.id.tutorialView);

        tutorialMode = getIntent().getIntExtra(TCons.TUTORIAL_MODE_ARG, TCons.TUTORIAL_REG_MODE);

        initTutorialView();
    }

    /**
     * init tutorials and tutorial view by tutorial Mode
     */
    private void initTutorialView() {

        tutorials = new ArrayList<>();

        switch (tutorialMode) {
            case TCons.TUTORIAL_WALK_MODE:
                tutorials = getIntent().getParcelableArrayListExtra(TCons.TUTORIAL_ARG);
                break;
            case TCons.TUTORIAL_REG_MODE:
                Tutorial t = getIntent().getParcelableExtra(TCons.TUTORIAL_ARG);
                tutorials.add(t);
                break;
        }

        tutorialIndex = 0;
        tutorialView.setmMode(tutorialMode);

        tutorialView.setmTutorial(tutorials.get(tutorialIndex));
        tutorialView.setTutorialListener(this);
        tutorialView.show();

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onTutorialHide() {

        if (tutorialMode == TCons.TUTORIAL_REG_MODE) {
            onBackPressed();
        } else {
            if (++tutorialIndex < tutorials.size()) {

                tutorialView.setmTutorial(tutorials.get(tutorialIndex));
                tutorialView.show();

            } else {
                onBackPressed();
            }

        }

    }

    @Override
    public void onTutorialSkiped() {
        onBackPressed();
    }

    @Override
    public void onTutorialUnderstood() {
        prefHelper.setShowed(getBaseContext(), String.valueOf(tutorials.get(tutorialIndex).getTutorialId()));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        finish();
        super.onConfigurationChanged(newConfig);
    }
}
