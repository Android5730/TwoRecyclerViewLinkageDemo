package com.itaem.navigationdemo.left;


import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itaem.navigationdemo.R;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.itaem.navigationdemo.NavigationBean;

import java.util.List;

public class LeftAdapter extends BaseQuickAdapter<NavigationBean.DataBean, BaseViewHolder> {
    public LeftAdapter(@Nullable List<NavigationBean.DataBean> data) {
        super(R.layout.item_left, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, NavigationBean.DataBean dataBean) {
        baseViewHolder.setText(R.id.text_item_left,dataBean.getName());
        if (dataBean.getSelect()){
            baseViewHolder.setBackgroundColor(R.id.text_item_left, Color.RED);
        }else {
            baseViewHolder.setBackgroundResource(R.id.text_item_left, R.color.gray);
        }
    }
}
