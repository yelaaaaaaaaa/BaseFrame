package com.example.baseframe.weight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;


import com.blankj.ALog;
import com.example.baseframe.R;
import com.example.baseframe.databinding.LayoutInputHasSelectImageBinding;
import com.example.baseframe.utils.ScreenUtils;
import com.example.baseframe.weight.uploadImageList.UploadImgListBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * 输入文字和图片组件
 *
 * 使用：
 *   初始化：   //传入基础视图 用于监听软键盘高度
 *             parentContent = findViewById(android.R.id.content);
 *             inputView.setBaseView(parentContent);
 *             //初始化图片选择
 *             inputView.setUploadImgListBuilder(new UploadImgListBuilder().setContext(this).setMaxSelectedCount(9).setNoSelectPictureDialog(true).setUploadType("order-feedback"));
 *
 *   监听：     inputView.setInputViewListener(this);
 *
 *   onActivityResult    @Override
 *                          protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 *                              super.onActivityResult(requestCode, resultCode, data);
 *                              inputView.handleOnActivityResult(requestCode,resultCode,data);
 *                       }
 *
 *   onResume: //在Activity onResume中调用 用于恢复现场 （选择图片的时候弹窗需关闭 选择完毕后需弹出软键盘）
 *             @Override
 *             protected void onResume() {
 *                  super.onResume();
 *                  inputView.onResume();
 *              }
 *
 *    清除内容 (调用发送成功后调用)： clean();
 */


@SuppressLint("NonConstantResourceId")
public class InputView extends ConstraintLayout {
    /**
     * 上下文
     */
    private Context context;
    /**
     * 输入框
     */

    EditText etInput;
    /**
     * 图片选择组件
     */


    /**
     * 图片选择图标
     */

    ImageView ivSelectPicture;
    /**
     * 发送按钮
     */

    Button btSend;
    /**
     * 界面基础视图 （用于监听 是否弹出软键盘 ）
     */
    private View baseContentView;
    /**
     * 回调监听
     */
    private InputViewListener inputViewListener;

    /**
     * 键盘高度
     */
    private int softKeyHeight;
    /**
     * 是否展示
     */
    public boolean show = false;
    /**
     * 是否展示图片选择
     */
    private boolean showImageSelect;
    /**
     * 软键盘
     */
    private InputMethodManager imm;
    private LayoutInputHasSelectImageBinding dataBinding;
    interface BaseViewInitInf {
        void initView();

        void initData();

        void initListener();
    }

    public InputView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_input_has_select_image, this);
        dataBinding= DataBindingUtil.bind(view);
        etInput = dataBinding.etInput;

        ivSelectPicture = dataBinding.ivSelectPicture;
        initListener();
    }



    public void initListener() {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                ALog.i("监听到软键盘点击" + actionId);
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ALog.i("监听到软键盘点击隐藏软键盘操作 show=false");
                    show = false;
                }
                return false;
            }
        });

    }

    /**
     * 设置基础视图
     * 界面基础视图 （用于监听 是否弹出软键盘 ）
     */
    public void setBaseView(View baseContentView) {
        this.baseContentView = baseContentView;
        setBaseContentViewListener();
    }

    /**
     * 是否展示图片选择
     */
    public void showImageSelect(boolean showImageSelect){
        this.showImageSelect=showImageSelect;
        if(showImageSelect){
            ivSelectPicture.setVisibility(VISIBLE);
        }else {
            ivSelectPicture.setVisibility(GONE);
         //   uploadImgListView.setVisibility(GONE);
        }
    }

    /**
     * 图片组件设置参数
     */
    public void setUploadImgListBuilder(UploadImgListBuilder uploadImgListBuilder) {
      //  uploadImgListView.setUploadImgListBuilder(uploadImgListBuilder);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_select_picture:

                show = true;
                ALog.i("点击图片设置 show=true");
                break;
            case R.id.bt_send:
                ALog.i("点击发送");
                ALog.i("设置show=false");
                show = false;
                if (inputViewListener != null) {
                   // inputViewListener.clickSend(etInput.getText().toString(), uploadImgListView.getServiceImagesStrs());
                }
                hideView();
                break;

        }
    }

    /**
     * 选择图片时会关闭软键盘 返回后需恢复现场
     */
    public void onResume() {
        ALog.i("恢复现场");
        if (show) {
            showView();
        } else {
            hideView();
        }
    }


    /**
     * 为基础视图添加监听 软键盘弹窗的时候显示组件 隐藏的时候隐藏组件
     */
    private void setBaseContentViewListener() {
        if (baseContentView != null) {
            baseContentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect r = new Rect();
                    ALog.i("测量界面 show=" + show);
                    baseContentView.getWindowVisibleDisplayFrame(r);
                    int displayHeight = r.bottom - r.top;
                    int parentHeight = baseContentView.getHeight();
                    int visibleHeight = parentHeight - displayHeight;
                    if (visibleHeight > (ScreenUtils.getScreenHeight() / 4)) {
                        ALog.i("测量界面 展示软键盘了softKeyHeight= " + visibleHeight);
                        softKeyHeight = visibleHeight;
                        updateViewLocation();
                    } else {
                       InputView.this.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    /**
     * 显示界面
     */
    public void showView() {
        try {
            ALog.i("手动显示界面");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   InputView.this.setVisibility(View.VISIBLE);
                    showSoftInput();
                   InputView.this.etInput.requestFocus();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           InputView.this.etInput.requestFocus();
                        }
                    }, 200);
                }
            }, 300);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新界面位置
     */
    public void updateViewLocation() {
        int translationY = -softKeyHeight + ScreenUtils.getStatusBarHeight((Activity) context);
        ALog.i("更新界面位置 translationY=" + translationY);
       InputView.this.post(new Runnable() {
            @Override
            public void run() {
               InputView.this.setVisibility(View.VISIBLE);
               InputView.this.setTranslationY(translationY);

            }
        });

    }


    /**
     * 隐藏界面
     */
    public void hideView() {
        ALog.i("隐藏界面");
       InputView.this.setVisibility(View.GONE);
        hideSoftInput();
    }

    /**
     * 展示软键盘
     */
    public void showSoftInput() {
        //强制展示软键盘 （监听到软键盘弹起后要更新界面位置）
        imm.showSoftInput(etInput, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        //隐藏软键盘
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
    }

    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {

    }


    /**
     * 设置监听
     */
    public void setInputViewListener(InputViewListener inputViewListener) {
        this.inputViewListener = inputViewListener;
    }



    /**
     * 清除内容 (调用发送成功后调用)
     */
    public void clean() {
        etInput.setText("");

    }

    /**
     * 回调
     */
    public interface InputViewListener {
        /**
         * 点击发送
         */
        void clickSend(String content, List<String> images);

    }
}
