package ayalma.ir.tuturialview;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Model that contain all information about one tutorial page
 * Created by alimohammadi on 12/14/15.
 *
 * @author alimohammadi
 */
final class Tutorial implements Parcelable {
    private float x;
    private float y;
    private float width;
    private float height;
    private float centerX;
    private float centerY;
    private float radius;
    private float top;
    private float bottom;
    private float left;
    private float right;
    private String title;
    private String info;
    private int tutorialId;
    private boolean hasActionBar;

    /**
     * empty cunstructor
     */
    public Tutorial() {

    }

    /**
     * for passing Tutorial as extra to activity
     *
     * @param in parcel that contain Tutorial information
     */
    Tutorial(Parcel in) {

        this.x = in.readFloat();
        this.y = in.readFloat();
        this.width = in.readFloat();
        this.height = in.readFloat();
        this.centerX = in.readFloat();
        this.centerY = in.readFloat();
        this.radius = in.readFloat();

        this.top = in.readFloat();
        this.bottom = in.readFloat();
        this.left = in.readFloat();
        this.right = in.readFloat();

        this.title = in.readString();
        this.info = in.readString();
        this.tutorialId = in.readInt();
        this.hasActionBar = in.readByte() != 0; //if read byte == 0 hasActionbar = false

    }

    /**
     * perfect constructor for creating tutorial.
     *
     * @param surroundedView view that will surrounded by tutorial.
     * @param title          title of tutorial.
     * @param description    of tutorial .
     */
    public Tutorial(final View surroundedView, String title, String description, boolean hasActionBar) {
        this.title = title;
        this.info = description;
        this.tutorialId = surroundedView.getId();
        this.hasActionBar = hasActionBar;
        init(surroundedView);
    }

    /**
     * set view that will be surrounded by tutorial
     *
     * @param mTutorialView view that tutorial showing for him;
     */
    public void setView(final View mTutorialView) {
        init(mTutorialView);

    }

    /**
     * init all position variable
     *
     * @param surroundedView init positioning value with it.
     */
    private void init(final View surroundedView) {
        surroundedView.post(new Runnable() {
            @Override
            public void run() {

                int[] tempLoc = new int[2];
                int[] temoLocOn = new int[2];
                surroundedView.getLocationOnScreen(tempLoc);
                surroundedView.getLocationInWindow(temoLocOn);

                x = tempLoc[0];
                y = tempLoc[1];

                if (Build.VERSION.SDK_INT < 19)// checking for sdk version if sdk version is lower than 19 reduce statusbar
                {
                    y -= TUtils.getStatusBarHeight(surroundedView.getContext());
                }

                width = surroundedView.getWidth();
                height = surroundedView.getHeight();

                radius = (float) (Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) / 2);
                centerX = (int) (x + width / 2);
                //// TODO: 12/18/15  get action bar method or get action barsize form client
                centerY = (hasActionBar) ? (int) (y - 50 + height / 2) : (int) (y + height / 2); // if action bar is present center y calculate in different way

                bottom = y + height + (radius - height / 2);
                top = y - (radius - height / 2);
                left = x - (radius - width / 2);
                right = x + width + (radius - width / 2);
            }
        });
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRadius() {
        return radius;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getLeft() {
        return left;
    }

    public float getBottom() {
        return bottom;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(int tutorialId) {
        this.tutorialId = tutorialId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeFloat(getX());
        parcel.writeFloat(getY());

        parcel.writeFloat(getWidth());
        parcel.writeFloat(getHeight());

        parcel.writeFloat(getCenterX());

        parcel.writeFloat(getCenterY());
        parcel.writeFloat(getRadius());

        parcel.writeFloat(getTop());
        parcel.writeFloat(getBottom());
        parcel.writeFloat(getLeft());
        parcel.writeFloat(getRight());

        parcel.writeString(title);
        parcel.writeString(info);
        parcel.writeInt(tutorialId);
        parcel.writeByte((byte) ((hasActionBar) ? 1 : 0));//if has action bar is true write 1 other 0
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     * <p/>
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     * <p/>
     * I just find it easier to use the constructor.
     * It makes sense for the way my brain thinks ;-)
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Tutorial createFromParcel(Parcel in) {
                    return new Tutorial(in);
                }

                public Tutorial[] newArray(int size) {
                    return new Tutorial[size];
                }
            };
}
