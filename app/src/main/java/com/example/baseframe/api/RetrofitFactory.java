package com.example.baseframe.api;


import android.content.Context;
import android.util.Log;

import com.blankj.ALog;
import com.example.baseframe.http.cookie.CookieJarImpl;
import com.example.baseframe.http.cookie.store.PersistentCookieStore;
import com.example.baseframe.rx.rxjava3adapter.RxJava3CallAdapterFactory;
import com.example.baseframe.utils.CommUtils;
import com.example.baseframe.utils.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit  生产类
 */
public class RetrofitFactory {
    public static final int TIMEOUT = 30;
    private static OkHttpClient okHttpClient;
    private static Context mContext = Utils.getContext();
    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient= new OkHttpClient.Builder()
                    .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
                    .addInterceptor(new HeaderInterceptor())//加header  he token   比如要加保密这些都可以从这里走
                    //请求日志拦截打印
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            if(CommUtils.isJson(message)){
                                ALog.json(message);
                            }else{
                                Log.w("RetrofitFactory",message);
                            }

                        }
                    }).setLevel(HttpLoggingInterceptor.Level.BODY))
                //    .addInterceptor(new CaptureInfoInterceptor())//com.github.DingProg.NetworkCaptureSelf:library:v1.0.1 抓包工具，可屏蔽
                    .build();

        }
        return okHttpClient;
    }
    /**
     * 初始化
     * @return
     */
    public static Retrofit getInstance() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                // 设置请求的域名
                .baseUrl(ConfigApi.BASE_URL)
                // 设置解析转换工厂
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        return mRetrofit;
    }



    /**
     * 获取微信小程序二维码
     * @return
     */
    public static Retrofit getWchatInstance() {
        return new Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com/wxa/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }






}
