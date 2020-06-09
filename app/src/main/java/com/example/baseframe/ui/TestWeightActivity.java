package com.example.baseframe.ui;

import com.blankj.ALog;

import com.example.baseframe.R;
import com.example.baseframe.frame.base.BaseActivity;
import com.example.baseframe.frame.base.BaseViewModel;
import com.example.baseframe.frame.rx.bus.RxTimerUtil;
import com.example.baseframe.databinding.ActivityTestWeightBinding;
import com.example.baseframe.utils.animations.Other;
import com.example.baseframe.utils.animations.RxAnimation;
import com.example.baseframe.weight.IProgressBar;
import com.example.baseframe.weight.SuperTextView;


import java.util.concurrent.TimeUnit;

/**
 * @
 * @time 2019/11/30 15:07
 */
public class TestWeightActivity extends BaseActivity<ActivityTestWeightBinding, BaseViewModel> {

    int num=0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_weight;
    }

//    @Override
//    protected BaseMvvmViewModel initMVVMViewModel() {
//        return null;
//    }

    @Override
    public void initViewObservable() {

    }

    @Override
    protected void initView() {
        mBinding.mCircleProgressBar.setIProgressBarTextGenerator(new IProgressBar.IProgressBarTextGenerator() {
            @Override
            public String generateText(IProgressBar progressBar, int value, int maxValue) {
                if(value==maxValue){
                    mBinding.mCircleProgressBar.setProgress(0);
                    RxTimerUtil.cancel("TestWeightActivity");
                }
                return 100 * value / maxValue + "%";
            }
        });

        //倒计时工具类
        RxTimerUtil.interval(100, TimeUnit.MILLISECONDS,"TestWeightActivity",(number, timerName) -> {
            mBinding.mCircleProgressBar.setProgress(num++);

        });

        mBinding.mCountDownView.startCountDown();
        mBinding.mCountDownView.setAddCountDownListener(() -> mBinding.mCountDownView.startCountDown());
        mBinding.mLineWaveVoiceView.startRecord();

        //示例动画工具类使用
        RxAnimation.get().setAnimation(Other.pulseAnimator(mBinding.mCountDownView,2))
                .setDuration(1000)
                .start();


        String content="Android仿酷狗动感歌词（支持翻译和音译歌词）显示效果\nhttps://www.jianshu.com/p/9e7111db7b41";
        mBinding.mSuperTextView.setDynamicText(content);
        mBinding.mSuperTextView.setDynamicStyle(SuperTextView.DynamicStyle.CHANGE_COLOR);
        mBinding.mSuperTextView.setDurationByToalTime(8*1000);
        mBinding.mSuperTextView.start();
        mBinding.mSuperTextView.setOnDynamicListener(new SuperTextView.OnDynamicListener() {
            @Override
            public void onChange(int position, int total) {
                int lineCount = mBinding.mSuperTextView.getLineCount();
                int lineHeight = mBinding.mSuperTextView.getLineHeight();
                ALog.i(String.format("总行数：%s,行高度：%s", lineCount, lineHeight));
            }

            @Override
            public void onCompile() {
                mBinding.mSuperTextView.setDynamicStyle(SuperTextView.DynamicStyle.TYPEWRITING);
                mBinding.mSuperTextView.start();
            }
        });
    }
}
