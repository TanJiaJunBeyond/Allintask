package com.allintask.lingdao.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.allintask.lingdao.R;

/**
 * @author sunday
 */
public class EditTextWithDelete extends EditText {
    private Drawable imgAble;
    private Context mContext;
    private TextStateListener textStateListener;

    public EditTextWithDelete(Context context) {
        super(context, null);
    }

    public EditTextWithDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        imgAble = mContext.getResources().getDrawable(R.mipmap.delete_icon);

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
                if (textStateListener != null) {
                    textStateListener.checkText(s.toString());
                }
            }
        });
        setDrawable();
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    if (length() > 0) {
                        setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, imgAble, null);
                        isImgInable = true;
                    }
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, null, null);
                    isImgInable = false;
                }

            }
        });
    }

    public interface TextStateListener {
        void checkText(String s);
    }

    public void setTextStateListener(TextStateListener textStateListener) {
        this.textStateListener = textStateListener;
    }

    private void setDrawable() {
        if (length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, null, null);
            isImgInable = false;
        } else {
            setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], null, imgAble, null);
            isImgInable = true;
        }
    }

    private boolean isImgInable;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
            if (isImgInable && touchable)
                setText("");
        }
        return super.onTouchEvent(event);
    }

}
