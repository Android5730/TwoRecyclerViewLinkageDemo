package com.itaem.navigationdemo;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.itaem.navigationdemo.left.LeftAdapter;
import com.itaem.navigationdemo.right.RightAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    // 左列表item，右列表头部
    private List<NavigationBean.DataBean> leftBeans = new ArrayList<>();
    private LeftAdapter leftAdapter;
    // 右列表全部item
    private List<NavigationBean.DataBean.ArticlesBean> rightBeans = new ArrayList<>();
    private RecyclerView rightRv;
    private RecyclerView leftRv;
    private RightAdapter rightAdapter;
    private List<Integer> integers = new ArrayList<>();
    private boolean leftScroller;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
  //      initView();
        initSent();
    }

    /**
     * 发送请求，获取数据
     */
    private void initSent() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.wanandroid.com/navi/json")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                parseData(string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                        // 增加头部。参数：context、右列表实体集合
                        DemoItemDecoration decoration = new DemoItemDecoration(MainActivity.this,rightBeans);
                        rightRv.addItemDecoration(decoration);
                    }
                });
            }
        });
    }

    /**
     * 解析返回数据
     * @param string
     */
    private void parseData(String string){
        int sort = 0;
        integers.add(sort);
        Gson gson = new Gson();
        NavigationBean navigationBean = gson.fromJson(string, NavigationBean.class);
        leftBeans = navigationBean.getData();
        for (NavigationBean.DataBean dataBean: navigationBean.getData()){
            for (int i = 0 ;i<dataBean.getArticles().size();i++){
                // 定好始末content的状态
                if (i==0){
                    dataBean.getArticles().get(i).setFirst(true);
                }else if (i==dataBean.getArticles().size()-1){
                    dataBean.getArticles().get(i).setLast(true);
                    integers.add(sort);
                }
                sort++;
                rightBeans.add(dataBean.getArticles().get(i));
            }
        }
    }
    /**
     * 设置UI
     */
    private void initView() {
        // 左RV
        leftRv = findViewById(R.id.navigation_recyclerView_left);
        leftRv.setLayoutManager(new LinearLayoutManager(this));
        leftAdapter = new LeftAdapter(leftBeans);
        leftRv.setAdapter(leftAdapter);
        // 右RV
        rightRv = findViewById(R.id.navigation_recyclerView_right);
        rightRv.setLayoutManager(new LinearLayoutManager(this));
        rightRv.setAdapter(new RightAdapter(rightBeans));
        // 左边导航选择
        leftAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                leftScroller = true;
                // 恢复每个item未选中后的状态
                for(int i=0;i<leftBeans.size();i++){
                    if (leftBeans.get(i).getSelect()&&i!=position){
                        leftBeans.get(i).setSelect(false);
                        leftAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                // 改变选中item数据的状态
                leftBeans.get(position).setSelect(true);//分类选中状态
                // 随之改变数据
                leftAdapter.notifyItemChanged(position);
                // 留为最后的办法
         //       leftAdapter.notifyDataSetChanged();
                // 匹配每个导航item对应的右RV头部
                for(i=0;i<rightBeans.size();i++){
                    if(leftBeans.get(position).getName().equals(rightBeans.get(i).getChapterName())){//分类对应的商品列表
                        // 调用方法，调动右RV
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                smoothMoveToPosition(rightRv,i);
                            }
                        }).start();
                        break;
                    }
                }
            }
        });
        // 监听右rv滑动
        rightRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                // 滑动状态变化时调用的方法
              //  super.onScrollStateChanged(recyclerView, newState);
                // 不再滑动状态SCROLL_STATE_IDLE且不是左RV调用右RV滑动
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&!leftScroller){
                    OnScrollChange(recyclerView);
                }
            }
        });
    }

    /**
     * 监听右RV滑动，联动左RV
     * @param recyclerView
     */
    private void OnScrollChange(final RecyclerView recyclerView){
        // 获取每次滑动状态暂停时可见第一项位置
        int firstItemPosition = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
        int lastItemPosition = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1));
        // 右rv已经滑动到底部了
/*        if (lastItemPosition> integers.get(integers.size()-1)){
            return;
        }*/
        for (int j = 0;j<integers.size();j++){
            if (j!=integers.size()-1&&firstItemPosition>=integers.get(j)&&firstItemPosition<integers.get(j+1)){
                Log.d("TAG", "滑动安排 ");
                for(int i=0;i<leftBeans.size();i++){
                    if (leftBeans.get(i).getSelect()){
                        if (i==j){
                            return;
                        }
                        leftBeans.get(i).setSelect(false);
                        leftAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                // 改变选中item数据的状态
                leftBeans.get(j).setSelect(true);//分类选中状态
                leftAdapter.notifyItemChanged(j);

                //  smoothMoveToPosition(leftRv,j);
                // 如果移动位置仍在
                // 左rv最后可见项 后面，便调用方法滑动左rv
                // 左rv可见项第一个前面，也调用
                smoothMoveToPosition(leftRv,j);
                leftScroller = false;
                Log.d("TAG", "滑动安排左 "+j);
                break;
          /*      if (leftRv.getChildLayoutPosition(leftRv.getChildAt(leftRv.getChildCount() - 1))<j||
                leftRv.getChildLayoutPosition(leftRv.getChildAt(0))>j){
                   // recyclerView.smoothScrollToPosition(j);


                }*/
            }
        }
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    /**
     * 使指定的项平滑到顶部
     * @param mRecyclerView
     * @param position      待指定的项
     */
    private void smoothMoveToPosition(final RecyclerView mRecyclerView, final int position) {
        int firstItemPosition = -1;
        int lastItemPosition = -1;
        // todo 获取第一个和最后一个可见位置方式1
        // 第一个可见位置
        firstItemPosition = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        lastItemPosition = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        Log.i("firstItemPosition", firstItemPosition + "");
        Log.i("lastItemPosition", lastItemPosition + "");
        Log.i("position",position+"");
        if (position < firstItemPosition) {
            // 第一种可能:跳转位置在第一个可见位置之前
            leftScroller = true;
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItemPosition) {
            // 第二种可能:跳转位置在第一个可见位置之后,在最后一个可见项之前
            // 移动末位置-当前第一个item位置
            int movePosition = position - firstItemPosition;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                // getTop():该视图相对于其父视图的顶部位置。
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                leftScroller = false;
                mRecyclerView.smoothScrollBy(0, top);//dx>0===>向左  dy>0====>向上
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
            leftScroller = true;
            //监听事件的设置，仅仅是为了第三种情况，即：要跳转的位置在可见项之后
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    // 目标在可见项后：且已经滑动到可见
                    if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {//
                        mShouldScroll = false;
                        // 递归调用方法，一般处于第二种情况
                        smoothMoveToPosition(mRecyclerView, mToPosition);
                    }
                }
            });
        }
    }
}