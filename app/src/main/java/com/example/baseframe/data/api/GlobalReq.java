package com.example.baseframe.data.api;


import com.example.baseframe.data.RetrofitFactory;
import com.example.baseframe.data.bean.HomeListBean;
import com.example.baseframe.data.bean.ResultListBean;
import com.example.baseframe.data.bean.ResultBean;
import com.example.baseframe.data.bean.WanAndroidBannerBean;


import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


import static com.example.baseframe.global.SystemConst.ERROR_CODE;


/**
 * @Description: Http请求类
 * 基于原有的 RetrofitFactory.getInstance()请求类 在封装的一层。
 * @Author: yzh
 * @CreateDate: 2019/10/28 9:57
 */
public class GlobalReq {

    private static volatile GlobalReq mInstance;

    public static GlobalReq getInstance() {
        if (mInstance == null) {
            synchronized (GlobalReq.class) {
                if (mInstance == null) {
                    mInstance = new GlobalReq();
                }
            }
        }
        return mInstance;
    }

    /**
     * 返回 Observable<ResultBean<T>> 类型
     */
    private <T> Observable<ResultBean<T>> requests(Observable<ResultBean<T>> beanObservable) {
        return beanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(t -> new ResultBean<>(ERROR_CODE, t.getMessage()));//onErrorReturn 请求失败了返回一个错误信息的对象
    }

    private <T> Observable<ResultListBean<T>> requestss(Observable<ResultListBean<T>> beanObservable) {
        return beanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(t -> new ResultListBean<>(ERROR_CODE, t.getMessage()));
    }

    private <T> Observable<T> requestT(Observable<T> beanObservable) {
        return beanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 返回 Observable<ResultBean> 类型
     */
    private Observable<ResultBean> request(Observable<ResultBean> beanObservable) {
        return beanObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(t -> new ResultBean<>(ERROR_CODE, t.getMessage()));
    }


//    /**
//     * 获取消息列表
//     */
//    public Observable<ResultBean<MessageBean>> getMessageList(String pageNo, String pageSize) {
//        return RetrofitFactory.getInstance().create(SeniorApi.class)
//                .getMessageList(pageNo,pageSize)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .onErrorReturn(t -> new ResultBean<>(ERROR_CODE, t.getMessage()));
//    }


    /**
     * 获取消息列表
     */
//    public Observable<ResultBean<MessageBean>> getMessageList(String pageNo, String pageSize) {
//        return requests(RetrofitFactory.getInstance().create(SeniorApi.class)
//                .getMessageList(pageNo,pageSize)
//        );
//    }
    public Observable<ResultListBean<WanAndroidBannerBean>> getWanBanner() {
        return requestss(RetrofitFactory.getInstance().create(GlobalApi.class).getWanBanner());
    }

    public Observable<ResultBean<HomeListBean>> getHomeList(int page, int cid) {
        HashMap<String, Integer> map = new HashMap<>();
        if (cid != -1) {
            map.put("cid", cid);
        }
        return requests(RetrofitFactory.getInstance().create(GlobalApi.class).getHomeList(page, map));
    }

}
