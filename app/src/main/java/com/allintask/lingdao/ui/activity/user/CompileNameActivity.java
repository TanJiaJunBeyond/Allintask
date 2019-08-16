package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.CompileNamePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.EmojiUtils;
import com.allintask.lingdao.view.user.ICompileNameView;
import com.allintask.lingdao.widget.EditTextWithDelete;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public class CompileNameActivity extends BaseActivity<ICompileNameView, CompileNamePresenter> implements ICompileNameView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.iv_right_second)
    ImageView rightIv;
    @BindView(R.id.etwd_name)
    EditTextWithDelete nameETWD;

    private String name;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_compile_name;
    }

    @Override
    protected CompileNamePresenter CreatePresenter() {
        return new CompileNamePresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            name = intent.getStringExtra(CommonConstant.EXTRA_NAME);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.compile_name));
        rightIv.setImageResource(R.mipmap.ic_tick);
        rightIv.setVisibility(View.VISIBLE);
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameETWD.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    int nameLength = name.length();

                    if (nameLength >= 2) {
                        mPresenter.setNameRequest(name);
                    } else {
                        showToast("姓名长度必须是2~4位");
                    }
                } else {
                    showToast("请输入姓名");
                }
            }
        });

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        nameETWD.setText(name);
        nameETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = nameETWD.getText().toString().trim();
                int index = nameETWD.getSelectionStart() - 1;

                if (index >= 0) {
                    if (EmojiUtils.noEmoji(name)) {
                        Editable editable = nameETWD.getText();
                        editable.delete(index, index + 1);
                    }
                }
            }
        });
    }

    @Override
    public void onCompileNameSuccess() {
        if (!TextUtils.isEmpty(name)) {
            Intent intent = new Intent();
            intent.putExtra(CommonConstant.EXTRA_NAME, name);
            setResult(CommonConstant.RESULT_CODE, intent);
        }

        finish();
    }

}
