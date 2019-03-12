package com.example.kys_8.easyforest.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.MapBean;

import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.SearchDialog;
import com.example.kys_8.easyforest.utils.ToastUtil;
import com.example.kys_8.easyforest.weight.MaterialSearchView;
import com.example.kys_8.easyforest.weight.onSearchListener;
import com.example.kys_8.easyforest.weight.onSimpleSearchActionsListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MapActivity extends AppCompatActivity
        implements onSimpleSearchActionsListener,onSearchListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private final int[] res = {R.drawable.tag_mulan,R.drawable.tag_mulan,R.drawable.tag_mulan,
            R.drawable.tag_mulan,R.drawable.tag_mulan,R.drawable.tag_mulan};
    Map<String, Integer> mMap = new HashMap<>();
    public MaterialSearchView mSearchView;
    private WindowManager mWindowManager;
    private Toolbar mToolbar;
    private boolean mSearchViewAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mToolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("重点保护林木分布");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initMap();
        initView();
        initMaterialSearchView();
    }

    private void initMaterialSearchView(){
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mSearchView = new MaterialSearchView(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setHintText("重点林木查询");
        if (mToolbar != null) {
            // Delay adding SearchView until Toolbar has finished loading
            mToolbar.post(new Runnable() {
                @Override
                public void run() {
                    if (!mSearchViewAdded && mWindowManager != null) {
                        mWindowManager.addView(mSearchView,
                                MaterialSearchView.getSearchViewLayoutParams(MapActivity.this));
                        mSearchViewAdded = true;
                    }
                }
            });
        }
    }

    private void initMap(){
        mMap.put("1",R.drawable.yinxing);
        mMap.put("2",R.drawable.hongsong);
        mMap.put("3",R.drawable.jinqiansong);
        mMap.put("4",R.drawable.shuishan);
        mMap.put("5",R.drawable.lianxiangshu);
        mMap.put("6",R.drawable.xiayepolei);
        mMap.put("7",R.drawable.polei);
        mMap.put("8",R.drawable.runnan);
        mMap.put("9",R.drawable.minnan);
        mMap.put("10",R.drawable.hongchun);
        mMap.put("11",R.drawable.fujianbai);
        mMap.put("12",R.drawable.chisutie12);
        mMap.put("13",R.drawable.sutie);
        mMap.put("14",R.drawable.tushan);
        mMap.put("15",R.drawable.zhejiannan);
        mMap.put("16",R.drawable.nanmu);
        mMap.put("17",R.drawable.younan);
        mMap.put("18",R.drawable.ezhangqiu);
        mMap.put("19",R.drawable.dayemulian);
        mMap.put("20",R.drawable.gongtong);
        mMap.put("21",R.drawable.gygongtong);
        mMap.put("22",R.drawable.ynlanguoshu);
        mMap.put("23",R.drawable.chuishu23);
    }

    private void initView(){
        //获取地图控件引用
        mMapView = findViewById(R.id.map_view);
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false); // 让百度地图默认的地图缩放控件不显示
        mMapView.removeViewAt(1); // 不显示百度地图Logo
        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(6.2f);
        mBaiduMap.animateMapStatus(update);
        LatLng ll = new LatLng(30.5942,114.312981);//location
        MapStatusUpdate location = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(location); // 定位到合适的位置
        query();
        setMarkerListener();
    }

    private void query(){
        BmobQuery<MapBean> query = new BmobQuery<>();
        query.findObjects(new FindListener<MapBean>() {
            @Override
            public void done(List<MapBean> list, BmobException e) {
                if (e == null){
                    addOverlay(list);
                }else {
                    LogUtil.e("MapActivity",e.getMessage());
                }
            }
        });
    }

    /**
     * 添加覆盖物的方法
     */
    private void addOverlay(List<MapBean> list){
        mBaiduMap.clear(); // 清除地图上所有覆盖物，无法分成批删除
        View view = View.inflate(this, R.layout.map_mark, null);
        final ImageView img = view.findViewById(R.id.img_mark);
        BitmapDescriptor bitmap;
        Marker marker;
        //创建OverlayOptions的集合
//        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        for (MapBean m : list){
            img.setImageResource(mMap.get(m.getMark()));
            bitmap = BitmapDescriptorFactory.fromView(view);
//            new MarkerOptions().position(new LatLng(m.getLatitude(),m.getLongitude()))
//            .icon(bitmap).animateType(MarkerOptions.MarkerAnimateType.grow);
            marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().position(new LatLng(m.getLatitude(),m.getLongitude()))
                    .icon(bitmap).animateType(MarkerOptions.MarkerAnimateType.grow));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info",m);
            marker.setExtraInfo(bundle);
        }
    }


    private void setMarkerListener(){
        final View view = View.inflate(this, R.layout.baidu_map_info_layout, null);
        final TextView tv_title = view.findViewById(R.id.map_info_tv_title);
        final TextView tv_content = view.findViewById(R.id.tv_content);
        final ImageView img = view.findViewById(R.id.map_info_img);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                InfoWindow infoWindow;
                if (marker.getExtraInfo() != null){
                    final MapBean info = (MapBean) marker.getExtraInfo().get("info");
                    img.setImageResource(mMap.get(info.getMark()));
                    tv_title.setText(info.getName());
                    tv_content.setText(info.getFbfw());

                    //得到点击的覆盖物的经纬度
                    LatLng ll = marker.getPosition();
                    infoWindow = new InfoWindow(view, ll, -80);
                    mBaiduMap.showInfoWindow(infoWindow);//显示此infoWindow
                    //让地图以备点击的覆盖物为中心
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(status);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MapActivity.this,TreeDetaActivity.class);
                            intent.putExtra("mark",info.getName());
                            startActivity(intent);
                        }
                    });
                }else {
                    ToastUtil.showToast(MapActivity.this,"请检查网络");
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.clear_info:
                mBaiduMap.hideInfoWindow();
                break;
            case R.id.search_map:
                mSearchView.display();
                break;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mWindowManager != null)
            mWindowManager.removeViewImmediate(mSearchView);
    }

    @Override
    public void onSearch(String query) {
        new SearchDialog(this,mSearchView,query).show();
    }

    @Override
    public void searchViewOpened() {}

    @Override
    public void searchViewClosed() {}

    @Override
    public void onCancelSearch() {mSearchView.hide();}

    @Override
    public void onItemClicked(String item) {}

    @Override
    public void onScroll() {}

    @Override
    public void error(String localizedMessage) {}
}
