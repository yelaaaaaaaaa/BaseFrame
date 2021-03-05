package com.example.baseframe.weight.uploadImageList;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

public class UploadImgListBuilder {
    private AppCompatActivity mContext;
    private String mType;
    private int mMaxCount=9;
    private boolean canSelectVideo;
    /**
     * 不展示底部选择弹窗
     */
    private boolean noSelectPictureDialog;


    public UploadImgListBuilder setContext(AppCompatActivity mContext) {
        this.mContext = mContext;
        return this;
    }

    public UploadImgListBuilder setUploadType(String mType) {
        this.mType = mType;
        return this;
    }

    public UploadImgListBuilder setMaxSelectedCount(int mMaxCount) {
        this.mMaxCount = mMaxCount;
        return this;
    }

    public boolean isCanSelectVideo() {
        return canSelectVideo;
    }

    public void setCanSelectVideo(boolean canSelectVideo) {
        this.canSelectVideo = canSelectVideo;
    }

    public UploadImgListBuilder get() {
        return this;
    }

    public AppCompatActivity getContext() {
        return mContext;
    }

    public String getType() {
        return mType;
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    public boolean isNoSelectPictureDialog() {
        return noSelectPictureDialog;
    }

    public UploadImgListBuilder setNoSelectPictureDialog(boolean noSelectPictureDialog) {
        this.noSelectPictureDialog = noSelectPictureDialog;
        return this;
    }
}
