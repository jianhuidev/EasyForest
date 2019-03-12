package com.example.kys_8.easyforest.ui.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.bean.User;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.SearchDialog;
import com.example.kys_8.easyforest.utils.SpUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;
import com.example.kys_8.easyforest.weight.MaterialSearchView;
import com.example.kys_8.easyforest.weight.onSearchListener;
import com.example.kys_8.easyforest.weight.onSimpleSearchActionsListener;
import com.example.kys_8.easyforest.weight.picker.DatePickerDialog;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MDUserInfoActivity extends AppCompatActivity implements View.OnClickListener,onSimpleSearchActionsListener,onSearchListener {

    private ImageView bgImg,avatarImg;
    private TextView birthdayTv,myLeaves;
    private TextView albumAvatarTv,photoAvatarTv,albumBgTv,photoBgTv,photoBygTv;
    private EditText nicknameEt,emailEt,phoneEt,introEt;
    private FloatingActionButton mFab;
    private LinearLayout birthdayLayout;
    ProgressBar progressInfo,progressAvatar;
    private BottomSheetDialog mBottomSheetDialog;

    public MaterialSearchView mSearchView;
    private WindowManager mWindowManager;
    private Toolbar mToolbar;
    private boolean mSearchViewAdded = false;

    private static final int AVATAR_CAMERA = 1;
    private static final int AVATAR_ALBUM = 2;
    private static final int BG_CAMERA = 3;
    private static final int BG_ALBUM = 4;
    private static final int BYG = 5;

    private int[] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mduser_info);
        mToolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("个人信息");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initMaterialSearchView();
        initView();
    }

    private void initView(){
//        arr = new int[3];
        myLeaves = findViewById(R.id.my_leaves_u);
        avatarImg = findViewById(R.id.avatar_user_info);
        bgImg = findViewById(R.id.bg_img_u);
        nicknameEt = findViewById(R.id.nickname_et_user);
        birthdayLayout = findViewById(R.id.birthday_layout_user);
        birthdayLayout.setOnClickListener(this);
        birthdayTv = findViewById(R.id.birthday_tv_u);
        emailEt = findViewById(R.id.et_mail_u);
        phoneEt = findViewById(R.id.mdphone_et_u);

        introEt = findViewById(R.id.mdintro_et_u);
        progressInfo = findViewById(R.id.progress_info);
        progressAvatar = findViewById(R.id.progress_avatar);
        mFab = findViewById(R.id.fab_user);
        mFab.setOnClickListener(this);

        if (GlobalVariable.userInfo == null){
            return;
        }
        updateUserInfo();
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
                                MaterialSearchView.getSearchViewLayoutParams(MDUserInfoActivity.this));
                        mSearchViewAdded = true;
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.change_user:
                showBottomDialog();
                break;
            case R.id.search_user:
                mSearchView.display();
                break;
        }
        return true;
    }

    private void startUpdateData(){
        mFab.hide();
        progressInfo.setVisibility(View.VISIBLE);
    }

    private void endUpdateData(){
        mFab.show();
        progressInfo.setVisibility(View.INVISIBLE);
    }
    private void startUpdateImage(){
        progressAvatar.setVisibility(View.VISIBLE);
    }

    private void endUpdateImage(){
        progressAvatar.setVisibility(View.INVISIBLE);
    }

    private void setUserInfo(){
        User user = GlobalVariable.userInfo;
        nicknameEt.setText(user.getNickName());
        emailEt.setText(user.getEmail());
        phoneEt.setText(user.getPhone());
        introEt.setText(user.getIntro());
        birthdayTv.setText(user.getBirthday());
        myLeaves.setText("："+user.getLeaves());
    }

    private void updateUserInfo(){
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(GlobalVariable.userInfo.getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e == null){
                    GlobalVariable.userInfo = user;
                    setUserInfo();
                    setImage(GlobalVariable.userInfo.getByoBg());
                    SpUtil.put("userInfo", JSON.toJSONString(user));
                }else{
                    LogUtil.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 设置头像与背景
     * @param bg 可判断是否为默认背景
     */
    private void setImage(int bg){
        if (GlobalVariable.userInfo.getAvatar() != null){
            Glide.with(this).load(GlobalVariable.userInfo.getAvatar().getUrl())
                    .error(R.mipmap.avatar_off)
                    .crossFade()
                    .into(avatarImg);
        }
        if (bg == -1 && GlobalVariable.userInfo.getUserBackground() != null){
            Glide.with(this).load(GlobalVariable.userInfo.getUserBackground().getUrl())
                    .error(R.mipmap.card_image_1)
                    .crossFade()
                    .into(bgImg);
        }else {
            Glide.with(this).load(GlobalVariable.drawArr[bg])
                    .error(R.mipmap.card_image_1)
                    .crossFade()
                    .into(bgImg);
        }
    }

    private void submit(){
        if (GlobalVariable.userInfo == null)
            return;
        final String nickName = nicknameEt.getText().toString().trim();
        final String birthday = birthdayTv.getText().toString().trim();
        final String email = emailEt.getText().toString().trim();
        final String phone = phoneEt.getText().toString().trim();
        final String intro = introEt.getText().toString().trim();
        User user = new User();
        user.setValue("nickName",nickName);
        user.setValue("birthday",birthday);
        user.setValue("email",email);
        user.setValue("phone",phone);
        user.setValue("intro",intro);
        startUpdateData();
        user.update(GlobalVariable.userInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    GlobalVariable.userInfo.setNickName(nickName);
                    GlobalVariable.userInfo.setBirthday(birthday);
                    GlobalVariable.userInfo.setEmail(email);
                    GlobalVariable.userInfo.setPhone(phone);
                    GlobalVariable.userInfo.setIntro(intro);
                    ToastUtil.showToast(MDUserInfoActivity.this,"更新成功");
                    SpUtil.put("userInfo", JSON.toJSONString(GlobalVariable.userInfo));
                    queryUploadData();
                }else {
                    endUpdateData();
                    if (e.getErrorCode() == 203){
                        ToastUtil.showToast(MDUserInfoActivity.this,"注意一个邮箱只能绑定一个账号哦");
                        return;
                    }
                    ToastUtil.showToast(MDUserInfoActivity.this,"更新失败，请检查网络");
                    LogUtil.e("MDUserInfoActivity","更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }

    private void showBottomDialog(){
        mBottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom_image, null);
        mBottomSheetDialog.setContentView(view);
        albumAvatarTv = view.findViewById(R.id.album_avatar_tv);
        photoAvatarTv = view.findViewById(R.id.photo_avatar_tv);
        albumBgTv = view.findViewById(R.id.album_bg_tv);
        photoBgTv = view.findViewById(R.id.photo_bg_tv);
        photoBygTv = view.findViewById(R.id.photo_byg_tv);

        albumAvatarTv.setOnClickListener(this);
        photoAvatarTv.setOnClickListener(this);
        albumBgTv.setOnClickListener(this);
        photoBgTv.setOnClickListener(this);
        photoBygTv.setOnClickListener(this);

        mBottomSheetDialog.show();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fab_user:
                submit();
                break;
            case R.id.birthday_layout_user:
                new DatePickerDialog.Builder(this)
                        .setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
                            @Override
                            public void onDateSelected(int[] dates) {
                                String dateStr = dates[0]+"/"+dates[1]+"/"+dates[2]+" ";
                                birthdayTv.setText(dateStr);
                            }
                        }).create("请选择你的生日").show();
                break;
            case R.id.album_avatar_tv:
                album(AVATAR_ALBUM,true); // 头像提供裁剪
                mBottomSheetDialog.cancel();
                break;
            case R.id.photo_avatar_tv:
                camera(AVATAR_CAMERA);
                mBottomSheetDialog.cancel();
                break;
            case R.id.album_bg_tv:
                album(BG_ALBUM,false);
                mBottomSheetDialog.cancel();
                break;
            case R.id.photo_bg_tv:
                camera(BG_CAMERA);
                mBottomSheetDialog.cancel();
                break;
            case R.id.photo_byg_tv:
                startActivityForResult(new Intent(this,BgActivity.class),BYG);
                mBottomSheetDialog.cancel();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            String filePath = "";
            List<String> pathList;
            switch (requestCode){
                case AVATAR_CAMERA:
                    filePath = data.getStringExtra("result");
                    setImage(filePath,1); // 1 代表头像
//                    queryUploadData();
                    break;
                case BG_CAMERA:
                    filePath = data.getStringExtra("result");
                    setImage(filePath,2);
                    break;
                case AVATAR_ALBUM:
                    pathList = data.getStringArrayListExtra("result");
                    for (String path : pathList) {filePath = path;}
                    setImage(filePath,1); // 1 代表头像
//                    queryUploadData();
                    break;
                case BG_ALBUM:
                    pathList = data.getStringArrayListExtra("result");
                    for (String path : pathList) {filePath = path;}
                    setImage(filePath,2);
                    break;
                case BYG:

                    break;
            }
        }
    }

    /**
     * 上传图片并将图片设置到头像或背景
     * @param filePath 图片路径
     * @param code 1 代表头像
     */
    private void setImage(String filePath,int code){
        if (filePath == null){
            ToastUtil.showToast(this,"图片未选择，请选择图片");
            return;
        }
        if (code == 1){
            Glide.with(this).load(new File(filePath))
                    .error(R.mipmap.card_image_1).crossFade().into(avatarImg);
            updateImage(filePath,1);
        }else {
            Glide.with(this).load(new File(filePath))
                    .error(R.mipmap.card_image_1).crossFade().into(bgImg);
            updateImage(filePath,2);
        }
    }

    private void updateImage(String filePath, final int code){
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        startUpdateImage();
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    if (code == 1){ // 1 代表处理头像
                        GlobalVariable.userInfo.setAvatar(bmobFile);
                        setUserTable(1);
                    }else {
                        GlobalVariable.userInfo.setUserBackground(bmobFile);
                        GlobalVariable.userInfo.setByoBg(-1);
                        setUserTable(2);
                    }
                }else {
                    endUpdateImage();
                    ToastUtil.showToast(MDUserInfoActivity.this,"上传失败，请检查网络");
                }
            }
        });
    }

    /**
     * 将上传的图片，更新到User 表
     * @param code 1 代表头像
     */
    private void setUserTable(final int code){
        GlobalVariable.userInfo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    if (code == 1){
                        queryUploadData();
                    }else {
                        ToastUtil.showToast(MDUserInfoActivity.this,"上传成功");
                        endUpdateImage();
                    }
                }else {
                    endUpdateImage();
                    ToastUtil.showToast(MDUserInfoActivity.this,"上传失败，请检查网络");
                    LogUtil.e("MDUserInfoActivity",e.getMessage());
                }
            }
        });
    }

    /**
     * 将我的分享数据查询出来
     */
    private void queryUploadData(){
        BmobQuery<UploadData> query = new BmobQuery<>();
        query.addWhereEqualTo("userObjId", GlobalVariable.userInfo == null ? "" : GlobalVariable.userInfo.getObjectId());
        query.findObjects(new FindListener<UploadData>() {
            @Override
            public void done(List<UploadData> list, BmobException e) {
                if (e == null){
                    if (list !=null && list.size()>0){
                        arr = new int[list.size()];
                        for (int i = 0; i<arr.length; i++){
                            updateUploadData(list.get(i),i);
                        }
                    }else {
                        endUpdateImage();
                        endUpdateData();
                    }
                }else {
                    endUpdateImage();
                    endUpdateData();
                    LogUtil.e("MDUserInfoActivity","查询失败" + e.getMessage()+" "+e.getErrorCode());
                }
            }
        });
    }

    private void updateUploadData(UploadData current, final int position){
        current.setUserAvatarUrl(GlobalVariable.userInfo.getAvatar() == null ? "0":
                GlobalVariable.userInfo.getAvatar().getUrl());
        if (!TextUtils.isEmpty(GlobalVariable.userInfo.getNickName())){
            current.setUserNmae(GlobalVariable.userInfo.getNickName());
        }else {
            current.setUserNmae(GlobalVariable.userInfo.getUsername());
        }
        current.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    if (updateFinish(position)){
                        endUpdateImage();
                        endUpdateData();
                        GlobalVariable.isNeedRefresh = true;
                        ToastUtil.showToast(MDUserInfoActivity.this,"更新成功");
                    }
                }else {
                    ToastUtil.showToast(MDUserInfoActivity.this,"数据同步错误，请检查网络");
                    LogUtil.e("MDUserInfoActivity","查询失败" + e.getMessage()+" "+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 检查是不是最后一个头像更改
     */
    private synchronized boolean updateFinish(int position){
        arr[position] = 1;
        for (int a : arr){
            if (a == 0){
                return false;
            }
        }
        return true;
    }


    public void camera(int reqCode) {
        ISCameraConfig config = new ISCameraConfig.Builder()
                .needCrop(true)
                .cropSize(1, 1, 200, 200)
                .build();
        ISNav.getInstance().toCameraActivity(this, config, reqCode);
    }

    private void album(int reqCode,boolean crop){
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选
                .multiSelect(false)
                .btnText("Confirm")
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(getResources().getColor(R.color.colorPrimary))
                .backResId(R.mipmap.back)
                .title("请选择一个图片")
                .titleColor(Color.WHITE)
                .titleBgColor(getResources().getColor(R.color.colorPrimary))
                .allImagesText("所有图片")
                .needCrop(crop)
//                .cropSize(1, 1, 200, 200)
                // 第一个是否显示相机
                .needCamera(false)
                // 最大选择图片数量
                .maxNum(9)
                .build();
        ISNav.getInstance().toListActivity(this, config, reqCode);
    }

//    快捷搜索框部分
    @Override
    public void onSearch(String query) {
        new SearchDialog(this,mSearchView,query).show();
    }

    @Override
    public void searchViewOpened() {

    }

    @Override
    public void searchViewClosed() {

    }

    @Override
    public void onCancelSearch() {mSearchView.hide();}

    @Override
    public void onItemClicked(String item) {

    }

    @Override
    public void onScroll() {

    }

    @Override
    public void error(String localizedMessage) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWindowManager != null)
            mWindowManager.removeViewImmediate(mSearchView);
    }
}
