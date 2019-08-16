package com.allintask.lingdao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allintask.lingdao.R;

/**
 * @author jianping
 * @author TanJiaJun
 * @date 2016-10-12
 */
public class EditPasswordView extends LinearLayout {

    private View parent;
    private EditTextWithDelete edt_password;
    private ImageView psd_visible_btn;

    public EditPasswordView(Context context) {
        super(context);
    }

    public EditPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditPasswordView);
        boolean showLeftIcon = typedArray.getBoolean(R.styleable.EditPasswordView_epv_show_left_icon, true);
        int leftIconBackground = typedArray.getResourceId(R.styleable.EditPasswordView_epv_left_icon_background, R.mipmap.login_password_icon_nor);
        int hint = typedArray.getResourceId(R.styleable.EditPasswordView_epv_hint, R.string.please_input_password);
        typedArray.recycle();

        parent = LayoutInflater.from(context).inflate(R.layout.edit_password_view, null);
        edt_password = (EditTextWithDelete) parent.findViewById(R.id.edt_password);
        psd_visible_btn = (ImageView) parent.findViewById(R.id.psd_visible_btn);

        Drawable drawable = getResources().getDrawable(leftIconBackground);
        edt_password.setCompoundDrawablesWithIntrinsicBounds(showLeftIcon ? drawable : null, null, edt_password.getCompoundDrawables()[2], null);
        edt_password.setHint(getResources().getString(hint));
        edt_password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(parent, params);
        psd_visible_btn.setTag("invisible");
        psd_visible_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_password.requestFocus();
                if (psd_visible_btn.getTag().equals("visible")) {
                    psd_visible_btn.setTag("invisible");
                    psd_visible_btn.setImageResource(R.mipmap.psd_invisible);
                    edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    // 使光标始终在最后位置
                    Editable etable = edt_password.getText();
                    Selection.setSelection(etable, etable.length());
                } else {
                    edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    psd_visible_btn.setImageResource(R.mipmap.psd_visible);
                    psd_visible_btn.setTag("visible");
                    // 使光标始终在最后位置
                    Editable etable = edt_password.getText();
                    Selection.setSelection(etable, etable.length());
                }

            }
        });
    }

    public EditTextWithDelete getEditTextWithDel() {
        return edt_password;
    }

    public String getPassword() {
        String password = edt_password.getText().toString().trim();
        return password;
    }

}
