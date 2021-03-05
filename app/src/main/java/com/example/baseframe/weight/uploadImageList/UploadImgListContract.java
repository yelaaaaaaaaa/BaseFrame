package com.example.baseframe.weight.uploadImageList;

import java.util.List;

public interface UploadImgListContract {

    void setData(List<String> afterSaleImage);

    void clear();

    void setUploadImgListBuilder(UploadImgListBuilder uploadImgListBuilder);
}
