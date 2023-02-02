package com.itaem.navigationdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// 列表头部类
public class DemoItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSectionHeight = 40;
    // 头部列表集合
    private List<NavigationBean.DataBean.ArticlesBean> titles ;
    // 头部背景画笔
    private Paint bPaint;
    // 头部文字画笔
    private Paint TextPaint;
    public DemoItemDecoration(Context context, List<NavigationBean.DataBean.ArticlesBean> titles) {
        this.titles = titles;
        // 设置画笔
        bPaint = new Paint();
        bPaint.setColor(Color.GRAY);
        // 文字笔
        TextPaint = new Paint();
        // 颜色
        TextPaint.setColor(Color.WHITE);
        // 字体大小
        TextPaint.setTextSize(40);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 获取列表的位置
        int position = parent.getChildAdapterPosition(view);
        // 在每个组上面空出一定距离 目前为40
        if(titles.get(position).isFirst()){
            outRect.top = mSectionHeight;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        // 获取列表长度
        int childCount = parent.getChildCount();
        for(int i = 0;i<childCount;i++){
            // child 为列表的view
            View child = parent.getChildAt(i);
            //获取列表位置
            int position = parent.getChildAdapterPosition(child);
            NavigationBean.DataBean.ArticlesBean app = titles.get(position);
            // 设置位于头部的文字
            if (app.isFirst()){
                // 获取四个方向的距离，来布置画布
                float sectionLeft = parent.getLeft();
                float sectionTop = child.getTop() - mSectionHeight;
                float sectionRight = parent.getWidth();
                float sectionBottom = child.getTop();
                c.drawRect(0,sectionTop,sectionRight,sectionBottom,bPaint);
                // 文字笔居中
                c.drawText(app.getChapterName(),sectionLeft,child.getTop()-5,TextPaint);
            }
/*            // 制作吸顶效果
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if(position == firstVisibleItemPosition){
                if(app.isLast()){
                    if(child.getBottom()<mSectionHeight){
                        float sectionLeft = parent.getLeft();
                        float sectionTop = child.getTop()-(mSectionHeight-child.getBottom())-mSectionHeight;
                        float sectionRight = parent.getWidth();
                        float sectionBottom = child.getTop()+mSectionHeight-(mSectionHeight-child.getBottom());
                        c.drawRect(0,sectionTop,sectionRight,sectionBottom,bPaint);
                        c.drawText(app.getChapterName(),parent.getLeft(),sectionBottom-5,TextPaint);
                    }else {
                        float sectionLeft = parent.getLeft();
                        float sectionTop = parent.getTop();
                        float sectionRight = parent.getWidth();
                        float sectionBottom = parent.getTop() + 80;
                        c.drawRect(0,sectionTop,sectionRight,sectionBottom,bPaint);
                        c.drawText(app.getChapterName(),parent.getLeft(),sectionBottom-5,TextPaint);
                    }
                }else {
                    float sectionLeft = parent.getLeft();
                    float sectionTop = parent.getTop();
                    float sectionRight = parent.getWidth();
                    float sectionBottom = parent.getTop() + 80;
                    c.drawRect(0,sectionTop,sectionRight,sectionBottom,bPaint);
                    c.drawText(app.getChapterName(),parent.getLeft(),sectionBottom - 5,TextPaint);
                }
            }*/
        }
    }
}
