package com.allintask.lingdao.ui.activity.main;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.SystemBarTintManager;
import com.allintask.lingdao.utils.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by TanJiaJun on 2018/3/31.
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.bga_banner)
    BGABanner bgaBanner;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guide;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init() {
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_blue));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(GuideActivity.this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.color.select_skills_top_background_start_color);

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        MIUISetStatusBarLightMode(window, true);
        FlymeSetStatusBarLightMode(window, true);

        initData();
        initUI();
    }

    private void initData() {
        UserPreferences.getInstance().setIsFirst(false);
    }

    private void initUI() {
        View firstView = LayoutInflater.from(getParentContext()).inflate(R.layout.view_guide_first, null);
        View secondView = LayoutInflater.from(getParentContext()).inflate(R.layout.view_guide_second, null);
        View thirdView = LayoutInflater.from(getParentContext()).inflate(R.layout.view_guide_third, null);

        List<View> viewList = new ArrayList<>();
        viewList.add(firstView);
        viewList.add(secondView);
        viewList.add(thirdView);
        bgaBanner.setData(viewList);

        Button experienceBtn = (Button) thirdView.findViewById(R.id.btn_experience_at_once);

        experienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getParentContext(), MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

}
