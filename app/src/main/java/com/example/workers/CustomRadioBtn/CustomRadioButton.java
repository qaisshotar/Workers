package com.example.workers.CustomRadioBtn;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;


public abstract class CustomRadioButton extends RelativeLayout implements RadioCheckable {

    // Attribute Variables
    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;
    private OnLongPressListener onLongPressListener;
    private OnUnCheckListener onUnCheckListener;
    private OnCheckListener onCheckListener;
    private ArrayList<OnCheckedChangeListener> mOnCheckedChangeListeners = new ArrayList<>();
    private CustomRadioButton context = this;
    private boolean mChecked;

    //================================================================================
    // Constructors
    //================================================================================

    public CustomRadioButton(Context context) {
        super(context);
        setupView();
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
        setupView();
    }

    //================================================================================
    // Init & inflate methods
    //================================================================================

    protected abstract void parseAttributes(AttributeSet attrs);

    // Template method
    private void setupView() {
        inflateView();
        bindView();
        setCustomTouchListener();
    }

    protected abstract void inflateView();

    protected abstract void bindView();


    //================================================================================
    // Overriding default behavior
    //================================================================================

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        this.onLongPressListener = onLongPressListener;
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public void setOnUnCheckListener(OnUnCheckListener onUnCheckListener) {
        this.onUnCheckListener = onUnCheckListener;
    }

    protected void setCustomTouchListener() {
        super.setOnTouchListener(new TouchListener());
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }


    private void onTouchDown(MotionEvent motionEvent) {


        if (!isChecked()) {
            setChecked(true);
            if (onCheckListener != null)
                onCheckListener.OnCheck(this);
        } else if (onUnCheckListener != null)
            onUnCheckListener.OnUnCheck(this);
    }

    private void onTouchUp(MotionEvent motionEvent) {
        // Handle user defined click listeners
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
    }

    //================================================================================
    // Public methods
    //================================================================================


    public abstract void setCheckedState();

    public abstract void setNormalState();


    //================================================================================
    // Checkable implementation
    //================================================================================


    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (!mOnCheckedChangeListeners.isEmpty()) {
                for (int i = 0; i < mOnCheckedChangeListeners.size(); i++) {
                    mOnCheckedChangeListeners.get(i).onCheckedChanged(this, mChecked);
                }
            }
            if (mChecked) {
                setCheckedState();
            } else {
                setNormalState();
            }

        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.add(onCheckedChangeListener);

    }

    @Override
    public void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.remove(onCheckedChangeListener);
    }


    //================================================================================
    // Inner classes
    //================================================================================

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {

            if (onLongPressListener != null)
                onLongPressListener.OnLongPress(context);
        }
    };


    private final class TouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    handler.postDelayed(mLongPressed, ViewConfiguration.getLongPressTimeout());

                    break;
                case MotionEvent.ACTION_UP:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if(handler.hasCallbacks(mLongPressed))
                            onTouchDown(event);
                    }
                    handler.removeCallbacks(mLongPressed);

                    onTouchUp(event);
                    break;

            }
            if (mOnTouchListener != null) {
                mOnTouchListener.onTouch(v, event);
            }
            return true;
        }

    }
}