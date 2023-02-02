package com.itaem.navigationdemo.right;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.itaem.navigationdemo.NavigationBean;
import com.itaem.navigationdemo.R;

import java.util.List;

public class RightAdapter extends BaseQuickAdapter<NavigationBean.DataBean.ArticlesBean, BaseViewHolder> {
    public RightAdapter(@Nullable List<NavigationBean.DataBean.ArticlesBean> data) {
        super(R.layout.item_right, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, NavigationBean.DataBean.ArticlesBean articlesBean) {
        baseViewHolder.setText(R.id.text_item_right,articlesBean.getTitle());
    }
}
