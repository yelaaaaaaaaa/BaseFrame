package com.example.baseframe;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.blankj.ALog;

import com.didichuxing.doraemonkit.DoraemonKit;
import com.example.baseframe.frame.ActivityManager;
import com.example.baseframe.ui.main.MainNewActivity;
import com.example.baseframe.utils.Utils;
import com.example.baseframe.utils.crash.CaocConfig;
import com.example.baseframe.weight.webview.WebViewActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;

public class App extends MultiDexApplication {

    public static App app;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initALog();
        setSmartRefreshLayout();
        initDoraemonKit();
        Utils.init(this);
        initCrash();
        registerActivityLifecycleCallbacks(this);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void initDoraemonKit() {
        DoraemonKit.install(this);
        DoraemonKit.setDebug(false);//DoraemonKit疯狂打印日志看了很烦

        // H5任意门功能需要，非必须（使用自己的H5容器打开这个链接）
        DoraemonKit.setWebDoorCallback((context, url) -> WebViewActivity.loadUrl(url, "DoraemonKit测试"));
    }

    public void initALog() {
        ALog.Config config = ALog.init(this)
                // 设置 log 总开关，包括输出到控制台和文件，默认开
                .setLogSwitch(BuildConfig.DEBUG)
                // 设置是否输出到控制台开关，默认开
                .setConsoleSwitch(BuildConfig.DEBUG)
                // 设置 log 全局标签，默认为空
                .setGlobalTag(null)
                // 当全局标签不为空时，我们输出的 log 全部为该 tag，
                // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
                // 设置 log 头信息开关，默认为开
                .setLogHeadSwitch(true)
                // 打印 log 时是否存到文件的开关，默认关
                .setLog2FileSwitch(false)
                // 当自定义路径为空时，写入应用的 /cache/log/ 目录中
                .setDir("")
                // 当文件前缀为空时，默认为 "alog"，即写入文件为 "alog-MM-dd.txt"
                .setFilePrefix("")
                // 输出日志是否带边框开关，默认开
                .setBorderSwitch(true)
                // 一条日志仅输出一条，默认开，为美化 AS 3.1 的 Logcat
                .setSingleTagSwitch(false)
                // log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
                .setConsoleFilter(ALog.V)
                // log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
                .setFileFilter(ALog.V)
                // log 栈深度，默认为 1
                .setStackDeep(1)
                // 设置栈偏移，比如二次封装的话就需要设置，默认为 0
                .setStackOffset(0)
                // 设置日志可保留天数，默认为 -1 表示无限时长
                .setSaveDays(3)
                // 新增 ArrayList 格式化器，默认已支持 Array, Throwable, Bundle, Intent 的格式化输出
                .addFormatter(new ALog.IFormatter<ArrayList>() {
                    @Override
                    public String format(ArrayList list) {
                        return "ALog Formatter ArrayList { " + list.toString() + " }";
                    }
                });
        ALog.d(config.toString());
    }


    /**
     * 设置SmartRefreshLayout默认的header Footer样式，需要在布局生成之前设置，建议代码放在 Application 中
     */
    private void setSmartRefreshLayout() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                ClassicsHeader header = new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Scale);
                header.setPrimaryColorId(R.color.ui_activity_bg);
                header.setAccentColorId(R.color.ui_gray);
                header.setTextSizeTime(13);
                header.setTextSizeTitle(15);
                return header;//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
//            @NonNull
//            @Override
//            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
//                layout.setEnableLoadMoreWhenContentNotFull(true);//内容不满一页时候启用加载更多
//                ClassicsFooter footer = new ClassicsFooter(context);
//                footer.setTextSizeTitle(15);
//                footer.setBackgroundResource(android.R.color.transparent);
//                footer.setSpinnerStyle(SpinnerStyle.Scale);//设置为拉伸模式
//                return footer;//指定为经典Footer，默认是 BallPulseFooter
//            }
//        });
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.drawable.customactivityoncrash_error_image) //错误图标
                .restartActivity(MainNewActivity.class) //重新启动后的activity
//                .errorActivity(MyDefaultErrorActivity.class) //崩溃后的错误activity(不设置使用默认)
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }


    //注册activity生命周期， activity生命周期监听可以做一些事情
    private void registerActivityLifecycleCallbacks(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                ALog.v(activity.getClass().getSimpleName() + "-onActivityCreated");
                ActivityManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                ALog.v(activity.getClass().getSimpleName() + "-onActivityResumed");
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                ALog.v(activity.getClass().getSimpleName() + "-onActivityDestroyed");
                ActivityManager.getInstance().removeActivity(activity);
            }
        });
    }

}