package com.gp.pulltorefresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener{

    private SwipeRefreshLayout swipeRefresh;
    private ListView listView;
    private List<String> listData = new ArrayList<>();
    private View footLoadMore, emptyView, header, footView_nodata;
    private ArrayAdapter<String> adapter;
    private int times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        listView = (ListView) findViewById(R.id.list);


        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
//        swipeRefresh.setProgressViewOffset(true, -20, 150);
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        // 通过 setEnabled(false) 禁用下拉刷新
//        swipeRefresh.setEnabled(false);
        // 设定下拉圆圈的背景
        swipeRefresh.setProgressBackgroundColor(R.color.red);
        //设置手势下拉刷新的监听
        swipeRefresh.setOnRefreshListener(this);


        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listData);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, listData.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnScrollListener(this);

        footLoadMore = LayoutInflater.from(this).inflate(R.layout.listview_footer_loading, null);
        footView_nodata = LayoutInflater.from(this).inflate(R.layout.listview_footer_nodata, null);
        header = LayoutInflater.from(this).inflate(R.layout.listview_header, null);
        emptyView = LayoutInflater.from(this).inflate(R.layout.listview_emptyview, null);
        listView.addHeaderView(header);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);

        swipeRefresh.setRefreshing(true);
//        onRefresh();
    }

    @Override
    public void onRefresh() {
        //setRefreshing(false) 和 setRefreshing(true) 来手动调用刷新的动画
        listData.clear();
        adapter.notifyDataSetChanged();
        swipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> temp = new ArrayList<String>();
                for(int i = 0; i < 20; i++ ){
                    temp.add("初始数据" + i);
                }
                listData.addAll(temp);
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        }, 5000);
    }

    private void getData(){
        if(times == 4){
            times = 0;
            listView.removeFooterView(footLoadMore);
            listView.addFooterView(footView_nodata);
            Toast.makeText(this, "没有数据了", Toast.LENGTH_SHORT).show();
        }else {
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<String> temp = new ArrayList<String>();
                    for (int i = 0; i < 10; i++){
                        temp.add("灼灼桃花" + i);
                    }
                    listData.addAll(temp);
                    adapter.notifyDataSetChanged();

                    times++;
                }
            }, 5000);

            listView.removeFooterView(footLoadMore);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        switch (scrollState) {
//            case SCROLL_STATE_IDLE:
//                boolean flag = isListViewReachBottomEdge(view);
//                if (flag) {
//                    getData();
//                }
//                break;
//        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean flag = ((firstVisibleItem + visibleItemCount) == totalItemCount);
        if(flag){
            listView.addFooterView(footLoadMore);
            getData();
        }
    }

    //listView滑动到最后一个Item的内容的底部
    public boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result;
    }

    //ListView滚动到顶部
    public boolean isListViewReachTopEdge(final ListView listView) {
        boolean result = false;
        if (listView.getFirstVisiblePosition() == 0) {
            final View topChildView = listView.getChildAt(0);
            result = topChildView.getTop() == 0;
        }
        return result;
    }
}
