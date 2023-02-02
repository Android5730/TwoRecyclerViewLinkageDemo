package com.itaem.navigationdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.itaem.navigationdemo.left.LeftAdapter;
import com.itaem.navigationdemo.right.RightAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private List<NavigationBean.DataBean> leftBeans = new ArrayList<>();
    private List<NavigationBean.DataBean.ArticlesBean> rightBeans = new ArrayList<>();
    private RecyclerView rightRv;
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
        Gson gson = new Gson();
        NavigationBean navigationBean = gson.fromJson(string, NavigationBean.class);
        leftBeans = navigationBean.getData();
        for (NavigationBean.DataBean dataBean: navigationBean.getData()){
            for (int i = 0 ;i<dataBean.getArticles().size();i++){
                // 定好始末content的状态
                if (i==0){
                    dataBean.getArticles().get(i).setFirst(true);
                }else if (i==dataBean.getArticles().size()){
                    dataBean.getArticles().get(i).setLast(true);
                }
                rightBeans.add(dataBean.getArticles().get(i));
            }
        }
    }
    /**
     * 设置UI
     */
    private void initView() {
        // 左RV
        RecyclerView leftRv = findViewById(R.id.navigation_recyclerView_left);
        leftRv.setLayoutManager(new LinearLayoutManager(this));
        leftRv.setAdapter(new LeftAdapter(leftBeans));
        // 右RV
        rightRv = findViewById(R.id.navigation_recyclerView_right);
        rightRv.setLayoutManager(new LinearLayoutManager(this));
        rightRv.setAdapter(new RightAdapter(rightBeans));
    }
}