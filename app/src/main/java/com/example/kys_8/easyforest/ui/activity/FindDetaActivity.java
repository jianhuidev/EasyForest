package com.example.kys_8.easyforest.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kys_8.easyforest.GlobalVariable;
import com.example.kys_8.easyforest.R;
import com.example.kys_8.easyforest.bean.CommentData;
import com.example.kys_8.easyforest.bean.UploadData;
import com.example.kys_8.easyforest.ui.adapter.CommentAdapter;
import com.example.kys_8.easyforest.ui.adapter.FDAdapter;
import com.example.kys_8.easyforest.utils.LogUtil;
import com.example.kys_8.easyforest.utils.ToastUtil;
import com.example.kys_8.easyforest.weight.fabnav.FooterLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class FindDetaActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "FDActivity";
    private RecyclerView mRv,commentRv;
    private FDAdapter imgAdapter;
    private CommentAdapter commentAdapter;
    private FooterLayout mFabLayout;

    private FloatingActionButton mFab;
    /**
     * 图片的个数
     */
    private int size = 9; // 拟作intent 传进来的
    private LinearLayout mline;
    private UploadData mUploadData;
    private LinearLayout likeLayout,dislikeLayout,starLayout,commentLayout,shareLayout;
    private TextView mCollectTv,mDislikeTv,mLikeTv,shareTv;
    private TextView mSend;
    private EditText mCommentEd;
    /**
     * 点赞数，默认为0
     */
    private int likeCount = 0;
    private int starCount = 0;
    private int dislikeCount = 0;
    private int shareCount = 0;
    private int commentCount = 0;
    ImageView likeImg,dislikeImg,starImg,shareImg;
    /**
     * 该变量用来控制是灰色图片还是黄色图片之类的
     * 是否收藏，默认没有 false
     */
    private boolean isStar = false;
    private boolean isLike = false;
    private boolean isDislike = false;
    /**
     * 是否进行了转发操作，如果进行了，返回时更新数据库转发量
     */
    private boolean toShare = false;

    private ImageView avatarImg;
    private TextView usernameTv,timeTv,contentTv,commentTv;

    /**
     * 参评后，返回回调标识
     */
    private boolean isChange = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_deta);
        Toolbar toolbar = findViewById(R.id.toolbar_fd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("发现");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mUploadData = (UploadData) getIntent().getSerializableExtra("upload");
        size = mUploadData.getImgs().size();
        handleUploadData();
        GlobalVariable.isNeedRefresh = true;
        initView();
        initImgRecyclerView();
        initCommentRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toShare = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (toShare)
            updateShareCount();
    }

    private void handleUploadData(){
        // 如果收藏的集合不为空，就把集合的size 赋给starCount
        if (mUploadData.getCollectObjIds() != null){
            starCount = mUploadData.getCollectObjIds().size();
        }
        // 检查是否收藏
        if (mUploadData.getCollectObjIds() != null && GlobalVariable.userInfo != null &&
                mUploadData.getCollectObjIds().contains(GlobalVariable.userInfo.getObjectId())){
            isStar = true; // 检查初始化，很重要，后面的逻辑在这之后
        }
        if (mUploadData.getLikeObjIds() != null)
            likeCount = mUploadData.getLikeObjIds().size();
        if (mUploadData.getLikeObjIds() != null && GlobalVariable.userInfo != null &&
                mUploadData.getLikeObjIds().contains(GlobalVariable.userInfo.getObjectId())){
            isLike = true;
        }
        if (mUploadData.getDislikeObjIds() != null)
            dislikeCount = mUploadData.getDislikeObjIds().size();
        if (mUploadData.getDislikeObjIds() != null && GlobalVariable.userInfo != null &&
                mUploadData.getDislikeObjIds().contains(GlobalVariable.userInfo.getObjectId())){
            isDislike = true;
        }
        if (mUploadData.getShares() != null)
            shareCount = mUploadData.getShares();
    }
    private void initView(){
        commentLayout = findViewById(R.id.comment_layout);
        commentTv = findViewById(R.id.comment_tv);
        usernameTv = findViewById(R.id.tv_username_fd);
        avatarImg = findViewById(R.id.avatar_fd);
        timeTv = findViewById(R.id.tv_time_fd);
        contentTv = findViewById(R.id.content_fd);
        shareTv = findViewById(R.id.share_tv);
        usernameTv.setText(mUploadData.getUserNmae());
        timeTv.setText(mUploadData.getCreatedAt());
        contentTv.setText(mUploadData.getContent());
        Glide.with(this).load(mUploadData.getUserAvatarUrl())
                .error(R.mipmap.avatar_off)
                .crossFade()
                .into(avatarImg);
        likeImg = findViewById(R.id.like_img_fd);
        dislikeImg = findViewById(R.id.dislike_img_fd);
        starImg = findViewById(R.id.star_img_fd);
        shareImg = findViewById(R.id.share_img_fd);
        mCollectTv = findViewById(R.id.collect_tv);
        if (starCount > 0)
            mCollectTv.setText(starCount+"");
        mDislikeTv = findViewById(R.id.dislike_tv);
        if (dislikeCount > 0)
            mDislikeTv.setText(dislikeCount+"");
        mLikeTv = findViewById(R.id.like_tv);
        if (likeCount > 0)
            mLikeTv.setText(likeCount+"");
        if (shareCount > 0)
            shareTv.setText(shareCount + " ");
        likeLayout = findViewById(R.id.like_fd);
        dislikeLayout = findViewById(R.id.dislike_fd);
        starLayout = findViewById(R.id.star_fd);
        likeLayout.setOnClickListener(this);
        dislikeLayout.setOnClickListener(this);
        starLayout.setOnClickListener(this);
        shareLayout = findViewById(R.id.share_fd);
        shareLayout.setOnClickListener(this);
        if (isLike)
            likeImg.setImageResource(R.mipmap.ic_like_yellow);
        if (isDislike)
            dislikeImg.setImageResource(R.mipmap.ic_dislike_yellow);
        if (isStar)
            starImg.setImageResource(R.mipmap.ic_star_yellow1112);
    }

    private void initImgRecyclerView(){
        mRv = findViewById(R.id.rv_img_fd);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        if (size == 1){
            layoutManager.setSpanCount(1);
            imgAdapter = new FDAdapter(this,getRvWidth()*2/3);
        }else if (size < 5){
            layoutManager.setSpanCount(2);
            imgAdapter = new FDAdapter(this,getRvWidth()/2);
        }else if (size >= 5){
            layoutManager.setSpanCount(3);
            imgAdapter = new FDAdapter(this,getRvWidth()/3);
        }
        mRv.setLayoutManager(layoutManager);

        mRv.setAdapter(imgAdapter);
        List<String> urlList = new ArrayList<>();
        if (mUploadData.getImgs() != null) {
            for (BmobFile file : mUploadData.getImgs()){
                urlList.add(file.getUrl());
            }
            imgAdapter.setDatas(urlList);
        }
    }

    private int getRvWidth(){
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
        }
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
//
//        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
//        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int marginWidth = (int) (density*42);
        return width - marginWidth;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.like_fd:
                likeClick();
                break;
            case R.id.dislike_fd:
                dislikeClick();
                break;
            case R.id.star_fd:
                starClick();
                break;
            case R.id.share_fd:
                if (GlobalVariable.userInfo == null){
                    new AlertDialog.Builder(this).setMessage("您还未登录不能分享，是否跳转到登录界面 ?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(FindDetaActivity.this,MDLoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null).show();
                    return;
                }
                toShare = true;
                if (mUploadData.getImgs() != null && mUploadData.getImgs().size() > 0){
                    String url = mUploadData.getImgs().get(0).getUrl();
                    shareText(this,mUploadData.getContent()+" "+mUploadData.getCreatedAt()+" "+url);
                }
                break;
            case R.id.fab_comment:
                if (GlobalVariable.userInfo == null){
                    dialogToLogin();
                    return;
                }
                mFabLayout.expandFab();
                mCommentEd.setText("");
                break;
            case R.id.send_comment:
                sendComment();
                break;
        }
    }
    private void likeClick(){
        // 先检查是否登录了吗，只有登录了才能进行收藏
        if (GlobalVariable.userInfo == null){
            dialogToLogin();
            return;
        }
        if (isLike){
            isLike = false; // 先置为反
            likeCount--;
            if (likeCount == 0){
                mLikeTv.setText("喜欢");
            }else {
                mLikeTv.setText(likeCount+"");
            }
            likeImg.setImageResource(R.mipmap.ic_like_grey1109);
            mUploadData.removeAll("likeObjIds", Arrays.asList(GlobalVariable.userInfo.getObjectId()));
        }else {
            isLike = true;
            likeCount++;
            mLikeTv.setText(likeCount+"");
            likeImg.setImageResource(R.mipmap.ic_like_yellow);
            if (isDislike){ // 如果是true ，表示当前用户点不喜欢过，所以要计数减一，图片换灰，反之则不用管
                isDislike = false; // 置反，为未点状态
                dislikeImg.setImageResource(R.mipmap.ic_dislike_grey1109); // 点击好评要把差评取消
                if (dislikeCount > 0){ // 不为0 情况
                    dislikeCount--;
                    if (dislikeCount == 0){
                        mDislikeTv.setText("不喜欢");
                    }else if (dislikeCount > 0){
                        mDislikeTv.setText(dislikeCount+"");
                    }
                }
                mUploadData.removeAll("dislikeObjIds", Arrays.asList(GlobalVariable.userInfo.getObjectId()));
            }
            mUploadData.addUnique("likeObjIds",GlobalVariable.userInfo.getObjectId());
        }
        mUploadData.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    isChange = true; // 更新成功，修改参评标识
//                    shareTv.setText();
                    LogUtil.e(TAG,"喜欢更新成功");
                }else {
                    LogUtil.e(TAG,e.getMessage()+" -> "+e.getErrorCode());
                }
            }
        });
    }

    /**
     *
     */
    private void dislikeClick(){
        if (GlobalVariable.userInfo == null){
            dialogToLogin();
            return;
        }
        if (isDislike){
            isDislike = false; // 先置为反
            dislikeCount--;
            if (dislikeCount == 0){
                mDislikeTv.setText("不喜欢");
            }else {
                mDislikeTv.setText(dislikeCount+"");
            }
            dislikeImg.setImageResource(R.mipmap.ic_dislike_grey1109);
//            mUploadData.removeAll("dislikeObjIds", Arrays.asList(GlobalVariable.userInfo.getObjectId()));
            mUploadData.removeAll("dislikeObjIds", Collections.singletonList(GlobalVariable.userInfo.getObjectId()));
        }else {
            isDislike = true;
            dislikeCount++;
            mDislikeTv.setText(dislikeCount+"");
            dislikeImg.setImageResource(R.mipmap.ic_dislike_yellow);
            if (isLike){
                isLike = false;
                likeImg.setImageResource(R.mipmap.ic_like_grey1109);
                if (likeCount > 0){
                    likeCount--;
                    if (likeCount == 0){
                        mLikeTv.setText("喜欢");
                    }else if (likeCount > 0){
                        mLikeTv.setText(likeCount+"");
                    }
                }
                mUploadData.removeAll("likeObjIds", Collections.singletonList(GlobalVariable.userInfo.getObjectId()));
            }
            mUploadData.addUnique("dislikeObjIds",GlobalVariable.userInfo.getObjectId());
        }
        mUploadData.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    isChange = true; // 更新成功，修改参评标识
                    LogUtil.e(TAG,"不赞更新成功");
                }else {
                    LogUtil.e(TAG,e.getMessage()+" -> "+e.getErrorCode());
                }
            }
        });
    }

    public void shareText(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "分享给他人吧"));
    }

    private void updateShareCount(){
        toShare = false;
        mUploadData.increment("shares");
        mUploadData.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    LogUtil.e(TAG," 分享加一成功");
                    shareCount++;
                    shareTv.setText(shareCount+"");
                }else {
                    LogUtil.e(TAG," 分享加一失败"+e.getMessage()+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 包含收藏情况与未收藏情况
     */
    private void starClick(){
        if (GlobalVariable.userInfo == null){
            dialogToLogin();
            return;
        }
        if (GlobalVariable.userInfo.getObjectId().equals(mUploadData.getUserObjId())){
            ToastUtil.showToast(this,"没有必要收藏自己上传的啦!");
            return;
        }
        if (isStar){ // true 为已收藏
            isStar = false; // 先置为反
            // 在已收藏状态，点击图片变灰，并减1
            starCount--;
            if (starCount == 0){
                mCollectTv.setText("收藏");
            }else {
                mCollectTv.setText(starCount+"");
            }
            starImg.setImageResource(R.mipmap.ic_star_grey1112);
            mUploadData.removeAll("collectObjIds", Collections.singletonList(GlobalVariable.userInfo.getObjectId()));
        }else {
            isStar = true;
            // 先改变为加1 的状态，然后再update 数据库，增强用户体验，但是可能会失败
            // 收藏成功收藏加1
            starCount++;
            mCollectTv.setText(starCount+"");
            //图片更换为黄色
            starImg.setImageResource(R.mipmap.ic_star_yellow1112);
            mUploadData.addUnique("collectObjIds",GlobalVariable.userInfo.getObjectId());
        }
        mUploadData.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    isChange = true; // 更新成功，修改参评标识
                    LogUtil.e(TAG,"收藏更新成功");
                }else {
                    LogUtil.e(TAG,e.getMessage()+" -> "+e.getErrorCode());
                }
            }
        });
    }
    private void dialogToLogin(){
        new AlertDialog.Builder(this).setMessage("您还未登录不能参评，是否跳转到登录界面 ?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(FindDetaActivity.this,MDLoginActivity.class));
                    }
                })
                .setNegativeButton("取消", null).show();
    }


    //*************************** 开始评论的
    private void initCommentRecyclerView(){
        commentRv = findViewById(R.id.comment_rv);
        commentRv.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentRv.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(this,mUploadData);
        commentRv.setAdapter(commentAdapter);

        //设置悬浮按钮
        mFabLayout = findViewById(R.id.fab_layout);
        mFab = findViewById(R.id.fab_comment);
        mFabLayout.setFab(mFab);
        mSend = findViewById(R.id.send_comment);
        mCommentEd = findViewById(R.id.et_comment);
        mSend.setOnClickListener(this);
        mFab.setOnClickListener(this);
        queryComment();

    }

    private void queryComment(){
        BmobQuery<CommentData> query = new BmobQuery<>();
//        if (GlobalVariable.userInfo == null){
//            ToastUtil.showToast(FDActivity.this,"没登录");
//            return;
//        }
        query.addWhereEqualTo("uploadObjId",mUploadData.getObjectId());
        query.order("createdAt");
//        LogUtil.e(TAG,mUploadData.getObjectId());
        query.findObjects(new FindListener<CommentData>() {
            @Override
            public void done(List<CommentData> list, BmobException e) {
                if (e == null){
                    if (list.size() != 0){
                        commentLayout.setVisibility(View.VISIBLE);
                        commentAdapter.setDatas(list);
                        commentCount = list.size();
                        commentTv.setText(commentCount+"");
                    }

                }else {
                    LogUtil.e(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 发送评论
     */
    private void sendComment(){
        if (TextUtils.isEmpty(mCommentEd.getText().toString().trim())){
            ToastUtil.showToast(this,"请输入呀发送的信息！");
            return;
        }
        final CommentData data = new CommentData();
        data.setContent(mCommentEd.getText().toString().trim());
        data.setUploadObjId(mUploadData.getObjectId());
        if (!TextUtils.isEmpty(GlobalVariable.userInfo.getNickName())){
            data.setUsername(GlobalVariable.userInfo.getNickName());
        }else {
            data.setUsername("用户"+GlobalVariable.userInfo.getUsername());
        }
        data.setUserAvatarUrl(GlobalVariable.userInfo.getAvatar() == null ? "":
                GlobalVariable.userInfo.getAvatar().getUrl());
        data.setUserId(GlobalVariable.userInfo.getObjectId());
        data.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    isChange = true; // 成功，修改参评标识
                    commentAdapter.addItem(data);
                    mUploadData.increment("commentCount",1);
                    mUploadData.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                mCommentEd.setText("");
                                isChange = true; // 更新成功，修改参评标识
                                commentCount++;
                                commentTv.setText(commentCount+"");
                                ToastUtil.showToast(FindDetaActivity.this,"评论成功");
                            }else {
                                ToastUtil.showToast(FindDetaActivity.this,"评论成功，可能网络状态不好");
                            }
                        }
                    });
                }else {
                    LogUtil.e(TAG,"上传评论失败");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fd, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.pack_up:
                mFabLayout.contractFab();
                break;
        }
        return true;
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
