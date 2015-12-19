package ayalma.ir.tuturialview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import carbon.widget.Button;
import carbon.widget.RelativeLayout;
import carbon.widget.TextView;

/**
 * view to show tutorial
 * Created by Ali Mohammadi on 12/14/15.
 *
 * @autor Ali Mohammadi
 */
final class TutorialView extends RelativeLayout implements ValueAnimator.AnimatorUpdateListener, View.OnClickListener {

    private static final String Tag = TutorialView.class.getSimpleName();

    private static final PorterDuffXfermode sXfermodeClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private static final PorterDuffXfermode sXfermodeNormal = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);

    protected static final int DEFAULT_BACKGROUND_COLOR = Color.BLUE;

    private TutorialListener tutorialListener;

    float radius = 0;
    private Tutorial mTutorial;
    ValueAnimator mShowAnimator;
    ValueAnimator mHideAnimator;
    ValueAnimator mSkipAnimator;
    private View mContentView;
    private View mContentContainer;

    private Button mDismissButton;
    private Button mSkipButton;
    private TextView mTitleTextView;
    private TextView mInfoTextView;

    private float marginTop = 0;
    private float marginBottom = 0;

    protected Paint mInnerCirclePaint;
    protected Paint paint;
    private boolean show = false;

    /**
     * this attr will use to find out type of tutorial e walk or reg
     */
    private int mMode;

    public TutorialView(Context context) {
        super(context);

    }

    public TutorialView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setColor(DEFAULT_BACKGROUND_COLOR);
        mInnerCirclePaint.setStyle(Paint.Style.STROKE);
        mInnerCirclePaint.setAlpha(TCons.INNER_CIRCLE_DEFAULT_ALPHA);
        mInnerCirclePaint.setStrokeWidth(TCons.DEFAULT_STROKE_WIDTH);
        mInnerCirclePaint.setColor(Color.parseColor("#66DE3226"));

        paint = new Paint();
        paint.setColor(Color.parseColor("#aaDE3226"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(false);
    }

    public TutorialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmMode(int mode) {
        this.mMode = mode;
    }

    /**
     * init radius and other stuff
     */
    private void init() {

        createContentView();

        // Getting the longest distance from the screen size so that will be the max of the animator max value.
        float rightWidth, leftWidth;
        float abowHeight, belowHeight;
        float maxWidth;
        float maxHeight;
        float maxRadius;

        rightWidth = getMeasuredWidth() - mTutorial.getCenterX();
        leftWidth = getMeasuredWidth() - rightWidth;

        belowHeight = getMeasuredHeight() - mTutorial.getCenterY();
        abowHeight = getMeasuredHeight() - belowHeight;

        maxWidth = (rightWidth > leftWidth) ? rightWidth : leftWidth;
        maxHeight = (abowHeight > belowHeight) ? abowHeight : belowHeight;

        maxRadius = (float) Math.sqrt(Math.pow(maxWidth, 2) + Math.pow(maxHeight, 2));


        mShowAnimator = ValueAnimator.ofFloat(0, maxRadius);
        mShowAnimator.setDuration(TCons.SHOW_DURATION);
        mShowAnimator.setStartDelay(TCons.SHOW_DELAY);
        mShowAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        mShowAnimator.addUpdateListener(this);
        mShowAnimator.addListener(animationListenerAdapter);

        mHideAnimator = ValueAnimator.ofFloat(maxRadius, 0);
        mHideAnimator.setDuration(TCons.HIDE_DURATION);
        mHideAnimator.setStartDelay(TCons.HIDE_DELAY);
        mHideAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mHideAnimator.addUpdateListener(this);
        mHideAnimator.addListener(animationListenerAdapter);

        mSkipAnimator = ValueAnimator.ofFloat(maxRadius, 0);
        mSkipAnimator.setDuration(TCons.HIDE_DURATION);
        mSkipAnimator.setStartDelay(TCons.HIDE_DELAY);
        mSkipAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        mSkipAnimator.addUpdateListener(this);
        mSkipAnimator.addListener(animationListenerAdapter);

    }

    /**
     * create content view by Tutorial and position of surrounded view
     */
    private void createContentView() {

        mContentView = inflate(getContext(), R.layout.tutorial_content_view, null);
        mContentView.setVisibility(INVISIBLE);

        mContentContainer = mContentView.findViewById(R.id.tutorial_view_contentContainer);

        mDismissButton = (Button) mContentView.findViewById(R.id.tutorial_view_dismissButton);
        mDismissButton.setOnClickListener(this);

        mSkipButton = (Button) mContentView.findViewById(R.id.tutorial_view_skipButton);
        mSkipButton.setOnClickListener(this);
        if (mMode == TCons.TUTORIAL_REG_MODE) {
            mSkipButton.setVisibility(GONE);
        }


        mTitleTextView = (TextView) mContentView.findViewById(R.id.tutorial_view_title);
        mTitleTextView.setText(mTutorial.getTitle());

        mInfoTextView = (TextView) mContentView.findViewById(R.id.tutorial_view_content);
        mInfoTextView.setText(mTutorial.getInfo());
        mInfoTextView.setMovementMethod(new ScrollingMovementMethod());

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // Adding the tutorial view.
        addView(mContentView, params);

        final float abowHeight = mTutorial.getY();
        final float belowHeight = getMeasuredHeight() - (mTutorial.getY() + mTutorial.getHeight());

        final float leftWidth = mTutorial.getX();
        final float rightWidth = getMeasuredWidth() - (mTutorial.getX() + mTutorial.getWidth());

        mSkipButton.post(new Runnable() {
            @Override
            public void run() {
                LayoutParams skipButtonParams = (LayoutParams) mSkipButton.getLayoutParams();
                skipButtonParams.addRule(ALIGN_PARENT_TOP);

                if (mSkipButton.getHeight() > abowHeight) //when we can't put button to screen bottom
                {
                    if (mSkipButton.getWidth() < leftWidth) {

                        skipButtonParams.setMargins(TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, (int) (rightWidth + (mTutorial.getRight() - getLeft()) + TCons.DEFAULT_MARGIN), TCons.DEFAULT_MARGIN);
                        skipButtonParams.addRule(ALIGN_PARENT_LEFT);

                        marginTop = Math.max(TCons.DEFAULT_MARGIN * 2 + mSkipButton.getHeight(), mTutorial.getBottom());

                    } else if (mSkipButton.getWidth() < rightWidth) {

                        skipButtonParams.setMargins((int) (mTutorial.getRight() + TCons.DEFAULT_MARGIN), TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN);
                        skipButtonParams.addRule(CENTER_VERTICAL);

                        marginTop = Math.max(TCons.DEFAULT_MARGIN * 2 + mSkipButton.getHeight(), mTutorial.getBottom());

                    } else {

                        skipButtonParams.setMargins(TCons.DEFAULT_MARGIN, (int) (mTutorial.getBottom() + TCons.DEFAULT_MARGIN), TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN);
                        marginTop = mTutorial.getBottom() + mSkipButton.getHeight() + TCons.DEFAULT_MARGIN * 2;

                    }

                    Log.d(Tag, "margin top :" + marginTop);

                } else {
                    skipButtonParams.setMargins(TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN);
                    marginTop = mSkipButton.getBottom();
                    Log.d(Tag, "margin top :" + marginTop);
                }

                mSkipButton.setLayoutParams(skipButtonParams);
                mSkipButton.requestLayout();
            }
        });

        mDismissButton.post(new Runnable() {
            @Override
            public void run() {
                LayoutParams mDismissButtonParams = (LayoutParams) mDismissButton.getLayoutParams();
                mDismissButtonParams.addRule(ALIGN_PARENT_BOTTOM);

                if (mDismissButton.getHeight() > belowHeight) //when we can't put button to screen bottom
                {
                    if (mDismissButton.getWidth() < leftWidth) {

                        mDismissButtonParams.setMargins(TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, (int) (rightWidth + (mTutorial.getRight() - getLeft()) + TCons.DEFAULT_MARGIN), TCons.DEFAULT_MARGIN);
                        mDismissButtonParams.addRule(ALIGN_PARENT_LEFT);

                        marginBottom = Math.max(TCons.DEFAULT_MARGIN * 2 + mDismissButton.getHeight(), mTutorial.getBottom() - mTutorial.getTop() + belowHeight);

                    } else if (mDismissButton.getWidth() < rightWidth) {

                        mDismissButtonParams.setMargins((int) (mTutorial.getRight() + TCons.DEFAULT_MARGIN), TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN);
                        mDismissButtonParams.addRule(CENTER_VERTICAL);

                        marginBottom = Math.max(TCons.DEFAULT_MARGIN * 2 + mDismissButton.getHeight(), mTutorial.getBottom() - mTutorial.getTop() + belowHeight);

                    } else {

                        mDismissButtonParams.setMargins(TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, (int) (mTutorial.getBottom() - mTutorial.getTop() + belowHeight));
                        marginBottom = mTutorial.getBottom() - mTutorial.getTop() + belowHeight + mSkipButton.getHeight() + TCons.DEFAULT_MARGIN * 2;

                    }

                    Log.d(Tag, "margin bottom :" + marginTop);

                } else {
                    mDismissButtonParams.setMargins(TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN, TCons.DEFAULT_MARGIN);
                    marginBottom = mDismissButton.getHeight() + 2 * TCons.DEFAULT_MARGIN;
                    Log.d(Tag, "margin bottom :" + marginBottom);
                }

                mDismissButton.setLayoutParams(mDismissButtonParams);
                mDismissButton.requestLayout();
            }
        });

        mContentContainer.post(new Runnable() {
            @Override
            public void run() {
                LayoutParams titleTextViewParams = (LayoutParams) mContentContainer.getLayoutParams();

                mContentContainer.setLayoutParams(calculateLayoutParam(titleTextViewParams));
                mContentContainer.requestLayout();

            }

            private ViewGroup.LayoutParams calculateLayoutParam(LayoutParams containerViewParams) {
                containerViewParams.setMargins(0, (int) marginTop, 0, (int) marginBottom);
                float rightArea = rightWidth * getHeight();
                float leftArea = leftWidth * getHeight();
                float abowArea = getWidth() * abowHeight;
                float belowArea = getWidth() * belowHeight;
                float maxArea = Math.max(Math.max(rightArea, leftArea), Math.max(abowArea, belowArea));
                Log.d(Tag, "rightArea: " + rightArea + "leftArea: " + leftArea + "abowArea: " + abowArea + "belowArea: " + belowArea);
                if (maxArea == rightArea) {
                    containerViewParams.setMargins((int) mTutorial.getRight(), (int) marginTop, 0, (int) marginBottom);
                } else if (maxArea == leftArea) {
                    containerViewParams.setMargins(0, (int) marginTop, (int) (rightWidth + mTutorial.getRight() - mTutorial.getLeft()), (int) marginBottom);
                } else if (maxArea == abowArea) {

                    containerViewParams.setMargins(0, (int) marginTop, 0, (int) Math.max((mTutorial.getBottom() - mTutorial.getTop() + belowHeight), marginBottom));
                } else {
                    containerViewParams.setMargins(0, (int) Math.max(mTutorial.getBottom(), marginTop), 0, (int) marginBottom);
                }

                return containerViewParams;
            }
        });

    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (mTutorial == null || !show)
            return;


        canvas.drawCircle(mTutorial.getCenterX(), mTutorial.getCenterY(), radius, paint);

        paint.setXfermode(sXfermodeClear);
        canvas.drawCircle(mTutorial.getCenterX(), mTutorial.getCenterY(), mTutorial.getRadius(), paint);

        mInnerCirclePaint.setAlpha(TCons.INNER_CIRCLE_DEFAULT_ALPHA);
        canvas.drawCircle(mTutorial.getCenterX(), mTutorial.getCenterY(), mTutorial.getRadius() - mInnerCirclePaint.getStrokeWidth() / 2, mInnerCirclePaint);

        mInnerCirclePaint.setAlpha(TCons.INNER_CIRCLE_DEFAULT_ALPHA / 2);
        canvas.drawCircle(mTutorial.getCenterX(), mTutorial.getCenterY(), mTutorial.getRadius() - mInnerCirclePaint.getStrokeWidth() - mInnerCirclePaint.getStrokeWidth() / 2, mInnerCirclePaint);
        paint.setXfermode(sXfermodeNormal);


    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation.equals(mShowAnimator))
            radius = (float) animation.getAnimatedValue();
        else if (animation.equals(mHideAnimator))
            radius = (float) animation.getAnimatedValue();
        else if (animation.equals(mSkipAnimator))
            radius = (float) animation.getAnimatedValue();
        invalidate();

    }

    public void setmTutorial(Tutorial mTutorial) {
        this.mTutorial = mTutorial;
        post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });

    }

    /**
     * start showing tutorial view
     */
    public void show() {
        post(new Runnable() {
            @Override
            public void run() {

                if (!(mShowAnimator.isStarted() || mShowAnimator.isRunning()))
                    mShowAnimator.start();

            }
        });

    }

    /**
     * start hiding of tutorial view
     */
    public void hide() {

        if (!(mHideAnimator.isStarted() || mHideAnimator.isRunning()))
            mHideAnimator.start();
    }

    /**
     * start skipping and hiding of tutorial view
     */
    public void skip() {

        if (!(mSkipAnimator.isStarted() || mSkipAnimator.isRunning()))
            mSkipAnimator.start();
    }

    public void setTutorialListener(TutorialListener tutorialListener) {
        this.tutorialListener = tutorialListener;
    }

    public TutorialListener getTutorialListener() {
        return tutorialListener;
    }


    /**
     * animation listener for finding out start and end of showing of tutorial
     */
    private AnimatorListenerAdapter animationListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            if (animation.equals(mShowAnimator)) {
                if (tutorialListener != null) {
                    //Todo : you crete method in TutorialListener for firing start event
                }
                show = true;
            } else if (animation.equals(mHideAnimator)) {
                mContentView.setVisibility(GONE);
                if (tutorialListener != null)
                    tutorialListener.onTutorialUnderstood();
            }
            else if (animation.equals(mSkipAnimator))
            {
                mContentView.setVisibility(GONE);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (animation.equals(mHideAnimator)) {
                if (tutorialListener != null)
                    tutorialListener.onTutorialHide();
                show = false;
            } else if (animation.equals(mShowAnimator)) {
                mContentView.setVisibility(VISIBLE);
            }
            else if (animation.equals(mSkipAnimator))
            {
                if (tutorialListener != null)
                    tutorialListener.onTutorialSkiped();
                show = false; // i add this line to OnDraw not draw text and other things anymory
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.equals(mDismissButton))
            hide();
        if (view.equals(mSkipButton)) {
            skip();
        }
    }
}
