package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.HotSkillsBean;
import com.allintask.lingdao.presenter.user.SelectSkillsPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.demand.SelectServiceActivity;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.utils.SystemBarTintManager;
import com.allintask.lingdao.view.user.ISelectSkillsView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/1/2.
 */

public class SelectSkillsActivity extends BaseActivity<ISelectSkillsView, SelectSkillsPresenter> implements ISelectSkillsView {

    @BindView(R.id.tv_skip)
    TextView skipTv;
    @BindView(R.id.tag_flow_layout)
    TagFlowLayout tagFlowLayout;
    @BindView(R.id.btn_one_key_experience)
    Button oneKeyExperienceBtn;

    private int position = -1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_skills;
    }

    @Override
    protected SelectSkillsPresenter CreatePresenter() {
        return new SelectSkillsPresenter();
    }

    @Override
    protected void init() {
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.select_skills_top_background_start_color));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(SelectSkillsActivity.this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.color.select_skills_top_background_start_color);

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        MIUISetStatusBarLightMode(window, false);
        FlymeSetStatusBarLightMode(window, false);

        initData();
        initUI();
    }

    private void initData() {
        mPresenter.fetchHotSkillsListRequest();
    }

    private void initUI() {
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int i, FlowLayout flowLayout) {
                position = i;
                return false;
            }
        });
    }

    @OnClick({R.id.tv_skip, R.id.btn_one_key_experience})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
                Intent skipIntent = new Intent(getParentContext(), SelectServiceActivity.class);
                startActivity(skipIntent);

                finish();
                break;

            case R.id.btn_one_key_experience:
                if (position != -1) {
                    int skillId = mPresenter.getSkillsId(position);
                    mPresenter.selectSkillsRequest(skillId);
                } else {
                    showToast("请选择技能");
                }
                break;
        }
    }

    @Override
    public void onShowHotSkillsList(List<String> hotSkillsList) {
        tagFlowLayout.setAdapter(new TagAdapter(hotSkillsList) {
            @Override
            public View getView(FlowLayout flowLayout, int i, Object o) {
                TextView tagTv = (TextView) LayoutInflater.from(getParentContext()).inflate(R.layout.item_select_skills_tag_flow_layout, flowLayout, false);
                tagTv.setText(String.valueOf(o));
                return tagTv;
            }
        });
    }

    @Override
    public void onSelectSkillsSuccess() {
        Intent oneKeyExperienceIntent = new Intent(getParentContext(), SelectServiceActivity.class);
        startActivity(oneKeyExperienceIntent);

        finish();
    }

}
