package com.example.baseframe.download;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by goldze
 * 文件下载管理，封装一行代码实现下载
 */

public class DownLoadManager {
    private static DownLoadManager instance;

    private static Retrofit retrofit;

    private DownLoadManager() {
        buildNetWork();
    }


    public static DownLoadManager getInstance() {
        if (instance == null) {
           synchronized (DownLoadManager.class){
               if(instance==null){
                   instance = new DownLoadManager();
               }
           }
        }
        return instance;
    }

    //下载
    public void load(String downUrl, final ProgressCallBack callBack) {
        retrofit.create(ApiService.class)
                .download(downUrl)
                .subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
                .observeOn(Schedulers.io()) //指定线程保存文件
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        callBack.saveFile(responseBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //在主线程中更新ui
                .subscribe(new DownLoadSubscriber<ResponseBody>(callBack));
    }

    private void buildNetWork() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://www.baidu.com")
                .build();
    }

    private interface ApiService {
        @Streaming
        @GET
        Observable<ResponseBody> download(@Url String url);
    }

    public class ProgressInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body()))
                    .build();
        }
    }

}
