package com.allintask.lingdao.ui.activity.main;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.MyServiceBean;
import com.allintask.lingdao.bean.service.PublishServiceBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.main.PublishServicePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.service.PublishServiceAddEducationalExperienceActivity;
import com.allintask.lingdao.ui.activity.service.PublishServiceAddHonorAndQualificationActivity;
import com.allintask.lingdao.ui.activity.service.PublishServiceAddWorkExperienceActivity;
import com.allintask.lingdao.ui.activity.service.UploadAlbumActivity;
import com.allintask.lingdao.ui.adapter.main.PublishServiceAdapter;
import com.allintask.lingdao.utils.MediaRecordPlayerUtils;
import com.allintask.lingdao.utils.MediaRecordUtils;
import com.allintask.lingdao.utils.SocketUploader;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.version.DownloadService;
import com.allintask.lingdao.view.main.IPublishServiceView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.allintask.lingdao.widget.RecordImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.FileUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/5.
 */

public class PublishServiceActivity extends BaseActivity<IPublishServiceView, PublishServicePresenter> implements IPublishServiceView {

    private static int VALID_CODE_TIME = 0; // 时间初始值

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.ll_header)
    LinearLayout headerLL;
    @BindView(R.id.tv_service_category)
    TextView serviceCategoryTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;
    @BindView(R.id.btn_publish_immediately)
    Button publishImmediatelyBtn;

    private int publishServiceType = CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD;
    private int mServiceId;
    private int categoryId = -1;

    private InputMethodManager inputMethodManager;

    private PublishServiceActivityReceiver publishServiceActivityReceiver;

    private int userId;
    private String serviceModeAndPriceUnitStr;
    private String mVoiceUrl;
    private String myMerit;
    private String serviceIntroduction;

    private List<GetIdToChineseListBean.GetIdToChineseBean> categoryList;
    private List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList;
    private List<GetIdToChineseListBean.GetIdToChineseBean> priceUnitList;

    private int servicePosition;
    private Set<Integer> serviceIsSelectedSet;
    private int serviceIndex;

    private AlertDialog recordDialog;
    private PublishServiceAdapter publishServiceAdapter;
    private List<PublishServiceBean> publishServiceList;

    private List<ServiceCategoryListBean> serviceCategoryList;

    private RecordImageView recordDialogVoiceView;
    private ImageView recordDialogLeftImg;
    private TextView recordDialogBottomText;
    private TextView vLittleTime;// 剩余时间
    private ProgressBar progressBarLoading;

    private String voiceFileName;
    private File voiceFile;
    private int recordFileTime;
    private long startTime; // 按下时间
    private long endTime; // 离开时间
    private boolean isTimeShort = false;// 时间短
    private boolean isShowLittleTime = false;// 剩余时间
    private boolean isOutTime = false;// 超时
    private int littleTime = 0;
    private int sentTimeMax = 60;// 最长时间
    private int lastStartTime = 50;// 倒数开始时间

    private float eventDownY;
    private float eventUpY;

    private boolean isUp = false; // 是否点击过

    private String uploadFlagId;
    private SocketUploader socketUploader;

    private boolean isExistEducationalExperience = false;
    private boolean isExistWorkExperience = false;
    private boolean isExistHonorAndQualification = false;

    private List<IsAllBean> addressList;

    private String voiceUrl;
    private String downloadVoiceDemoFilePath;
    private int voiceDemoDuration;
    private String downloadVoiceFilePath;
    private int voiceStatus;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_publish_service;
    }

    @Override
    protected PublishServicePresenter CreatePresenter() {
        return new PublishServicePresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            publishServiceType = intent.getIntExtra(CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE, CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD);
            mServiceId = intent.getIntExtra(CommonConstant.EXTRA_SERVICE_ID, -1);
            categoryId = intent.getIntExtra(CommonConstant.EXTRA_CATEGORY_ID, -1);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != inputMethodManager && inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }

                finish();
            }
        });

        toolbar.setTitle("");

        switch (publishServiceType) {
            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD:
                titleTv.setText(getString(R.string.publish_service));
                break;

            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE:
                titleTv.setText(getString(R.string.compile_service));
                break;
        }

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        registerPublishActivityReceiver();

        switch (publishServiceType) {
            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD:
                headerLL.setVisibility(View.GONE);
                break;

            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE:
                headerLL.setVisibility(View.VISIBLE);
                break;
        }

        recyclerView.setFocusableInTouchMode(false);

        publishServiceAdapter = new PublishServiceAdapter(getParentContext(), publishServiceType);
        recyclerView.setAdapter(publishServiceAdapter);

        if (publishServiceType == CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE) {
            publishServiceAdapter.setVoiceStatus(CommonConstant.VOICE_DOWNLOADING);
        }

        publishServiceAdapter.setOnTouchListener(new PublishServiceAdapter.OnTouchListener() {
            @Override
            public void onRecordImageViewTouch(View view, MotionEvent event) {
                if (MediaRecordUtils.getInstance().checkPermission(getParentContext())) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (null != recordDialog) {
                                setVoiceStop();
                                setEventActionDown(event);
                            }
                            break;

                        case MotionEvent.ACTION_MOVE:
                            if (null != recordDialog) {
                                setEventActionMove(event);
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            if (null != recordDialog) {
                                setEventActionUp(event);
                            }
                            break;

                        case MotionEvent.ACTION_CANCEL:
                            if (null != recordDialog) {
                                setEventActionCancel(event);
                            }
                            break;
                    }
                }
            }
        });

        publishServiceAdapter.setOnClickListener(new PublishServiceAdapter.OnClickListener() {
            @Override
            public void onCheckIsPublished(int position, Set<Integer> isSelectedSet, int i) {
                servicePosition = position;
                serviceIsSelectedSet = isSelectedSet;
                serviceIndex = i;

                if (null != serviceCategoryList && serviceCategoryList.size() > 0) {
                    ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(i);
                    categoryId = TypeUtils.getInteger(serviceCategoryListBean.code, 0);

                    if (categoryId != -1) {
                        mPresenter.fetchServiceIsPublishedRequest(categoryId);
                    }
                }
            }

            @Override
            public void onTagClickListener(int tempCategoryId) {
                categoryId = tempCategoryId;
                mPresenter.fetchServiceModeAndPriceModeRequest(categoryId);
            }

            @Override
            public void onDeleteVoice() {
                voiceFile = null;

                if (TextUtils.isEmpty(downloadVoiceFilePath)) {
                    voiceStatus = CommonConstant.VOICE_UNCHANGED;
                } else {
                    voiceStatus = CommonConstant.DELETE_OLD_VOICE;
                }
            }
        });

        recordDialog = getRecordDialog();
        initRecordListener();

        switch (publishServiceType) {
            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD:
                publishImmediatelyBtn.setText(getString(R.string.publish_immediately));
                break;

            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE:
                publishImmediatelyBtn.setText(getString(R.string.confirm_compile));
                break;
        }
    }

    private void initData() {
        userId = UserPreferences.getInstance().getUserId();
        publishServiceList = new ArrayList<>();

        switch (publishServiceType) {
            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD:
                mPresenter.fetchServiceCategoryListRequest();
                break;

            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE:
                mPresenter.getIdToChineseRequest();
                break;
        }

        mPresenter.fetchAddressListRequest();
        mPresenter.fetchVoiceDemoRequest();

        socketUploader = new SocketUploader(getParentContext());
        socketUploader.setOnUploadStatusListener(new SocketUploader.OnUploadStatusListener() {
            @Override
            public void onSuccess(String flagId) {
                uploadFlagId = flagId;
                mPresenter.checkUploadIsSuccessRequest(uploadFlagId);
            }

            @Override
            public void onFail() {
                dismissProgressDialog();
                showToast(getString(R.string.upload_fail));
            }
        });
    }

    private void initRecordListener() {
        // 录音功能的监听
        MediaRecordUtils.getInstance().setOnRecordProgressListener(new MediaRecordUtils.OnRecordProgressListener() {
            // 成功开始录音
            @Override
            public void onSuccessStartListener(int flag) {
                if (recordDialogVoiceView != null) {
                    progressBarLoading.setVisibility(View.GONE);
                    recordDialogVoiceView.setVisibility(View.VISIBLE);
                    recordDialogLeftImg.setVisibility(View.VISIBLE);
                    recordDialogBottomText.setBackgroundColor(0);
                    recordDialogBottomText.setText(getResources().getString(R.string.record_cancel_pull_tip));
                }
            }

            // 录音音量
            @Override
            public void onVoiceDegreeListener(int flag) {
                int level;
                float percent = (float) flag / (float) 100;
                if (percent > 1) {
                    level = 9;
                } else if (percent < 0.12) {
                    level = 1;
                } else {
                    level = (int) (percent * 9);
                }
                if (null != recordDialogVoiceView) {
                    recordDialogVoiceView.setLevel(level);
                }
            }

            // 停止录音
            @Override
            public void onStopRecordListener(int time) {
                recordFileTime = time;
                voiceFileName = MediaRecordUtils.getInstance().getFileName();

                String voiceFileAddress = AllintaskApplication.getInstance().getVoiceFilePath();
                String voiceFileString = voiceFileAddress + voiceFileName;

                voiceFile = new File(voiceFileString);

                if (time > 0) {
                    if (Math.abs(eventUpY - eventDownY) < 300 && !TextUtils.isEmpty(voiceFileName) && !isTimeShort) {
                        if (null != publishServiceAdapter) {
                            voiceStatus = CommonConstant.UPDATE_VOICE;

                            publishServiceAdapter.setVoiceDuration(time);
                            publishServiceAdapter.setDownloadVoiceFilePath(null);
                            publishServiceAdapter.setVoiceStatus(CommonConstant.VOICE_DOWNLOAD_SUCCESS);
                        }

                        recordDialog.dismiss();
                        setVoiceStop();
                    }

                    if (Math.abs(eventUpY - eventDownY) > 300) {
                        recordDialog.dismiss();

                        if (!TextUtils.isEmpty(voiceFileName)) {
                            deleteMessageVoice(voiceFileName);
                        }
                    }
                } else if (!isTimeShort) {
                    if (!recordDialog.isShowing()) {
                        recordDialog.show();
                    }

                    // 时间端，在范围内的才提示时间短
                    if (Math.abs(eventUpY - eventDownY) < 300) {
                        setRecordTimeShortTip();
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recordDialog.dismiss();
                        }
                    }, 600);
                }
            }

            // 开启录音失败
            @Override
            public void onFailedStartListener(int flag) {
                MediaRecordUtils.getInstance().stopRecord();
                recordDialog.dismiss();
                voiceFileName = null;
            }

            @Override
            public void onVoiceChangeTime(int time) {
                littleTime = time;

                // 进行最后10秒的倒数
                if (time > lastStartTime && isShowLittleTime) {
                    vLittleTime.setText((sentTimeMax - time) + "");
                    recordDialogVoiceView.setVisibility(View.GONE);
                    recordDialogLeftImg.setVisibility(View.GONE);
                    vLittleTime.setVisibility(View.VISIBLE);
                    recordDialogBottomText.setBackgroundColor(0);
                    recordDialogBottomText.setText(getResources().getString(R.string.record_cancel_little_time));
                }

                // 最多60秒进行发送
                if (time == sentTimeMax) {
                    isOutTime = true;
                    MediaRecordUtils.getInstance().stopRecord();
                    recordDialog.dismiss();
                }
            }
        });
    }

    /**
     * 取消语音的时候删除语音文件
     *
     * @param url
     */
    public void deleteMessageVoice(String url) {
        String fileUrl = AllintaskApplication.getInstance().getVoiceFilePath() + url;

        try {
            File file = new File(fileUrl);
            if (file.exists()) {
                file.delete();// 删除存在的语音文件
            }
        } catch (Exception e) {
            Log.i("TanJiaJun", "***delete file error");
        }
    }

    public AlertDialog getRecordDialog() {
        if (null == recordDialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentContext(), R.style.RecordDialogStyle);
            View view = LayoutInflater.from(getParentContext()).inflate(R.layout.dialog_record, null);
            recordDialogVoiceView = (RecordImageView) view.findViewById(R.id.record_dialog_view);
            recordDialogLeftImg = (ImageView) view.findViewById(R.id.record_dialog_left_img);
            recordDialogBottomText = (TextView) view.findViewById(R.id.record_dialog_bottom_text);
            vLittleTime = (TextView) view.findViewById(R.id.record_dialog_time);
            progressBarLoading = (ProgressBar) view.findViewById(R.id.animProgress_loading);
            LinearLayout body = (LinearLayout) view.findViewById(R.id.record_dialog_body);

            // 设置右侧的音量条的高度和图片的一致
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recordDialogVoiceView.getLayoutParams();
            layoutParams.width = getDialogLeftImgWidth() / 2;
            recordDialogVoiceView.setLayoutParams(layoutParams);

            body.getBackground().setAlpha(255 * 65 / 100);

            builder.setView(view);
            return builder.create();
        } else {
            return recordDialog;
        }
    }

    /**
     * 语音图片的宽度
     *
     * @return
     */
    public int getDialogLeftImgWidth() {
        int width = 0;

        if (null != recordDialogLeftImg) {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            recordDialogLeftImg.measure(w, h);
            width = recordDialogLeftImg.getMeasuredWidth();
        }
        return width;
    }

    /**
     * 点击其他按钮时停止语音
     */
    public void setVoiceStop() {
        MediaRecordPlayerUtils.getInstance().release();
    }

    /**
     * 按下的事件
     *
     * @param event
     */
    public void setEventActionDown(MotionEvent event) {
        vLittleTime.setVisibility(View.GONE);
        recordDialogVoiceView.setVisibility(View.GONE);
        recordDialogLeftImg.setVisibility(View.GONE);

        progressBarLoading.setVisibility(View.VISIBLE);
        recordDialogBottomText.setBackgroundColor(0);
        recordDialogBottomText.setText(getString(R.string.record_waiting));

        startTime = System.currentTimeMillis();
        isShowLittleTime = false;
        VALID_CODE_TIME = 0;
        isUp = false;

        //计时器
        TimeThread timeThread = new TimeThread(Handler);
        Handler.post(timeThread);
        eventDownY = event.getY();
    }

    /**
     * 移动的事件
     *
     * @param event
     */
    public void setEventActionMove(MotionEvent event) {
        if (Math.abs(event.getY() - eventDownY) > 300) {
            setRecordDialogType(true);
            isShowLittleTime = false;
        } else {
            setRecordDialogType(false);
            isShowLittleTime = true;
        }
    }

    /**
     * 设置手指上滑时弹窗的变化
     *
     * @param cancel
     */
    public void setRecordDialogType(boolean cancel) {
        if (cancel) {
            vLittleTime.setVisibility(View.GONE);
            recordDialogLeftImg.setVisibility(View.VISIBLE);
            recordDialogLeftImg.setImageResource(R.mipmap.icon_record_cancel);
            recordDialogVoiceView.setVisibility(View.GONE);
            recordDialogBottomText.setBackgroundColor(getResources().getColor(R.color.theme_orange));
            recordDialogBottomText.setText(getResources().getString(R.string.record_cancel_tip));
        } else {
            if (littleTime > lastStartTime) {
                recordDialogLeftImg.setVisibility(View.GONE);
                recordDialogVoiceView.setVisibility(View.GONE);
                recordDialogBottomText.setBackgroundColor(0);
                recordDialogBottomText.setText(getResources().getString(R.string.record_cancel_little_time));
            } else {
                recordDialogLeftImg.setVisibility(View.VISIBLE);
                recordDialogLeftImg.setImageResource(R.mipmap.icon_recording_voice);
                recordDialogVoiceView.setVisibility(View.VISIBLE);
                recordDialogBottomText.setBackgroundColor(0);
                recordDialogBottomText.setText(getResources().getString(R.string.record_cancel_pull_tip));
            }

        }
    }

    /**
     * 时间短的提示
     */
    public void setRecordTimeShortTip() {
        recordDialogLeftImg.setVisibility(View.VISIBLE);
        recordDialogVoiceView.setVisibility(View.VISIBLE);
        recordDialogLeftImg.setImageResource(R.mipmap.icon_recording_voice);
        recordDialogBottomText.setBackgroundColor(0);
        recordDialogBottomText.setText(getResources().getString(R.string.record_time_short_tip));
    }

    /**
     * 离开触摸的事件
     *
     * @param event
     */
    public void setEventActionUp(MotionEvent event) {
        eventUpY = event.getY();
        endTime = System.currentTimeMillis();
        if (VALID_CODE_TIME > 1) {
            // 防止不够1秒
            if ((endTime - startTime) > 600) {
                isTimeShort = false;
                MediaRecordUtils.getInstance().stopRecord();
            } else {
                // 时间短，在范围内的才提示时间短
                if (Math.abs(eventUpY - eventDownY) < 300) {
                    setRecordTimeShortTip();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isTimeShort = true;
                        if (!isOutTime) {
                            MediaRecordUtils.getInstance().stopRecord();
                            recordDialog.dismiss();
                        } else {
                            isOutTime = false;
                        }
                    }
                }, 600);
            }
        }
        // 防止连续点击的无用功
        else {
            isUp = true;
        }
    }

    /**
     * 去除权限弹框询问时的干扰
     *
     * @param event
     */
    public void setEventActionCancel(MotionEvent event) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaRecordUtils.getInstance().stopRecord();
                recordDialog.dismiss();
                voiceFileName = null;
            }
        }, 300);
    }

    /**
     * 防止点击马上离开的恶意触发
     */
    private Handler Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CommonConstant.MSG_UPDATE_VALID_CODE) {
                int currentTime = msg.arg1;
                if (currentTime > 1 && !isUp && currentTime < 3) {
                    //录音
                    MediaRecordUtils.getInstance().startRecord(1);
                    recordDialog.setCanceledOnTouchOutside(true);
                    recordDialog.show();

                    setVibrator();
                }
            }
        }
    };

    /**
     * 计时器
     */
    private class TimeThread implements Runnable {
        private Handler mHandler;

        public TimeThread(final Handler mHandler) {
            this.mHandler = mHandler;
        }

        @Override
        public void run() {
            if (VALID_CODE_TIME < 3) {
                VALID_CODE_TIME++;
                Message msg = new Message();
                msg.what = CommonConstant.MSG_UPDATE_VALID_CODE;
                msg.arg1 = VALID_CODE_TIME;
                mHandler.sendMessage(msg);
                mHandler.postDelayed(new TimeThread(mHandler), 200);
            }
        }
    }

    /**
     * 设置振动
     */
    private void setVibrator() {
        Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(100);
    }

    private void startDownloadVoiceDemoService(String downloadURL, String title, String desitinationPath) {
        if (null != publishServiceAdapter) {
            publishServiceAdapter.setVoiceStatus(CommonConstant.VOICE_DEMO_DOWNLOADING);
        }

        Intent updateIntent = new Intent(getParentContext(), DownloadService.class);
        updateIntent.setAction("com.allintask.lingdao.version.DownloadService");
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_TYPE, DownloadService.DOWNLOAD_VOICE_DEMO);
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_PATH, downloadURL);
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_TITLE, title);
        updateIntent.putExtra(DownloadService.APP_LOCAL_PATH, desitinationPath);
        startService(updateIntent);
    }

    private void startDownloadVoiceService(String downloadURL, String title, String desitinationPath) {
        if (null != publishServiceAdapter) {
            publishServiceAdapter.setVoiceStatus(CommonConstant.VOICE_DOWNLOADING);
        }

        Intent updateIntent = new Intent(getParentContext(), DownloadService.class);
        updateIntent.setAction("com.allintask.lingdao.version.DownloadService");
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_TYPE, DownloadService.DOWNLOAD_VOICE);
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_PATH, downloadURL);
        updateIntent.putExtra(DownloadService.APP_DOWNLOAD_TITLE, title);
        updateIntent.putExtra(DownloadService.APP_LOCAL_PATH, desitinationPath);
        startService(updateIntent);
    }

    private void registerPublishActivityReceiver() {
        publishServiceActivityReceiver = new PublishServiceActivityReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConstant.ACTION_DOWNLOAD_VOICE_DEMO_SUCCESS);
        intentFilter.addAction(CommonConstant.ACTION_DOWNLOAD_VOICE_SUCCESS);

        registerReceiver(publishServiceActivityReceiver, intentFilter);
    }

    class PublishServiceActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();

                if (null != action && null != publishServiceAdapter) {
                    if (action.equals(CommonConstant.ACTION_DOWNLOAD_VOICE_DEMO_SUCCESS)) {
                        publishServiceAdapter.setDownloadVoiceDemoFilePath(downloadVoiceDemoFilePath);
                        publishServiceAdapter.setVoiceDemoDuration(voiceDemoDuration);
                        publishServiceAdapter.setVoiceStatus(CommonConstant.VOICE_DEMO_DOWNLOAD_SUCCESS);
                    } else if (action.equals(CommonConstant.ACTION_DOWNLOAD_VOICE_SUCCESS)) {
                        publishServiceAdapter.setDownloadVoiceFilePath(downloadVoiceFilePath);
                        publishServiceAdapter.setVoiceDuration(recordFileTime);
                        publishServiceAdapter.setVoiceStatus(CommonConstant.VOICE_DOWNLOAD_SUCCESS);
                    }
                }
            }
        }
    }

    @OnClick({R.id.btn_publish_immediately})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_publish_immediately:
                if (null != inputMethodManager && inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }

                int categoryId = publishServiceAdapter.getCategoryId();
                String serviceCategoryString = publishServiceAdapter.getServiceCategoryListString();
                String serviceModeAndPriceModeString = publishServiceAdapter.getServiceModeAndPriceModeString();
                List<Integer> mustSelectedCategoryIdList = publishServiceAdapter.getMustSelectedCategoryIdList();
                boolean isNeedAddress = publishServiceAdapter.getIsNeedAddress();
                String provinceCode = publishServiceAdapter.getProvinceCode();
                String cityCode = publishServiceAdapter.getCityCode();
                String myMerit = publishServiceAdapter.getMyMerit();
                String serviceIntroduction = publishServiceAdapter.getServiceIntroduction();

                switch (publishServiceType) {
                    case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD:
                        if (categoryId != -1) {
                            if (null != mustSelectedCategoryIdList && mustSelectedCategoryIdList.size() > 0) {
                                if (!TextUtils.isEmpty(serviceCategoryString)) {
                                    boolean isServiceCategoryWhole = true;

                                    for (int i = 0; i < mustSelectedCategoryIdList.size(); i++) {
                                        int mustSelectedCategoryId = mustSelectedCategoryIdList.get(i);

                                        if (!serviceCategoryString.contains(String.valueOf(mustSelectedCategoryId))) {
                                            isServiceCategoryWhole = false;
                                        }
                                    }

                                    if (isServiceCategoryWhole) {
                                        if (!TextUtils.isEmpty(serviceModeAndPriceModeString)) {
                                            if (!serviceModeAndPriceModeString.equals("0")) {
                                                if (isNeedAddress) {
                                                    if (!TextUtils.isEmpty(provinceCode)) {
                                                        if (!TextUtils.isEmpty(myMerit)) {
                                                            if (!TextUtils.isEmpty(serviceIntroduction)) {
                                                                int myMeritNumberOfWords = myMerit.length();

                                                                if (myMeritNumberOfWords >= 20) {
                                                                    int serviceIntroductionNumberOfWords = serviceIntroduction.length();

                                                                    if (serviceIntroductionNumberOfWords >= 30) {
                                                                        if (null == voiceFile) {
                                                                            mPresenter.publishServiceRequest(serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, categoryId, -1, provinceCode, cityCode);
                                                                        } else {
                                                                            mPresenter.uploadVoiceRequest(voiceFile);
                                                                        }
                                                                    } else {
                                                                        showToast("服务介绍不够字数");
                                                                    }
                                                                } else {
                                                                    showToast("我的优势不够字数");
                                                                }
                                                            } else {
                                                                showToast("请填写服务介绍");
                                                            }
                                                        } else {
                                                            showToast("请填写我的优势");
                                                        }
                                                    } else {
                                                        showToast("请选择服务城市");
                                                    }
                                                } else {
                                                    if (!TextUtils.isEmpty(myMerit)) {
                                                        if (!TextUtils.isEmpty(serviceIntroduction)) {
                                                            int myMeritNumberOfWords = myMerit.length();

                                                            if (myMeritNumberOfWords >= 20) {
                                                                int serviceIntroductionNumberOfWords = serviceIntroduction.length();

                                                                if (serviceIntroductionNumberOfWords >= 30) {
                                                                    if (null == voiceFile) {
                                                                        mPresenter.publishServiceRequest(serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, categoryId, -1, provinceCode, cityCode);
                                                                    } else {
                                                                        mPresenter.uploadVoiceRequest(voiceFile);
                                                                    }
                                                                } else {
                                                                    showToast("服务介绍不够字数");
                                                                }
                                                            } else {
                                                                showToast("我的优势不够字数");
                                                            }
                                                        } else {
                                                            showToast("请填写服务介绍");
                                                        }
                                                    } else {
                                                        showToast("请填写我的优势");
                                                    }
                                                }
                                            } else {
                                                showToast("请填写正确的价格方式");
                                            }
                                        } else {
                                            showToast("请选择服务方式");
                                        }
                                    } else {
                                        showToast("还有必选项没选择");
                                    }
                                } else {
                                    showToast("请选择服务品类分类");
                                }
                            } else {
                                if (!TextUtils.isEmpty(serviceModeAndPriceModeString)) {
                                    if (!serviceModeAndPriceModeString.equals("0")) {
                                        if (isNeedAddress) {
                                            if (!TextUtils.isEmpty(provinceCode)) {
                                                if (!TextUtils.isEmpty(myMerit)) {
                                                    if (!TextUtils.isEmpty(serviceIntroduction)) {
                                                        int myMeritNumberOfWords = myMerit.length();

                                                        if (myMeritNumberOfWords >= 20) {
                                                            int serviceIntroductionNumberOfWords = serviceIntroduction.length();

                                                            if (serviceIntroductionNumberOfWords >= 30) {
                                                                if (null == voiceFile) {
                                                                    mPresenter.publishServiceRequest(serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, categoryId, -1, provinceCode, cityCode);
                                                                } else {
                                                                    mPresenter.uploadVoiceRequest(voiceFile);
                                                                }
                                                            } else {
                                                                showToast("服务介绍不够字数");
                                                            }
                                                        } else {
                                                            showToast("我的优势不够字数");
                                                        }
                                                    } else {
                                                        showToast("请填写服务介绍");
                                                    }
                                                } else {
                                                    showToast("请填写我的优势");
                                                }
                                            } else {
                                                showToast("请选择服务城市");
                                            }
                                        } else {
                                            if (!TextUtils.isEmpty(myMerit)) {
                                                if (!TextUtils.isEmpty(serviceIntroduction)) {
                                                    int myMeritNumberOfWords = myMerit.length();

                                                    if (myMeritNumberOfWords >= 20) {
                                                        int serviceIntroductionNumberOfWords = serviceIntroduction.length();

                                                        if (serviceIntroductionNumberOfWords >= 30) {
                                                            if (null == voiceFile) {
                                                                mPresenter.publishServiceRequest(serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, categoryId, -1, provinceCode, cityCode);
                                                            } else {
                                                                mPresenter.uploadVoiceRequest(voiceFile);
                                                            }
                                                        } else {
                                                            showToast("服务介绍不够字数");
                                                        }
                                                    } else {
                                                        showToast("我的优势不够字数");
                                                    }
                                                } else {
                                                    showToast("请填写服务介绍");
                                                }
                                            } else {
                                                showToast("请填写我的优势");
                                            }
                                        }
                                    } else {
                                        showToast("请填写正确的价格方式");
                                    }
                                } else {
                                    showToast("请选择服务方式");
                                }
                            }
                        } else {
                            showToast("请选择服务品类");
                        }
                        break;

                    case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE:
                        if (this.categoryId != -1) {
                            if (null != mustSelectedCategoryIdList && mustSelectedCategoryIdList.size() > 0) {
                                if (!TextUtils.isEmpty(serviceCategoryString)) {
                                    boolean isServiceCategoryWhole = true;

                                    for (int i = 0; i < mustSelectedCategoryIdList.size(); i++) {
                                        int mustSelectedCategoryId = mustSelectedCategoryIdList.get(i);

                                        if (!serviceCategoryString.contains(String.valueOf(mustSelectedCategoryId))) {
                                            isServiceCategoryWhole = false;
                                        }
                                    }

                                    if (isServiceCategoryWhole) {
                                        if (!TextUtils.isEmpty(serviceModeAndPriceModeString)) {
                                            if (!serviceModeAndPriceModeString.equals("0")) {
                                                if (isNeedAddress) {
                                                    if (!TextUtils.isEmpty(provinceCode)) {
                                                        if (!TextUtils.isEmpty(myMerit)) {
                                                            if (!TextUtils.isEmpty(serviceIntroduction)) {
                                                                int myMeritNumberOfWords = myMerit.length();

                                                                if (myMeritNumberOfWords >= 20) {
                                                                    int serviceIntroductionNumberOfWords = serviceIntroduction.length();

                                                                    if (serviceIntroductionNumberOfWords >= 30) {
                                                                        switch (voiceStatus) {
                                                                            case CommonConstant.VOICE_UNCHANGED:
                                                                                mPresenter.updateServiceRequest(mServiceId, serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, this.categoryId, -1, CommonConstant.VOICE_UNCHANGED, provinceCode, cityCode);
                                                                                break;

                                                                            case CommonConstant.UPDATE_VOICE:
                                                                                mPresenter.uploadVoiceRequest(voiceFile);
                                                                                break;

                                                                            case CommonConstant.DELETE_OLD_VOICE:
                                                                                mPresenter.updateServiceRequest(mServiceId, serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, this.categoryId, -1, CommonConstant.DELETE_OLD_VOICE, provinceCode, cityCode);
                                                                                break;
                                                                        }
                                                                    } else {
                                                                        showToast("服务介绍不够字数");
                                                                    }
                                                                } else {
                                                                    showToast("我的优势不够字数");
                                                                }
                                                            } else {
                                                                showToast("请填写服务介绍");
                                                            }
                                                        } else {
                                                            showToast("请填写我的优势");
                                                        }
                                                    } else {
                                                        showToast("请选择服务城市");
                                                    }
                                                } else {
                                                    if (!TextUtils.isEmpty(myMerit)) {
                                                        if (!TextUtils.isEmpty(serviceIntroduction)) {
                                                            int myMeritNumberOfWords = myMerit.length();

                                                            if (myMeritNumberOfWords >= 20) {
                                                                int serviceIntroductionNumberOfWords = serviceIntroduction.length();

                                                                if (serviceIntroductionNumberOfWords >= 30) {
                                                                    switch (voiceStatus) {
                                                                        case CommonConstant.VOICE_UNCHANGED:
                                                                            mPresenter.updateServiceRequest(mServiceId, serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, this.categoryId, -1, CommonConstant.VOICE_UNCHANGED, provinceCode, cityCode);
                                                                            break;

                                                                        case CommonConstant.UPDATE_VOICE:
                                                                            mPresenter.uploadVoiceRequest(voiceFile);
                                                                            break;

                                                                        case CommonConstant.DELETE_OLD_VOICE:
                                                                            mPresenter.updateServiceRequest(mServiceId, serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, this.categoryId, -1, CommonConstant.DELETE_OLD_VOICE, provinceCode, cityCode);
                                                                            break;
                                                                    }
                                                                } else {
                                                                    showToast("服务介绍不够字数");
                                                                }
                                                            } else {
                                                                showToast("我的优势不够字数");
                                                            }
                                                        } else {
                                                            showToast("请填写服务介绍");
                                                        }
                                                    } else {
                                                        showToast("请填写我的优势");
                                                    }
                                                }
                                            } else {
                                                showToast("请填写正确的价格方式");
                                            }
                                        } else {
                                            showToast("请选择服务方式");
                                        }
                                    } else {
                                        showToast("还有必选项没选择");
                                    }
                                } else {
                                    showToast("请选择服务品类分类");
                                }
                            } else {
                                if (!TextUtils.isEmpty(serviceModeAndPriceModeString)) {
                                    if (!serviceModeAndPriceModeString.equals("0")) {
                                        if (isNeedAddress) {
                                            if (!TextUtils.isEmpty(provinceCode)) {
                                                if (!TextUtils.isEmpty(myMerit)) {
                                                    if (!TextUtils.isEmpty(serviceIntroduction)) {
                                                        int myMeritNumberOfWords = myMerit.length();

                                                        if (myMeritNumberOfWords >= 20) {
                                                            int serviceIntroductionNumberOfWords = serviceIntroduction.length();

                                                            if (serviceIntroductionNumberOfWords >= 30) {
                                                                switch (voiceStatus) {
                                                                    case CommonConstant.VOICE_UNCHANGED:
                                                                        mPresenter.updateServiceRequest(mServiceId, serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, this.categoryId, -1, CommonConstant.VOICE_UNCHANGED, provinceCode, cityCode);
                                                                        break;

                                                                    case CommonConstant.UPDATE_VOICE:
                                                                        mPresenter.uploadVoiceRequest(voiceFile);
                                                                        break;

                                                                    case CommonConstant.DELETE_OLD_VOICE:
                                                                        mPresenter.updateServiceRequest(mServiceId, serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, this.categoryId, -1, CommonConstant.DELETE_OLD_VOICE, provinceCode, cityCode);
                                                                        break;
                                                                }
                                                            } else {
                                                                showToast("服务介绍不够字数");
                                                            }
                                                        } else {
                                                            showToast("我的优势不够字数");
                                                        }
                                                    } else {
                                                        showToast("请填写服务介绍");
                                                    }
                                                } else {
                                                    showToast("请填写我的优势");
                                                }
                                            } else {
                                                showToast("请选择服务城市");
                                            }
                                        } else {
                                            if (!TextUtils.isEmpty(myMerit)) {
                                                if (!TextUtils.isEmpty(serviceIntroduction)) {
                                                    int myMeritNumberOfWords = myMerit.length();

                                                    if (myMeritNumberOfWords >= 20) {
                                                        int serviceIntroductionNumberOfWords = serviceIntroduction.length();

                                                        if (serviceIntroductionNumberOfWords >= 30) {
                                                            switch (voiceStatus) {
                                                                case CommonConstant.VOICE_UNCHANGED:
                                                                    mPresenter.updateServiceRequest(mServiceId, serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, this.categoryId, -1, CommonConstant.VOICE_UNCHANGED, provinceCode, cityCode);
                                                                    break;

                                                                case CommonConstant.UPDATE_VOICE:
                                                                    mPresenter.uploadVoiceRequest(voiceFile);
                                                                    break;

                                                                case CommonConstant.DELETE_OLD_VOICE:
                                                                    mPresenter.updateServiceRequest(mServiceId, serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, null, null, this.categoryId, -1, CommonConstant.DELETE_OLD_VOICE, provinceCode, cityCode);
                                                                    break;
                                                            }
                                                        } else {
                                                            showToast("服务介绍不够字数");
                                                        }
                                                    } else {
                                                        showToast("我的优势不够字数");
                                                    }
                                                } else {
                                                    showToast("请填写服务介绍");
                                                }
                                            } else {
                                                showToast("请填写我的优势");
                                            }
                                        }
                                    } else {
                                        showToast("请填写正确的价格方式");
                                    }
                                } else {
                                    showToast("请选择服务方式");
                                }
                            }
                        } else {
                            showToast("请选择服务品类");
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onShowServiceCategoryList(List<ServiceCategoryListBean> serviceCategoryList) {
        this.serviceCategoryList = serviceCategoryList;
        publishServiceAdapter.setServiceCategoryList(this.serviceCategoryList);

        switch (publishServiceType) {
            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD:
                List<String> subclassNameList = new ArrayList<>();
                Set<Integer> isSelectedSet = new HashSet<>();
                List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                for (int i = 0; i < serviceCategoryList.size(); i++) {
                    ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(i);
                    String subclassName = TypeUtils.getString(serviceCategoryListBean.name, "");
                    subclassNameList.add(subclassName);
                }

                PublishServiceBean publishServiceBean = new PublishServiceBean();
                publishServiceBean.isShow = true;
                publishServiceBean.isRequired = true;
                publishServiceBean.name = "服务品类（单选）";
                publishServiceBean.maxSelectCount = 1;
                publishServiceBean.subclassNameList = subclassNameList;
                publishServiceBean.isSelectedSet = isSelectedSet;
                publishServiceBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                publishServiceList.add(publishServiceBean);
                publishServiceAdapter.setDateList(publishServiceList);
                break;

            case CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE:
                mPresenter.fetchMyServiceBeanRequest(mServiceId);
                break;
        }
    }

    @Override
    public void onShowServiceModeAndPriceModeList(int demandMaxBudget, int maxEmploymentTimes, List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList) {
        publishServiceAdapter.setServiceModeAndPriceModeList(serviceModeAndPriceModeList);

        if (publishServiceType == CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE && null != serviceModeAndPriceModeList && serviceModeAndPriceModeList.size() > 0) {
            List<String> subclassNameList = new ArrayList<>();
            Set<Integer> isSelectedSet = new HashSet<>();
            List<Integer> isSelectedCategoryIdList = new ArrayList<>();

            for (int l = 0; l < serviceModeAndPriceModeList.size(); l++) {
                ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(l);
                String serviceModeName = TypeUtils.getString(serviceModeAndPriceModeBean.serveWayName, "");
                subclassNameList.add(serviceModeName);
            }

            String[] serviceModeAndPriceUnitArray = serviceModeAndPriceUnitStr.split(";");

            for (int i = 0; i < serviceModeAndPriceUnitArray.length; i++) {
                String firstStr = serviceModeAndPriceUnitArray[i];

                if (!TextUtils.isEmpty(firstStr)) {
                    String[] firstArray = firstStr.split(":");
                    int position = -1;

                    for (int j = 0; j < firstArray.length; j++) {
                        String secondStr = firstArray[j];
                        int second = Integer.valueOf(secondStr);

                        if (j == 0) {
                            for (int k = 0; k < serviceModeList.size(); k++) {
                                GetIdToChineseListBean.GetIdToChineseBean serviceGetIdToChineseBean = serviceModeList.get(k);

                                if (null != serviceGetIdToChineseBean) {
                                    int code = TypeUtils.getInteger(serviceGetIdToChineseBean.code, -1);

                                    if (code == second) {
                                        position = k;
                                        isSelectedSet.add(k);
                                        isSelectedCategoryIdList.add(code);
                                    }
                                }
                            }
                        } else if (j == 2) {
                            ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(position);
                            serviceModeAndPriceModeBean.price = secondStr;
                            publishServiceAdapter.setAddServiceModeAndPriceModeBean(serviceModeAndPriceModeBean);
                        }
                    }
                }
            }

            PublishServiceBean tempPublishServiceBean = new PublishServiceBean();

            if (serviceModeAndPriceModeList.size() > 1) {
                tempPublishServiceBean.isShow = true;
            } else {
                tempPublishServiceBean.isShow = false;
            }

            tempPublishServiceBean.isRequired = true;
            tempPublishServiceBean.name = "服务方式";
            tempPublishServiceBean.maxSelectCount = 100;
            tempPublishServiceBean.subclassNameList = subclassNameList;
            tempPublishServiceBean.isSelectedSet = isSelectedSet;
            tempPublishServiceBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
            publishServiceList.add(tempPublishServiceBean);

            publishServiceAdapter.setMyMerit(myMerit);
            publishServiceAdapter.setServiceIntroduction(serviceIntroduction);
            publishServiceAdapter.setDateList(publishServiceList);
        }
    }

    @Override
    public void onShowAddressList(List<IsAllBean> addressList) {
        if (null != addressList && addressList.size() > 0) {
            this.addressList = addressList;

            if (null != publishServiceAdapter) {
                publishServiceAdapter.setAddressList(addressList);
            }
        }
    }

    @Override
    public void onShowIsPublished(boolean isPublished, final int serviceId) {
        if (isPublished) {
            if (null != publishServiceAdapter) {
                publishServiceAdapter.unexpandedService(servicePosition);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getParentContext()).setTitle("已发布过服务").setPositiveButton("修改服务", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    Intent intent = new Intent(getParentContext(), PublishServiceActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE, CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE);
                    intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, serviceId);
                    intent.putExtra(CommonConstant.EXTRA_CATEGORY_ID, categoryId);
                    startActivityForResult(intent, CommonConstant.REQUEST_CODE);

                    finish();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            if (null != publishServiceAdapter) {
                publishServiceAdapter.expandService(servicePosition, serviceIsSelectedSet, serviceIndex);
            }
        }

        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void uploadVoiceSuccess(String voiceUrl) {
        if (!TextUtils.isEmpty(voiceUrl)) {
            mVoiceUrl = voiceUrl;

            String serviceCategoryString = publishServiceAdapter.getServiceCategoryListString();
            String serviceModeAndPriceModeString = publishServiceAdapter.getServiceModeAndPriceModeString();
            String myMerit = publishServiceAdapter.getMyMerit();
            String serviceIntroduction = publishServiceAdapter.getServiceIntroduction();
            String provinceCode = publishServiceAdapter.getProvinceCode();
            String cityCode = publishServiceAdapter.getCityCode();

            String tempVoiceUrl = null;

            if (!TextUtils.isEmpty(voiceUrl)) {
                tempVoiceUrl = "https:" + mVoiceUrl;
            }

            if (publishServiceType == CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_ADD) {
                if (categoryId != -1 && !TextUtils.isEmpty(tempVoiceUrl)) {
                    mPresenter.publishServiceRequest(serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, mVoiceUrl, "amr", categoryId, recordFileTime, provinceCode, cityCode);
                }
            } else if (publishServiceType == CommonConstant.EXTRA_PUBLISH_SERVICE_TYPE_UPDATE) {
                if (categoryId != -1 && !TextUtils.isEmpty(tempVoiceUrl)) {
                    mPresenter.updateServiceRequest(mServiceId, serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, mVoiceUrl, "amr", categoryId, recordFileTime, CommonConstant.UPDATE_VOICE, provinceCode, cityCode);
                }
            }
        }
    }

    @Override
    public void uploadVoiceFail() {
        showToast("上传语音失败");
    }

//    @Override
//    public void uploadSuccess(int status, String codeId, String imageUrl) {
//        if (status == 0) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mPresenter.checkUploadIsSuccessRequest(uploadFlagId);
//                }
//            }, 1000);
//        } else if (status == 1) {
//            String serviceCategoryString = publishServiceAdapter.getServiceCategoryListString();
//            String serviceModeAndPriceModeString = publishServiceAdapter.getServiceModeAndPriceModeString();
//            String myMerit = publishServiceAdapter.getMyMerit();
//            String serviceIntroduction = publishServiceAdapter.getServiceIntroduction();
//
//            if (categoryId != -1) {
//                mPresenter.publishServiceRequest(serviceCategoryString, serviceModeAndPriceModeString, myMerit, serviceIntroduction, "voice", "amr", categoryId);
//            }
//        } else {
//            dismissProgressDialog();
//            showToast(getString(R.string.upload_fail));
//        }
//    }
//
//    @Override
//    public void uploadFail() {
//        dismissProgressDialog();
//        showToast(getString(R.string.upload_fail));
//    }

    @Override
    public void onPublishServiceSuccess(int mServiceId) {
        this.mServiceId = mServiceId;
        mPresenter.checkPersonalInformationWholeRequest(this.mServiceId);
    }

    @Override
    public void onShowGetIdToChineseListBean(GetIdToChineseListBean getIdToChineseListBean) {
        categoryList = getIdToChineseListBean.category;
        serviceModeList = getIdToChineseListBean.serveWay;
        priceUnitList = getIdToChineseListBean.priceUnit;

        mPresenter.fetchServiceCategoryListRequest();
    }

    @Override
    public void onShowMyServiceBean(MyServiceBean myServiceBean) {
        List<String> subclassNameList = new ArrayList<>();

        for (int i = 0; i < serviceCategoryList.size(); i++) {
            ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(i);
            String subclassName = TypeUtils.getString(serviceCategoryListBean.name, "");
            subclassNameList.add(subclassName);
        }

        if (null != myServiceBean) {
            int categoryId = TypeUtils.getInteger(myServiceBean.categoryId, -1);
            String categoryPropertyListStr = TypeUtils.getString(myServiceBean.categoryPropertyList, "");
            serviceModeAndPriceUnitStr = TypeUtils.getString(myServiceBean.serveWayPriceUnitList, "");
            String provinceCode = TypeUtils.getString(myServiceBean.provinceCode, "");
            String cityCode = TypeUtils.getString(myServiceBean.cityCode, "");
            voiceUrl = TypeUtils.getString(myServiceBean.voiceUrl, "");
            int voiceDuration = TypeUtils.getInteger(myServiceBean.voiceDuration, -1);
            String advantage = TypeUtils.getString(myServiceBean.advantage, "");
            String introduce = TypeUtils.getString(myServiceBean.introduce, "");

            if (null != publishServiceAdapter) {
                publishServiceAdapter.setProvinceCode(provinceCode);
                publishServiceAdapter.setCityCode(cityCode);
            }

            if (!TextUtils.isEmpty(provinceCode)) {
                if (null != addressList && addressList.size() > 0) {
                    for (int i = 0; i < addressList.size(); i++) {
                        IsAllBean isAllBean = addressList.get(i);

                        if (null != isAllBean) {
                            String temProvinceCode = TypeUtils.getString(isAllBean.code, "");
                            List<AddressSubBean> tempCityList = isAllBean.sub;

                            if (temProvinceCode.equals(provinceCode)) {
                                isAllBean.isSelected = true;
                            } else {
                                isAllBean.isSelected = false;
                            }

                            if (!TextUtils.isEmpty(cityCode)) {
                                if (null != tempCityList && tempCityList.size() > 0) {
                                    for (int j = 0; j < tempCityList.size(); j++) {
                                        AddressSubBean addressSubBean = tempCityList.get(j);

                                        if (null != addressSubBean) {
                                            String tempCityCode = TypeUtils.getString(addressSubBean.code, "");

                                            if (tempCityCode.equals(cityCode)) {
                                                addressSubBean.isSelected = true;
                                            } else {
                                                addressSubBean.isSelected = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (null != publishServiceAdapter) {
                    publishServiceAdapter.setAddressList(addressList);
                }
            }

            if (!TextUtils.isEmpty(voiceUrl)) {
                voiceUrl = "https:" + voiceUrl;
                String voiceFilename = voiceUrl.substring(voiceUrl.lastIndexOf("/") + 1);
                voiceFile = FileUtils.getCacheFile(getParentContext(), AllintaskApplication.getInstance().getVoiceFilePath(), voiceFilename);
                downloadVoiceFilePath = voiceFile.getPath();

                if (voiceFile.exists()) {
                    if (null != publishServiceAdapter) {
                        publishServiceAdapter.setDownloadVoiceFilePath(downloadVoiceFilePath);
                        publishServiceAdapter.setVoiceDuration(voiceDuration);
                        publishServiceAdapter.setVoiceStatus(CommonConstant.VOICE_DOWNLOAD_SUCCESS);
                    }
                } else {
                    startDownloadVoiceService(voiceUrl, "语音下载", downloadVoiceFilePath);
                }
            } else {
                if (null != publishServiceAdapter) {
                    publishServiceAdapter.setVoiceStatus(CommonConstant.NO_VOICE);
                }
            }

            recordFileTime = voiceDuration;
            myMerit = advantage;
            serviceIntroduction = introduce;

            PublishServiceBean firstUpdatePublishServiceBean = new PublishServiceBean();
            firstUpdatePublishServiceBean.isShow = true;
            firstUpdatePublishServiceBean.isRequired = true;
            firstUpdatePublishServiceBean.maxSelectCount = 1;

            int firstServiceCategoryIndex = -1;

            for (int i = 0; i < categoryList.size(); i++) {
                GetIdToChineseListBean.GetIdToChineseBean getIdToChineseBean = categoryList.get(i);

                if (null != getIdToChineseBean) {
                    int code = TypeUtils.getInteger(getIdToChineseBean.code, -1);

                    if (code == categoryId) {
                        for (int j = 0; j < serviceCategoryList.size(); j++) {
                            ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(j);

                            if (null != serviceCategoryListBean) {
                                int firstCode = TypeUtils.getInteger(serviceCategoryListBean.code, -1);

                                if (firstCode == categoryId) {
                                    String value = TypeUtils.getString(serviceCategoryListBean.name, "");

                                    publishServiceAdapter.setFirstServiceCategoryIndex(j);
                                    firstServiceCategoryIndex = j;

                                    serviceCategoryTv.setText(value);
                                }
                            }
                        }
                    }
                }
            }

            if (firstServiceCategoryIndex != -1) {
                ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(firstServiceCategoryIndex);
                List<ServiceCategoryListBean.ServiceCategoryFirstBean> serviceCategoryFirstList = serviceCategoryListBean.sub;

                if (null != serviceCategoryFirstList && serviceCategoryFirstList.size() > 0) {

                    for (int k = 0; k < serviceCategoryFirstList.size(); k++) {
                        ServiceCategoryListBean.ServiceCategoryFirstBean serviceCategoryFirstBean = serviceCategoryFirstList.get(k);
                        int subclassCategoryId = TypeUtils.getInteger(serviceCategoryFirstBean.code, -1);
                        int mustSelect = TypeUtils.getInteger(serviceCategoryFirstBean.mustSelect, 0);
                        String firstName = TypeUtils.getString(serviceCategoryFirstBean.name, "");
                        int maxSelectLen = TypeUtils.getInteger(serviceCategoryFirstBean.maxSelectLen, 0);
                        List<ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean> serviceCategorySecondList = serviceCategoryFirstList.get(k).sub;

                        if (null != serviceCategorySecondList && serviceCategorySecondList.size() > 0) {
                            List<String> subclassSubclassNameList = new ArrayList<>();
                            Set<Integer> subclassIsSelectedSet = new HashSet<>();
                            List<Integer> subclassIsSelectedCategoryIdList = new ArrayList<>();

                            for (int l = 0; l < serviceCategorySecondList.size(); l++) {
                                ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean serviceCategorySecondBean = serviceCategorySecondList.get(l);
                                String secondName = TypeUtils.getString(serviceCategorySecondBean.name, "");
                                subclassSubclassNameList.add(secondName);
                            }

                            String[] firstArray = categoryPropertyListStr.split(";");

                            for (String firstStr : firstArray) {
                                String[] secondArray = firstStr.split(":");

                                int j = 0;
                                int key = -1;

                                for (String secondStr : secondArray) {
                                    if (!TextUtils.isEmpty(secondStr)) {
                                        if (j % 2 == 0) {
                                            key = Integer.valueOf(secondStr);
                                        } else {
                                            if (key == subclassCategoryId) {
                                                String[] array = secondStr.split(",");

                                                for (String string : array) {
                                                    int thirdCategoryId = Integer.valueOf(string);

                                                    for (int l = 0; l < serviceCategorySecondList.size(); l++) {
                                                        ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean serviceCategorySecondBean = serviceCategorySecondList.get(l);

                                                        if (null != serviceCategorySecondBean) {
                                                            int code = TypeUtils.getInteger(serviceCategorySecondBean.code, -1);

                                                            if (code == thirdCategoryId) {
                                                                subclassIsSelectedSet.add(l);
                                                            }
                                                        }
                                                    }

                                                    subclassIsSelectedCategoryIdList.add(thirdCategoryId);
                                                }
                                            }
                                        }

                                        j++;
                                    }
                                }
                            }

                            PublishServiceBean tempPublishServiceBean = new PublishServiceBean();
                            tempPublishServiceBean.isShow = true;

                            if (mustSelect == 0) {
                                tempPublishServiceBean.isRequired = false;
                            } else {
                                tempPublishServiceBean.isRequired = true;

                                if (null != publishServiceAdapter) {
                                    publishServiceAdapter.addMustSelectedCategoryId(subclassCategoryId);
                                }
                            }

                            tempPublishServiceBean.categoryId = subclassCategoryId;

                            if (maxSelectLen == 1) {
                                tempPublishServiceBean.name = firstName + "（单选）";
                            } else if (maxSelectLen == 100) {
                                tempPublishServiceBean.name = firstName + "（多选）";
                            } else {
                                tempPublishServiceBean.name = firstName + "（" + maxSelectLen + "个）";
                            }

                            tempPublishServiceBean.maxSelectCount = maxSelectLen;
                            tempPublishServiceBean.subclassNameList = subclassSubclassNameList;
                            tempPublishServiceBean.isSelectedSet = subclassIsSelectedSet;
                            tempPublishServiceBean.isSelectedCategoryIdList = subclassIsSelectedCategoryIdList;
                            publishServiceList.add(tempPublishServiceBean);
                        }
                    }
                }
            }
        }

        mPresenter.fetchServiceModeAndPriceModeRequest(categoryId);
    }

    @Override
    public void onUpdateServiceSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onCheckPersonalInformationSuccess(boolean isExistEducationalExperience, boolean isExistWorkExperience, boolean isExistHonorAndQualification) {
        this.isExistEducationalExperience = isExistEducationalExperience;
        this.isExistWorkExperience = isExistWorkExperience;
        this.isExistHonorAndQualification = isExistHonorAndQualification;

        if (mServiceId != -1) {
            mPresenter.fetchServiceAlbumSurplusRequest(mServiceId);
        }
    }

    @Override
    public void onShowServicePhotoAlbumSurplusAmount(int servicePhotoAlbumSurplusAmount) {
        boolean isNeedServicePhotoAlbum;

        if (servicePhotoAlbumSurplusAmount == 0) {
            isNeedServicePhotoAlbum = false;
        } else {
            isNeedServicePhotoAlbum = true;
        }

        if (!isExistEducationalExperience) {
            Intent intent = new Intent(getParentContext(), PublishServiceAddEducationalExperienceActivity.class);
            intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, mServiceId);
            intent.putExtra(CommonConstant.EXTRA_IS_EXIST_WORK_EXPERIENCE, isExistWorkExperience);
            intent.putExtra(CommonConstant.EXTRA_IS_EXIST_HONOR_AND_QUALIFICATION, isExistHonorAndQualification);
            intent.putExtra(CommonConstant.EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM, isNeedServicePhotoAlbum);
            startActivity(intent);
        } else if (!isExistWorkExperience) {
            Intent intent = new Intent(getParentContext(), PublishServiceAddWorkExperienceActivity.class);
            intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, mServiceId);
            intent.putExtra(CommonConstant.EXTRA_IS_EXIST_HONOR_AND_QUALIFICATION, isExistHonorAndQualification);
            intent.putExtra(CommonConstant.EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM, isNeedServicePhotoAlbum);
            startActivity(intent);
        } else if (!isExistHonorAndQualification) {
            Intent intent = new Intent(getParentContext(), PublishServiceAddHonorAndQualificationActivity.class);
            intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, mServiceId);
            intent.putExtra(CommonConstant.EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM, isNeedServicePhotoAlbum);
            startActivity(intent);
        } else if (isNeedServicePhotoAlbum) {
            Intent intent = new Intent(getParentContext(), UploadAlbumActivity.class);
            intent.putExtra(CommonConstant.EXTRA_SERVICE_ID, mServiceId);
            intent.putExtra(CommonConstant.EXTRA_UPLOAD_ALBUM_TYPE, CommonConstant.UPLOAD_ALBUM_PUBLISH_SERVICE);
            startActivity(intent);
        }

        finish();
    }

    @Override
    public void onFetchServicePhotoAlbumFail() {
        finish();
    }

    @Override
    public void onFetchVoiceDemoSuccess(int voiceDemoDuration, String voiceDemoUrl) {
        this.voiceDemoDuration = voiceDemoDuration;

        if (!TextUtils.isEmpty(voiceDemoUrl)) {
            voiceDemoUrl = "https:" + voiceDemoUrl;
            String voiceDemoFilename = voiceDemoUrl.substring(voiceDemoUrl.lastIndexOf("/") + 1);
            File voiceDemoFile = FileUtils.getCacheFile(getParentContext(), AllintaskApplication.getInstance().getVoiceFilePath(), voiceDemoFilename);
            downloadVoiceDemoFilePath = voiceDemoFile.getPath();

            if (voiceDemoFile.exists()) {
                if (null != publishServiceAdapter) {
                    publishServiceAdapter.setDownloadVoiceDemoFilePath(downloadVoiceDemoFilePath);
                    publishServiceAdapter.setVoiceDemoDuration(this.voiceDemoDuration);
                    publishServiceAdapter.setVoiceStatus(CommonConstant.VOICE_DEMO_DOWNLOAD_SUCCESS);
                }
            } else {
                startDownloadVoiceDemoService(voiceDemoUrl, "语音下载", downloadVoiceDemoFilePath);
            }
        } else {
            if (null != publishServiceAdapter) {
                publishServiceAdapter.setVoiceStatus(CommonConstant.NO_VOICE_DEMO);
            }
        }
    }

    @Override
    public void onFetchVoiceDemoFail() {
        if (null != publishServiceAdapter) {
            publishServiceAdapter.setVoiceStatus(CommonConstant.NO_VOICE_DEMO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SocketUploader.UPLOAD_FILE_REQUEST_CODE) {
            dismissProgressDialog();

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showProgressDialog("正在提交");
                socketUploader.uploadVoiceFile(userId, voiceFile, voiceFileName, PublishServiceActivity.this);
            } else {
                showToast("拒绝权限导致上传失败");
            }
        } else if (requestCode == MediaRecordUtils.CODE_PERMISSION_FOR_CHATTING) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showToast("请允许录音权限");
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (null != publishServiceActivityReceiver) {
            unregisterReceiver(publishServiceActivityReceiver);
        }

        MediaRecordPlayerUtils.getInstance().reset();
        MediaRecordPlayerUtils.getInstance().release();

        super.onDestroy();
    }

}
