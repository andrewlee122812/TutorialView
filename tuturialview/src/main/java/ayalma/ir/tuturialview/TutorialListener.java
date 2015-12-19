package ayalma.ir.tuturialview;

/**
 * this is listener that contain method will be called whit tutorial actions
 * Created by alimohammadi on 12/14/15.
 * @author alimohammadi
 */
 interface TutorialListener {
    /**
     * this method will call when tutorial is hide
     */
    void onTutorialHide();
    /**
     * this method will call when tutorial skiped by user
     */
    void onTutorialSkiped();

    /**
     * this method will call when tutorial understood by user
     */
    void onTutorialUnderstood();
}
