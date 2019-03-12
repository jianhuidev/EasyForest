package com.example.kys_8.easyforest.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.TakePhotoVariable;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.bean.User;
import com.example.kys_8.easyforest.ui.adapter.MyRecyclerItemTouchCallback;
import com.example.kys_8.easyforest.ui.adapter.OnRecyclerItemClickListener;
import com.example.kys_8.easyforest.ui.adapter.UploadAdapter;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;
import com.example.kys_8.easyforest.utils.VibratorUtil;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class UploadActivity extends AppCompatActivity implements MyRecyclerItemTouchCallback.OnDragListener{

    private final String TAG = "UploadActivity";
    private RecyclerView mRv;//要上传图片的那个
    private TextView mTv;
    private UploadAdapter adapter;

    private boolean isOne = true;
    private List<String> mSelectPath = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private EditText mEtUpload;
    private Switch mSwitch;
    private String isAnon = "0";

    /**
     * 从相册选择
     */
    private static final int ALBUM_CODE = 1;
    private boolean isChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
//        Toolbar toolbar = findViewById(R.id.toolbar_upload);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("分享你要保护的树木");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        GlobalVariable.isNeedRefresh = true;
        initView();
        syncRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upload, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.upload:
                uploadData();
                break;
        }
        return true;
    }

    public void initView() {
        mTv = (TextView) findViewById(R.id.tv);
        mSwitch = findViewById(R.id.anon_upload);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isAnon = "1";
//                    LogUtil.e(TAG,isAnon);
                }else {
                    isAnon = "0";
//                    LogUtil.e(TAG,isAnon);
                }
            }
        });
        mEtUpload = (EditText) findViewById(R.id.et_upload);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((CardView) findViewById(R.id.frage_shell_cardview_1)).setClipToOutline(false);
            ((CardView) findViewById(R.id.frage_shell_cardview_2)).setClipToOutline(false);
        }
    }

    public void syncRecyclerView(){
        mRv = (RecyclerView) findViewById(R.id.activity_shell_recycler);

        adapter = new UploadAdapter(this, R.layout.recycler_item_grid, mSelectPath);
        mRv.setHasFixedSize(true);
        mRv.setAdapter(adapter);
        mRv.setLayoutManager(new GridLayoutManager(this, 4));

        mItemTouchHelper = new ItemTouchHelper(new MyRecyclerItemTouchCallback(adapter).setOnDragListener(this));
        mItemTouchHelper.attachToRecyclerView(mRv);
        isOne = true;
        mRv.addOnItemTouchListener(new OnRecyclerItemClickListener(mRv){
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != mSelectPath.size() || mSelectPath.size() == TakePhotoVariable.MAX_SELECT_PHOTO_COUNT) {
                    mItemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(UploadActivity.this, 40);   //震动70ms
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != mSelectPath.size() || mSelectPath.size() == TakePhotoVariable.MAX_SELECT_PHOTO_COUNT) {
                    ToastUtil.showToast(UploadActivity.this,"图片缩放器");
                }else {
                    if (isOne){
                        isOne = false;
                        selectAalum();
                    }
                }
            }
        });
    }

    /**
     * 上传部分
     */
    private void uploadData(){
        if (mEtUpload.getText().toString().trim().isEmpty()) {
            ToastUtil.showToast(UploadActivity.this,"您还没有分享您的感受呢！");
            return;
        }
        if (mSelectPath.size() == 0) {
            ToastUtil.showToast(UploadActivity.this,"好像忘记了分享图片呀！");
            return;
        }
        final ProgressDialog hProgressDialog = new ProgressDialog(UploadActivity.this);
        hProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        hProgressDialog.setMessage("正在上传数据");
        hProgressDialog.setCancelable(false);
        hProgressDialog.setMax(100);
        hProgressDialog.show();
        final String[] filePaths = listToArr(mSelectPath);
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //如果数量相等，则代表文件全部上传完成，最后一次才行
                if(urls.size()==filePaths.length){
                    UploadData data = new UploadData();
                    data.setContent(mEtUpload.getText().toString().trim());
                    data.setImgs(files);
                    data.setIsAnon(isAnon);
                    if (!TextUtils.isEmpty(GlobalVariable.userInfo.getNickName())){
                        data.setUserNmae(GlobalVariable.userInfo.getNickName());
                    }else {
                        data.setUserNmae(GlobalVariable.userInfo.getUsername());
                    }
                    data.setUserAvatarUrl(GlobalVariable.userInfo.getAvatar() == null ? "0":
                            GlobalVariable.userInfo.getAvatar().getUrl());
                    data.setUserObjId(GlobalVariable.userInfo.getObjectId());
                    data.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            hProgressDialog.dismiss();
                            if (e == null){
                                isChange = true; // 更新成功，改变标识
                                ToastUtil.showToast(UploadActivity.this,"分享成功");
                                addALeaf();
                                finish();
                            } else{
                                ToastUtil.showToast(UploadActivity.this,"上传失败，请检查网络");
                            }
                        }
                    });

                }

            }

            @Override
            public void onProgress(int i, int curPercent, int i2, int totalPercent) {

                hProgressDialog.setProgress(totalPercent);
                if (totalPercent == 100)
                    hProgressDialog.setMessage("数据整理");

            }

            @Override
            public void onError(int i, String s) {
                hProgressDialog.dismiss();

            }
        });
    }

    private String[] listToArr(List<String> res) {
        String[] paths = new String[res.size()];
        for (int i = 0; i < res.size(); i++) {
            paths[i] = res.get(i);
        }
        return paths;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOne = true;
    }

    private void selectAalum(){
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选
                .multiSelect(true)
                .btnText("确定")
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(getResources().getColor(R.color.colorPrimary))
                .backResId(R.mipmap.back)
                .title("图片")
                .titleColor(Color.WHITE)
                .titleBgColor(getResources().getColor(R.color.colorPrimary))
                .allImagesText("所有图片")
                .needCrop(true)
                .cropSize(1, 1, 200, 200)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(9)
                .rememberSelected(false)
                .build();

        ISNav.getInstance().toListActivity(this, config, ALBUM_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALBUM_CODE && resultCode == RESULT_OK && data != null) {

            mSelectPath = data.getStringArrayListExtra("result");
            syncRecyclerView();
        }
    }

    @Override
    public void onFinishDrag() {

    }

    @Override
    public void deleteState(boolean delete) {
        if (delete) {
            mTv.setBackgroundResource(R.color.holo_red_dark);
            mTv.setText("放开手指，进行删除");
        } else {
            mTv.setText("拖动到此处，进行删除");
            mTv.setBackgroundResource(R.color.holo_red_light);
        }
    }

    @Override
    public void dragState(boolean start) {
        if (start) {
            mTv.setVisibility(View.VISIBLE);
        } else {
            mTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void isDelete(int position) {
        Log.e("tag", "确认删除：" + position);
        if (position >= 0) {
            mSelectPath.remove(position);
            adapter.syncResultList(mSelectPath);
            adapter.notifyItemRemoved(position);
        }
    }

//    /**
//     * 我的树叶加1
//     */
//    private void addLeaf(){
//        GlobalVariable.userInfo.increment("leaves",1);
//        GlobalVariable.userInfo.update(new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e == null){
//                    LogUtil.e(TAG," 树叶加一成功");
//                }else {
//                    LogUtil.e(TAG," 树叶加一失败"+e.getMessage()+e.getErrorCode());
//                }
//            }
//        });
//    }

    private void addALeaf(){
        User user = new User();
        user.setObjectId(GlobalVariable.userInfo.getObjectId());
        user.increment("leaves");
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    LogUtil.e(TAG," 树叶加一成功");
                }else {
                    LogUtil.e(TAG," 树叶加一失败"+e.getMessage()+e.getErrorCode());
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (isChange){
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
        }
        super.onBackPressed();
    }

}
